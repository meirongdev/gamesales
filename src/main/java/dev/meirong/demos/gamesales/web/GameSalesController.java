package dev.meirong.demos.gamesales.web;

import dev.meirong.demos.gamesales.domain.CsvImportLog;
import dev.meirong.demos.gamesales.exception.NotFoundException;
import dev.meirong.demos.gamesales.service.CsvService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class GameSalesController {

  private final CsvService csvService;

  public GameSalesController(CsvService csvService) {
    this.csvService = csvService;
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
}
