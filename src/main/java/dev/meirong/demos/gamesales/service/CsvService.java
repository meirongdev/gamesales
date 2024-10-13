package dev.meirong.demos.gamesales.service;

import dev.meirong.demos.gamesales.domain.CsvImportLog;
import dev.meirong.demos.gamesales.exception.CsvException;
import dev.meirong.demos.gamesales.repository.CsvImportLogRepo;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class CsvService {
  private final String uploadDir;

  private final CsvImportLogRepo csvImportLogRepo;

  public CsvService(
      @Value("${file.upload-dir}") String uploadDir, CsvImportLogRepo csvImportLogRepo) {
    this.uploadDir = uploadDir;
    this.csvImportLogRepo = csvImportLogRepo;
  }

  public String importCsv(MultipartFile file) {
    String localFileName = UUID.randomUUID().toString();
    try (InputStream inputStream = file.getInputStream()) {
      // Check the upload directory exists
      File dir = new File(uploadDir);
      if (!dir.exists()) {
        dir.mkdirs();
      }

      // Save the uploaded file to the upload directory
      File newFile = new File(uploadDir + File.separator + localFileName);

      try (BufferedOutputStream outputStream =
          new BufferedOutputStream(new FileOutputStream(newFile))) {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
      }
      return logImportToDatabase(file.getOriginalFilename(), localFileName);
    } catch (IOException e) {
      log.error(uploadDir, e);
      throw new CsvException("Fail to save data");
    }
  }

  private String logImportToDatabase(String originalFileName, String localFileName) {
    CsvImportLog logEntry =
        CsvImportLog.builder()
            .importId(localFileName)
            .originalFileName(originalFileName)
            .importStatus(0)
            .createdAt(Instant.now())
            .build();

    CsvImportLog savedLog = csvImportLogRepo.save(logEntry);
    return savedLog.getImportId();
  }

  public Optional<CsvImportLog> getImportLogByTraceNo(String traceNo) {
    return csvImportLogRepo.findById(traceNo);
  }
}
