package dev.meirong.demos.gamesales.web;

import dev.meirong.demos.gamesales.domain.CsvImportLog;
import dev.meirong.demos.gamesales.domain.GameSale;
import dev.meirong.demos.gamesales.exception.NotFoundException;
import dev.meirong.demos.gamesales.service.CsvService;
import dev.meirong.demos.gamesales.service.DailySaleService;
import dev.meirong.demos.gamesales.service.GameSaleService;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class GameSalesController {

  private final CsvService csvService;

  private final GameSaleService gameSaleService;

  private final DailySaleService dailySaleService;

  public GameSalesController(
      CsvService csvService, GameSaleService gameSaleService, DailySaleService dailySaleService) {
    this.csvService = csvService;
    this.gameSaleService = gameSaleService;
    this.dailySaleService = dailySaleService;
  }

  @PostMapping("/import")
  public Map<String, Object> uploadLargeFile(@RequestParam("file") MultipartFile file) {
    var importId = csvService.importCsv(file);
    Map<String, Object> response = new HashMap<>();
    response.put("message", file.getOriginalFilename() + " uploaded successfully.");
    response.put("trace no", importId);
    return response;
  }

  // curl -v GET http://localhost:8080/status/import/eb07e137-71ce-4555-9732-56804a8644f8
  @GetMapping("/import/status/{traceNo}")
  public CsvImportLog getImportLogByTraceNo(@PathVariable String traceNo) {
    return csvService
        .getImportLogByTraceNo(traceNo)
        .orElseThrow(() -> new NotFoundException("Log not found for trace no: " + traceNo));
  }

  // curl -v GET
  // http://localhost:8080/getGameSales?fromDate=2021-10-09T00:00:00Z&toDate=2024-10-10T23:59:59Z
  // curl -v GET http://localhost:8080/getGameSales?salePrice=20&priceComparison=less
  @GetMapping("/getGameSales")
  public Page<GameSale> getGameSales(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "100") int size,
      @RequestParam(required = false) Instant fromDate,
      @RequestParam(required = false) Instant toDate,
      @RequestParam(required = false) BigDecimal salePrice,
      @RequestParam(required = false) String priceComparison) {

    PageRequest pageable = PageRequest.of(page, size);

    if (fromDate != null && toDate != null) {
      return gameSaleService.getGameSalesByDateRange(fromDate, toDate, pageable);
    } else if (salePrice != null && "less".equalsIgnoreCase(priceComparison)) {
      return gameSaleService.getGameSalesBySalePriceLessThan(salePrice, pageable);
    } else if (salePrice != null && "greater".equalsIgnoreCase(priceComparison)) {
      return gameSaleService.getGameSalesBySalePriceGreaterThan(salePrice, pageable);
    } else {
      return gameSaleService.getAllGameSales(pageable);
    }
  }

  // curl -v GET http://localhost:8080/getTotalSales?fromDate=2021-10-09&toDate=2024-10-10
  @GetMapping("/getTotalSales")
  public DailySaleService.Summary getTotalSales(
      @RequestParam LocalDate fromDate,
      @RequestParam LocalDate toDate,
      @RequestParam(required = false) Integer gameNo) {

    if (gameNo != null) {
      return dailySaleService.getTotalSalesByGameNo(fromDate, toDate, gameNo);
    } else {
      return dailySaleService.getTotalSales(fromDate, toDate);
    }
  }
}
