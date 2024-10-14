package dev.meirong.demos.gamesales.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
    name = "daily_sales",
    indexes = {
      @Index(name = "idx_date", columnList = "date"),
      @Index(name = "idx_game_no_date", columnList = "game_no, date")
    })
public class DailySale {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "game_no")
  private Integer gameNo;

  @Column(name = "date", nullable = false)
  private LocalDate date;

  @Column(name = "total_sales", nullable = false)
  private BigDecimal totalSales;

  @Column(name = "total_count", nullable = false)
  private Integer totalCount;
}
