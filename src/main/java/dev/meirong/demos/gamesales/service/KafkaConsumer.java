package dev.meirong.demos.gamesales.service;

import dev.meirong.demos.gamesales.domain.GameSale;
import dev.meirong.demos.gamesales.domain.ImportStatus;
import dev.meirong.demos.gamesales.repository.CsvImportLogRepo;
import dev.meirong.demos.gamesales.repository.GameSaleRepo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

  private final GameSaleRepo gameSaleRepo;
  private final String uploadDir;

  private final CsvImportLogRepo csvImportLogRepo;
  private final Validator validator;

  public KafkaConsumer(
      @Value("${file.upload-dir}") String uploadDir,
      GameSaleRepo gameSaleRepo,
      CsvImportLogRepo csvImportLogRepo) {
    this.gameSaleRepo = gameSaleRepo;
    this.uploadDir = uploadDir;
    this.csvImportLogRepo = csvImportLogRepo;
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    this.validator = factory.getValidator();
  }

  @KafkaListener(topics = "csv-import", groupId = "gamesales-import-log-group")
  public void consume(String fileName) {
    // check fileName in cs_import_log
    var importLogOptional = csvImportLogRepo.findById(fileName);
    if (!importLogOptional.isPresent()) {
      log.error(fileName + " not found");
      return;
    }
    var importLog = importLogOptional.get();
    importLog.setImportStatus(ImportStatus.IMPORTING);
    importLog.setUpdatedAt(Instant.now());
    importLog = csvImportLogRepo.save(importLog);

    // check file exists
    String filePath = uploadDir + File.separator + fileName;
    if (!Files.exists(Paths.get(filePath))) {
      log.error(fileName + " not found");
      return;
    }
    var head = true;
    var successCount = 0;
    var failureCount = 0;
    List<GameSale> gameSales = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (head) {
          head = false;
          continue;
        }
        if (line.isBlank()) {
          continue;
        }
        String[] values = line.split(",");
        if (values.length != 9) {
          log.error("invalid line:" + line);
          failureCount++;
          continue;
        }

        Optional<GameSale> gameSale = convertToGameSale(values);
        if (!gameSale.isPresent()) {
          failureCount++;
          continue;
        }

        gameSales.add(gameSale.get());
        successCount++;

        if (gameSales.size() >= 200) {
          gameSaleRepo.saveAll(gameSales);
          gameSales.clear();
        }
      }

      if (!gameSales.isEmpty()) {
        gameSaleRepo.saveAll(gameSales);
      }

      importLog.setSuccessCount(successCount);
      importLog.setFailureCount(failureCount);
      if (failureCount > 0) {
        importLog.setImportStatus(ImportStatus.FAILURE);
      } else {
        importLog.setImportStatus(ImportStatus.SUCCESS);
      }
      csvImportLogRepo.save(importLog);
      log.info("File processed and saved to database: " + fileName);
    } catch (IOException e) {
      log.error("Error processing file: " + fileName, e);
      importLog.setImportStatus(ImportStatus.FAILURE);
      csvImportLogRepo.save(importLog);
    }
  }

  private Optional<GameSale> convertToGameSale(String[] values) {
    GameSale gameSale = new GameSale();
    gameSale.setGameNo(Integer.parseInt(values[1]));
    gameSale.setGameName(values[2]);
    gameSale.setGameCode(values[3]);
    gameSale.setType(Integer.parseInt(values[4]));
    gameSale.setCostPrice(new BigDecimal(values[5]));
    gameSale.setTax(new BigDecimal(values[6]));
    gameSale.setSalePrice(new BigDecimal(values[7]));
    var dateOfSale = values[8];
    gameSale.setDateOfSale(parse(dateOfSale));
    try {
      validateGameSale(gameSale);
    } catch (IllegalArgumentException e) {
      log.error("Validation failed for game sale: " + gameSale + " due to: " + e.getMessage());
      gameSale = null;
    }
    return Optional.ofNullable(gameSale);
  }

  private void validateGameSale(GameSale gameSale) {
    Set<ConstraintViolation<GameSale>> violations = validator.validate(gameSale);
    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (ConstraintViolation<GameSale> violation : violations) {
        sb.append(violation.getMessage()).append("; ");
      }
      throw new IllegalArgumentException(sb.toString());
    }
  }

  private Instant parse(String dateTimeString) {
    // Define the formatter for the input string
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Parse the string to LocalDateTime
    LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);

    // Get the ZoneId for Singapore
    ZoneId singaporeZone = ZoneId.of("Asia/Singapore");
    // Convert LocalDateTime to Instant using Singapore's time zone
    return localDateTime.atZone(singaporeZone).toInstant();
  }
}
