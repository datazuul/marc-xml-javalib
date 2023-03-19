package com.datazuul.metadata.marc.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.marc4j.marc.Record;

public class MarcXmlTest {

  @Test
  public void testMetadata() {
    final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("marc21-sandburg.xml");
    Record record = MarcXmlRecord.from(resourceAsStream);
    MarcXml marcXml = new MarcXml(record);

    assertNotNull(marcXml.getRecord());
    
    List<String> editionStatements = marcXml.getEditionStatements();
    assertEquals(Arrays.asList("1st ed."), editionStatements);
    
    List<String> publicationPlaces = marcXml.getPublicationPlaces();
    assertEquals(Arrays.asList("San Diego :"), publicationPlaces);
  }
}
