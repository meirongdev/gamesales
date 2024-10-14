package dev.meirong.demos.gamesales.repository;

import dev.meirong.demos.gamesales.domain.DailySale;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DailySaleRepo extends JpaRepository<DailySale, Long> {

  @Query(
      "SELECT SUM(ds.totalSales) as totalSales, SUM(ds.totalCount) as totalCount FROM"
          + " DailySale ds WHERE ds.date BETWEEN :fromDate AND :toDate")
  TotalSalesSummary findTotalSalesSummaryBetweenDates(LocalDate fromDate, LocalDate toDate);

  @Query(
      "SELECT SUM(ds.totalSales) as totalSales, SUM(ds.totalCount) as totalCount FROM"
          + " DailySale ds WHERE ds.date BETWEEN :fromDate AND :toDate AND ds.gameNo = :gameNo")
  TotalSalesSummary findTotalSalesSummaryByGameNoBetweenDates(
      LocalDate fromDate, LocalDate toDate, Integer gameNo);

  Optional<DailySale> findByGameNoAndDate(Integer gameNo, LocalDate date);
}
