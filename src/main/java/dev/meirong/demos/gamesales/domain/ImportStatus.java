package dev.meirong.demos.gamesales.domain;

public enum ImportStatus {
  TO_IMPORT(0),
  IMPORTING(1),
  SUCCESS(2),
  FAILURE(3);

  private final int value;

  ImportStatus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static ImportStatus fromValue(int value) {
    for (ImportStatus status : ImportStatus.values()) {
      if (status.value == value) {
        return status;
      }
    }
    throw new IllegalArgumentException("Unknown value: " + value);
  }
}
