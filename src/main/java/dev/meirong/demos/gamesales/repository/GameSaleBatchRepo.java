package dev.meirong.demos.gamesales.repository;

import dev.meirong.demos.gamesales.domain.GameSale;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
public class GameSaleBatchRepo {

  private JdbcTemplate jdbcTemplate;

  public GameSaleBatchRepo(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Transactional
  public void batchInsert(List<GameSale> gameSales) {
    long startTime = System.currentTimeMillis(); // 记录开始时间
    // String sql =
    //     "INSERT INTO game_sales (game_no, game_name, game_code, type, cost_price, tax,
    // sale_price,"
    //         + " date_of_sale) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    // jdbcTemplate.batchUpdate(
    // sql,
    // gameSales,
    // gameSales.size(),
    // (ps, gameSale) -> {
    // ps.setInt(1, gameSale.getGameNo());
    // ps.setString(2, gameSale.getGameName());
    // ps.setString(3, gameSale.getGameCode());
    // ps.setInt(4, gameSale.getType());
    // ps.setBigDecimal(5, gameSale.getCostPrice());
    // ps.setBigDecimal(6, gameSale.getTax());
    // ps.setBigDecimal(7, gameSale.getSalePrice());
    // ps.setTimestamp(8, java.sql.Timestamp.from(gameSale.getDateOfSale()));
    // });

    int batchSize = gameSales.size();
    for (int i = 0; i < gameSales.size(); i += batchSize) {
      List<GameSale> batchList = gameSales.subList(i, Math.min(i + batchSize, gameSales.size()));
      String sql = buildInsertSql(batchList);
      jdbcTemplate.update(sql);
    }
    long endTime = System.currentTimeMillis(); // 记录结束时间
    log.info("batch insert {} records, cost {} ms", gameSales.size(), endTime - startTime);
  }

  private String buildInsertSql(List<GameSale> gameSales) {
    String baseSql =
        "INSERT INTO game_sales (game_no, game_name, game_code, type, cost_price, tax, sale_price,"
            + " date_of_sale) VALUES ";
    String valuesSql =
        gameSales.stream()
            .map(
                gameSale ->
                    String.format(
                        "(%d, '%s', '%s', %d, %s, %s, %s, '%s')",
                        gameSale.getGameNo(),
                        gameSale.getGameName().replace("'", "''"),
                        gameSale.getGameCode().replace("'", "''"),
                        gameSale.getType(),
                        gameSale.getCostPrice(),
                        gameSale.getTax(),
                        gameSale.getSalePrice(),
                        Timestamp.from(gameSale.getDateOfSale())))
            .collect(Collectors.joining(", "));
    return baseSql + valuesSql;
  }
}
