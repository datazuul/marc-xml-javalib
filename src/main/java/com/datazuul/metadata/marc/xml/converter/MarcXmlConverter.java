package com.datazuul.metadata.marc.xml.converter;

public interface MarcXmlConverter<T extends Object> {
  public T convert();
}
