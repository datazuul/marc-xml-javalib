package com.datazuul.metadata.marc.xml;

import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.marc4j.marc.Record;

public class MarcXmlTest {

  @Test
  public void testGetDCMetadata() {
    final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("marc21-sandburg.xml");
    Record record = MarcXmlRecord.from(resourceAsStream);
    MarcXml marcXml = new MarcXml(record);

    // dc:date
    assertEquals("c1993.", marcXml.getDCDate());
  }
}
