package dev.meirong.demos.gamesales.service;

import dev.meirong.demos.gamesales.repository.DailySaleRepo;
import dev.meirong.demos.gamesales.repository.TotalSalesSummary;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class DailySaleService {
  private final DailySaleRepo dailySaleRepo;

  public DailySaleService(DailySaleRepo dailySaleRepo) {
    this.dailySaleRepo = dailySaleRepo;
  }

  public Summary getTotalSales(LocalDate fromDate, LocalDate toDate) {
    TotalSalesSummary summary = dailySaleRepo.findTotalSalesSummaryBetweenDates(fromDate, toDate);
    BigDecimal totalSales =
        summary.getTotalSales() != null ? summary.getTotalSales() : BigDecimal.ZERO;
    Integer totalCount = summary.getTotalCount() != null ? summary.getTotalCount() : 0;
    return new Summary(totalSales, totalCount);
  }

  public Summary getTotalSalesByGameNo(LocalDate fromDate, LocalDate toDate, Integer gameNo) {
    TotalSalesSummary summary =
        dailySaleRepo.findTotalSalesSummaryByGameNoBetweenDates(fromDate, toDate, gameNo);
    BigDecimal totalSales =
        summary.getTotalSales() != null ? summary.getTotalSales() : BigDecimal.ZERO;
    Integer totalCount = summary.getTotalCount() != null ? summary.getTotalCount() : 0;
    return new Summary(totalSales, totalCount);
  }

  public record Summary(BigDecimal totalSales, Integer totalCount) {}
}
