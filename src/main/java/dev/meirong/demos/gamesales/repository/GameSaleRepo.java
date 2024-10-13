package dev.meirong.demos.gamesales.repository;

import dev.meirong.demos.gamesales.domain.GameSale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSaleRepo extends JpaRepository<GameSale, Long> {}
