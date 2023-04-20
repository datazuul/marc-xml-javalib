package com.datazuul.metadata.marc.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

import com.datazuul.metadata.dublincore.DublinCore;
import com.datazuul.metadata.marc.xml.converter.MarcXml2DublinCore;

/**
 * MARC standards: https://www.loc.gov/marc/<br>
 * MARC 21 Format for bibliographic data:
 * https://www.loc.gov/marc/bibliographic/ and
 * https://www.loc.gov/marc/MARC_2012_Concise_PDF/Part3_Bibliographic.pdf<br>
 * MARC-XML standard: https://www.loc.gov/standards/marcxml/ and example
 * documents<br>
 * MARC-XML to Dublin Core:
 * https://www.loc.gov/standards/marcxml/xslt/MARC21slim2OAIDC.xsl and
 * http://www.loc.gov/standards/marcxml/xslt/MARC21slimUtils.xsl
 */
public class MarcXml {

  public static String concatenate(List<Subfield> subfields, String delimiter) {
    StringBuilder sb = new StringBuilder();
    for (Subfield subfield : subfields) {
      final String data = subfield.getData();
      if (data != null && !data.isBlank()) {
        sb.append(data).append(delimiter);
      }
    }
    String result = sb.toString().trim();
    return result;
  }

  private final Record record;

  public MarcXml(Record record) {
    this.record = record;
  }

  public String getControlFieldByTag(String tag) {
    return record.getControlFields().stream().filter(cf -> tag.equals(cf.getTag())).findFirst().orElse(null).getData();
  }

  public List<DataField> getDataFieldsByTag(String tag) {
    List<DataField> dataFields = record.getDataFields();
    List<DataField> matchingDataFields = dataFields.stream().filter(df -> tag.equals(df.getTag()))
        .collect(Collectors.toList());
    return matchingDataFields;
  }

  /**
   * Get character in leader element on given position. (Index starts at 0).
   */
  public char getLeader(int pos) {
    char[] leaderChars = record.getLeader().toString().toCharArray();
    return leaderChars[pos];
  }

  public List<String> getSubfieldsByTagAndCodes(String tag, String codes) {
    List<String> result = null;
    List<DataField> dataFields = getDataFieldsByTag(tag);
    for (DataField dataField : dataFields) {
      List<Subfield> subfields = dataField.getSubfields(codes);
      // inside one datafield: concatenate subfields data
      String data = concatenate(subfields, " ");

      // add subfields data of datafield to list
      if (result == null) {
        result = new ArrayList<>();
      }
      result.add(data);
    }
    return result;
  }

  /**
   * <p>
   * 260: https://www.loc.gov/marc/bibliographic/concise/bd260.html<br>
   * Information relating to the publication, printing, distribution, issue,
   * release, or production of a work.
   * 
   * <p>
   * $a - Place of publication, distribution, etc. (R)<br>
   * May contain the abbreviation [S.l.] when the place is unknown.
   */
  public List<String> getPublicationPlaces() {
    return getSubfieldsByTagAndCodes("260", "a");
  }

  /**
   * <p>
   * 250: https://www.loc.gov/marc/bibliographic/bd250.html<br>
   * Information relating to the edition of a work as determined by applicable
   * cataloging rules.
   * 
   * <p>
   * $a - Edition statement (NR)<br>
   * $b - Remainder of edition statement (NR)<br>
   * Usually, a statement of personal or corporate responsibility and/or a
   * parallel edition statement.
   */
  public List<String> getEditionStatements() {
    return getSubfieldsByTagAndCodes("250", "ab");
  }

  public Set<String> getIdentifiers() {
    Set<String> identifiers = new HashSet<>();
    List<DataField> dataFields = record.getDataFields();
    for (DataField dataField : dataFields) {
      if ("035".equals(dataField.getTag())) {
        Subfield subfield = dataField.getSubfield('a');
        if (subfield != null) {
          String identifier = subfield.getData();
          identifiers.add(identifier);
        } else {
          // "9" happened to be in LOC data: https://lccn.loc.gov/12027826/marcxml
          subfield = dataField.getSubfield('9');
          if (subfield != null) {
            String identifier = subfield.getData();
            identifiers.add(identifier);
          }
        }
      }
    }
    return identifiers;
  }

  public Record getRecord() {
    return record;
  }

  /**
   * leader6: https://www.loc.gov/marc/bibliographic/concise/bdleader.html Type of
   * record. One-character alphabetic code used to define the characteristics and
   * components of the record.
   */
  public char getTypeOfRecord() {
    return record.getLeader().getTypeOfRecord();
  }

  public DublinCore toDublinCore() {
    MarcXml2DublinCore marcXml2DublinCore = new MarcXml2DublinCore(this);
    return marcXml2DublinCore.convert();
  }
}
