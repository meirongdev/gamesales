package dev.meirong.demos.gamesales.exception;

public class CsvException extends RuntimeException {

  public CsvException(String message) {
    super(message);
  }

  public CsvException(String message, Throwable cause) {
    super(message, cause);
  }
}
