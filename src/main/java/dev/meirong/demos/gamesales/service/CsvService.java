package dev.meirong.demos.gamesales.service;

import dev.meirong.demos.gamesales.exception.CsvException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class CsvService {

  private final String uploadDir;

  public CsvService(@Value("${file.upload-dir}") String uploadDir) {
    this.uploadDir = uploadDir;
  }

  public void importCsv(MultipartFile file) {
    try (InputStream inputStream = file.getInputStream()) {
      // Check the upload directory exists
      File dir = new File(uploadDir);
      if (!dir.exists()) {
        dir.mkdirs();
      }

      // Save the uploaded file to the upload directory
      File newFile = new File(uploadDir + File.separator + file.getOriginalFilename());

      try (BufferedOutputStream outputStream =
          new BufferedOutputStream(new FileOutputStream(newFile))) {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
      }
    } catch (IOException e) {
      log.error(uploadDir, e);
      throw new CsvException("Fail to save data");
    }
  }
}
