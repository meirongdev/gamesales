package dev.meirong.demos.gamesales.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ImportStatusConverter implements AttributeConverter<ImportStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(ImportStatus attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.getValue();
  }

  @Override
  public ImportStatus convertToEntityAttribute(Integer dbData) {
    if (dbData == null) {
      return null;
    }
    return ImportStatus.fromValue(dbData);
  }
}
