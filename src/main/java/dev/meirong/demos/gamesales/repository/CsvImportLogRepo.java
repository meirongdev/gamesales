package dev.meirong.demos.gamesales.repository;

import dev.meirong.demos.gamesales.domain.CsvImportLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CsvImportLogRepo extends JpaRepository<CsvImportLog, String> {}
