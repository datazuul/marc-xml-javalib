package com.datazuul.metadata.marc.xml.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.marc4j.marc.Record;

import com.datazuul.metadata.dublincore.DublinCore;
import com.datazuul.metadata.marc.xml.MarcXml;
import com.datazuul.metadata.marc.xml.MarcXmlRecord;

class MarcXml2DublinCoreTest {

  private static MarcXml marcXml;
  private static DublinCore dc;

  @BeforeAll
  static void beforeAll() {
	final InputStream resourceAsStream = MarcXml2DublinCoreTest.class.getClassLoader().getResourceAsStream("marc21-sandburg.xml");
	Record record = MarcXmlRecord.from(resourceAsStream);
	marcXml = new MarcXml(record);
	dc = marcXml.toDublinCore();
  }

  @Test
  void testCreators() {
	List<String> creators = dc.getCreators();
	assertEquals(Arrays.asList("Sandburg, Carl, 1878-1967.", "Rand, Ted, ill."), creators);
  }

  @Test
  void testDates() {
	List<String> dates = dc.getDates();
	assertEquals(Arrays.asList("c1993."), dates);
  }

  @Test
  void testLanguage() {
	assertEquals("eng", dc.getLanguage());
  }

  @Test
  void testPublishers() {
	List<String> publishers = dc.getPublishers();
	assertEquals(Arrays.asList("San Diego :Harcourt Brace Jovanovich,"), publishers);
  }
}
