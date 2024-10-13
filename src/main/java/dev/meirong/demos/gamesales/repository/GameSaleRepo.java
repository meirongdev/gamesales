package dev.meirong.demos.gamesales.repository;

import dev.meirong.demos.gamesales.domain.GameSale;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSaleRepo extends JpaRepository<GameSale, Long> {
  Page<GameSale> findByDateOfSaleBetween(Instant fromDate, Instant toDate, Pageable pageable);

  Page<GameSale> findBySalePriceLessThan(BigDecimal salePrice, Pageable pageable);

  Page<GameSale> findBySalePriceGreaterThan(BigDecimal salePrice, Pageable pageable);
}
