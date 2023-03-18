package com.datazuul.metadata.marc.xml;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

import com.datazuul.metadata.dublincore.DublinCore;
import com.datazuul.metadata.marc.xml.converter.MarcXml2DublinCore;

/**
 * MARC standards: https://www.loc.gov/marc/<br>
 * MARC 21 Format for bibliographic data: https://www.loc.gov/marc/bibliographic/ and https://www.loc.gov/marc/MARC_2012_Concise_PDF/Part3_Bibliographic.pdf<br>
 * MARC-XML standard: https://www.loc.gov/standards/marcxml/ and example documents<br>
 * MARC-XML to Dublin Core: https://www.loc.gov/standards/marcxml/xslt/MARC21slim2OAIDC.xsl and http://www.loc.gov/standards/marcxml/xslt/MARC21slimUtils.xsl
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

  /*
  "Information in field 260 is similar to information in field 264 (Production, Publication, Distribution, Manufacture, and Copyright Notice). Field 260 is useful for cases where the content standard or institutional policies used do not make a distinction between functions"
  
  The 264 essentially replaced the 260 when RDA was implemented. You can read the PCC Guidelines.

I wouldn't change the fields. For the most part, AACR2 records and RDA records should be able to co-exist in your ILS/discovery system.
  
  Regarding copy cataloging or managing old records: I usually try not to overwrite/delete data in my records, so if I have a 260 line but no 264, I'll add the 264 for RDA compliance and leave the 260 for consistency with the pre-RDA record. If I have a 264 and no 260, then I simply leave it as is because that's covering the more up to date requirement.

Regarding original records: I use 264 only and follow RDA compliance.
   */
  public String getDCDate() {
    String result = null;
    List<DataField> dataFields = record.getDataFields();
    for (DataField dataField : dataFields) {
      if ("260".equals(dataField.getTag())) {
        List<Subfield> subfields = dataField.getSubfields("c"); // $c - Date of publication, distribution, etc. (R)
        result = concatenate(subfields, " ");
      }
    }
    if (result == null || result.isBlank()) {
      for (DataField dataField : dataFields) {
        if ("264".equals(dataField.getTag())) {
          List<Subfield> subfields = dataField.getSubfields("c"); // $c - Date of production, publication, distribution, manufacture, or copyright notice (R)
          result = concatenate(subfields, " ");
        }
      }
    }
    return result;
  }

  public String getDCLanguage() {
    // language (dc:language): control field with tag 008
    ControlField field = (ControlField) record.getVariableField("008");
    String data = field.getData();

    // the three-character MARC language code takes character positions 35-37
    // Three-character alphabetic code that indicates the language of the item.
    // Code from: MARC Code List for Languages (https://www.loc.gov/marc/languages/).
    // Choice of a MARC code is based on the predominant language of the item.
    // Three fill characters (|||) may also be used if no attempt is made to code the language or if non-MARC language coding is preferred (and coded in field 041 (Language code)).
    String lang = data.substring(35, 38);
    return lang;
  }
  
  public String getDCPublicationPlace() {
    List<DataField> dataFields = record.getDataFields();
    for (DataField dataField : dataFields) {
      if ("260".equals(dataField.getTag())) {
        List<Subfield> subfields = dataField.getSubfields("a"); // $a - Place of publication, distribution, etc. (R)
        return concatenate(subfields, " ");
      }
    }
    return null;
  }

  /*
  <xsl:for-each select="marc:datafield[@tag=260]">
    <dc:publisher>
      <xsl:call-template name="subfieldSelect">
        <xsl:with-param name="codes">ab</xsl:with-param>
      </xsl:call-template>
    </dc:publisher>
  </xsl:for-each>
   */
  public String getDCPublisher() {
    List<DataField> dataFields = record.getDataFields();
    for (DataField dataField : dataFields) {
      if ("260".equals(dataField.getTag())) {
        List<Subfield> subfields = dataField.getSubfields("b"); // $b - Name of publisher, distributor, etc. (R)
        return concatenate(subfields, " ");
      }
    }
    return null;
  }

  /*
   * <xsl:for-each select="marc:datafield[@tag=245]">
   *   <dc:title>
   *     <xsl:call-template name="subfieldSelect">
   *       <xsl:with-param name="codes">abfghk</xsl:with-param>
   *     </xsl:call-template>
   *   </dc:title>
   * </xsl:for-each>
   */
  public String getDCTitle() {
    List<DataField> dataFields = record.getDataFields();
    for (DataField dataField : dataFields) {
      if ("245".equals(dataField.getTag())) {
        List<Subfield> subfields = dataField.getSubfields("abfghk");
        return concatenate(subfields, " ");
      }
    }
    return null;
  }

  // https://www.loc.gov/marc/bibliographic/bd250.html
  // Edition statement that usually consists of numeric and alphabetic characters and accompanying words and/or abbreviations.
  public String getEditionStatement() {
    List<DataField> dataFields = record.getDataFields();
    for (DataField dataField : dataFields) {
      if ("250".equals(dataField.getTag())) {
        List<Subfield> subfields = dataField.getSubfields("ab");
        return concatenate(subfields, " ");
      }
    }
    return null;
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

  public DublinCore toDublinCore() {
    MarcXml2DublinCore marcXml2DublinCore = new MarcXml2DublinCore(this);
    return marcXml2DublinCore.convert();
  }
}
