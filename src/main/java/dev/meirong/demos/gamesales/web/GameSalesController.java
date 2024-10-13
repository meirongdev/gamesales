package dev.meirong.demos.gamesales.web;

import dev.meirong.demos.gamesales.service.CsvService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<String> uploadLargeFile(@RequestParam("file") MultipartFile file) {
    csvService.importCsv(file);
    return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully");
  }
}
