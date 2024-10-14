package dev.meirong.demos.gamesales.repository;

import java.math.BigDecimal;

public interface TotalSalesSummary {
  BigDecimal getTotalSales();

  Integer getTotalCount();
}
