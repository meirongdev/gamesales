package dev.meirong.demos.gamesales.service;

import dev.meirong.demos.gamesales.domain.GameSale;
import dev.meirong.demos.gamesales.repository.GameSaleRepo;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GameSaleService {

  private final GameSaleRepo gameSaleRepo;

  public GameSaleService(GameSaleRepo gameSaleRepo) {
    this.gameSaleRepo = gameSaleRepo;
  }

  public Page<GameSale> getAllGameSales(Pageable pageable) {
    return gameSaleRepo.findAll(pageable);
  }

  public Page<GameSale> getGameSalesByDateRange(
      Instant fromDate, Instant toDate, Pageable pageable) {
    return gameSaleRepo.findByDateOfSaleBetween(fromDate, toDate, pageable);
  }

  public Page<GameSale> getGameSalesBySalePriceLessThan(BigDecimal salePrice, Pageable pageable) {
    return gameSaleRepo.findBySalePriceLessThan(salePrice, pageable);
  }

  public Page<GameSale> getGameSalesBySalePriceGreaterThan(
      BigDecimal salePrice, Pageable pageable) {
    return gameSaleRepo.findBySalePriceGreaterThan(salePrice, pageable);
  }
}
