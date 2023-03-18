package com.datazuul.metadata.marc.xml;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.marc4j.marc.Record;

public class MarcXmlTest {

  @Test
  public void testGetDCMetadata() {
    final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("marc21-sandburg.xml");
    Record record = MarcXmlRecord.from(resourceAsStream);
    MarcXml marcXml = new MarcXml(record);

    assertNotNull(marcXml.getRecord());
  }
}
