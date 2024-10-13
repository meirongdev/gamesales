package dev.meirong.demos.gamesales.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "game_sales")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GameSale {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Min(1)
  @Max(100)
  @Column(name = "game_no", nullable = false)
  private Integer gameNo;

  @Size(max = 20)
  @NotNull
  @Column(name = "game_name", length = 20, nullable = false)
  private String gameName;

  @Size(max = 5)
  @NotNull
  @Column(name = "game_code", length = 5, nullable = false)
  private String gameCode;

  @Min(1)
  @Max(2)
  @Column(name = "type", nullable = false)
  private Integer type;

  @DecimalMax("100.00")
  @Column(name = "cost_price", nullable = false)
  private BigDecimal costPrice;

  @DecimalMin("0.09")
  @DecimalMax("0.09")
  @Column(name = "tax", nullable = false)
  private BigDecimal tax;

  @Column(name = "sale_price", nullable = false)
  private BigDecimal salePrice;

  @Column(name = "date_of_sale", nullable = false)
  private Instant dateOfSale;
}
