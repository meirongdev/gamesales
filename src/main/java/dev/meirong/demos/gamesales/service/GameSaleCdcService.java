package dev.meirong.demos.gamesales.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.meirong.demos.gamesales.domain.DailySale;
import dev.meirong.demos.gamesales.repository.DailySaleRepo;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameSaleCdcService {

  private final DailySaleRepo dailySaleRepo;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public GameSaleCdcService(DailySaleRepo dailySaleRepo) {
    this.dailySaleRepo = dailySaleRepo;
  }

  @KafkaListener(topics = "dbserver1.gamesales.game_sales", groupId = "game-sales-group")
  @Transactional
  public void handleGameSaleChange(String message) throws Exception {
    log.debug("Received message: {}", message);
    JsonNode rootNode = objectMapper.readTree(message);
    JsonNode payload = rootNode.path("payload");

    if (payload.has("after")) {
      JsonNode after = payload.path("after");

      Integer gameNo = after.path("game_no").asInt();
      BigDecimal salePrice = new BigDecimal(after.path("sale_price").asText());
      long microseconds = Long.parseLong(after.path("date_of_sale").asText());
      Instant dateOfSale =
          Instant.ofEpochSecond(microseconds / 1_000_000, (microseconds % 1_000_000) * 1_000);

      updateDailySales(gameNo, salePrice, dateOfSale);
    }
  }

  private void updateDailySales(Integer gameNo, BigDecimal salePrice, Instant dateOfSale) {
    ZoneId singaporeZone = ZoneId.of("Asia/Singapore");
    LocalDate saleDate = dateOfSale.atZone(singaporeZone).toLocalDate();

    Optional<DailySale> existingDailySale = dailySaleRepo.findByGameNoAndDate(gameNo, saleDate);

    if (existingDailySale.isPresent()) {
      DailySale dailySale = existingDailySale.get();
      dailySale.setTotalSales(dailySale.getTotalSales().add(salePrice));
      dailySale.setTotalCount(dailySale.getTotalCount() + 1);
      dailySaleRepo.save(dailySale);
    } else {
      DailySale newDailySale = new DailySale();
      newDailySale.setGameNo(gameNo);
      newDailySale.setDate(saleDate);
      newDailySale.setTotalSales(salePrice);
      newDailySale.setTotalCount(1);
      dailySaleRepo.save(newDailySale);
    }
  }
}
