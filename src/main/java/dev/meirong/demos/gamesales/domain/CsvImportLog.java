package dev.meirong.demos.gamesales.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Entity class for CSV import log. */
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CsvImportLog {

  @EqualsAndHashCode.Include
  @Id
  @Column(length = 36)
  private String importId;

  @Column(length = 100, nullable = false)
  private String originalFileName;

  @Convert(converter = ImportStatusConverter.class)
  @Column(length = 1)
  private ImportStatus importStatus;

  private int successCount;
  private int failureCount;

  @Column(updatable = false)
  private Instant createdAt;

  private Instant updatedAt;
}
