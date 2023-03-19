package com.datazuul.metadata.marc.xml.converter;

import java.util.ArrayList;
import java.util.List;

import org.marc4j.marc.DataField;

import com.datazuul.metadata.dublincore.DublinCore;
import com.datazuul.metadata.marc.xml.MarcXml;

public class MarcXml2DublinCore implements MarcXmlConverter<DublinCore> {
  private MarcXml marcXml;

  public MarcXml2DublinCore(MarcXml marcXml) {
	this.marcXml = marcXml;
  }

  @Override
  public DublinCore convert() {
	DublinCore dc = new DublinCore();

	dc.setCreators(parseCreators());
	dc.setDates(parseDates());
	dc.setLanguage(parseLanguage());
	dc.setPublishers(parsePublishers());

	return dc;
  }

  /**
   * <p>
   * 100: https://www.loc.gov/marc/bibliographic/concise/bd100.html<br>
   * Personal name used as a main entry in a bibliographic record.<br>
   * For more specific indicators see above linked documentation
   * 
   * <p>
   * 110: https://www.loc.gov/marc/bibliographic/concise/bd110.html<br>
   * Corporate name used as a main entry in a bibliographic record.<br>
   * For more specific indicators see above linked documentation
   *  
   * <p>
   * 111: https://www.loc.gov/marc/bibliographic/concise/bd111.html<br>
   * Meeting or conference name used as a main entry in a bibliographic record.<br>
   * For more specific indicators see above linked documentation
   * 
   * <p>
   * 700: https://www.loc.gov/marc/bibliographic/concise/bd700.html<br>
   * Added entry in which the entry element is a personal name.<br>
   * For more specific indicators see above linked documentation
   * 
   * <p>
   * 710: https://www.loc.gov/marc/bibliographic/concise/bd710.html<br>
   * Added entry in which the entry element is a corporate name.<br>
   * For more specific indicators see above linked documentation
   * 
   * <p>
   * 711: https://www.loc.gov/marc/bibliographic/concise/bd711.html<br>
   * Added entry in which the entry element is a meeting name.<br>
   * For more specific indicators see above linked documentation
   * 
   * <p>
   * 720: https://www.loc.gov/marc/bibliographic/concise/bd720.html<br>
   * Added entry in which the name is not controlled in an authority file
   * or list. It is also used for names that have not been formulated
   * according to cataloging rules. Names may be of any type (e.g.,
   * personal, corporate, meeting).<br>
   * For more specific indicators see above linked documentation
   * 
   * <pre>
   * <xsl:for-each select="marc:datafield[@tag=100]|marc:datafield[@tag=110]
   * |marc:datafield[@tag=111]|marc:datafield[@tag=700]|marc:datafield[@tag=710]
   * |marc:datafield[@tag=711]|marc:datafield[@tag=720]">
   *   <dc:creator>
   *     <xsl:value-of select="."/>
   *   </dc:creator>
   * </xsl:for-each>
    </pre>
   */
  private List<String> parseCreators() {
	List<String> result = null;
	List<String> tags = List.of("100", "110", "111", "700", "710", "711", "720");
	for (String tag : tags) {
	  List<String> specifiedTagsContent = marcXml.getSubfieldsByTagAndCode(tag, "abcdefghijklmnopqrstuvwxyz");
	  if (specifiedTagsContent != null) {
		if (result == null) {
		  result = new ArrayList<>();
		}
		result.addAll(specifiedTagsContent);
	  }
	}
	return result;
  }

  /**
   * <p>
   * 260c: https://www.loc.gov/marc/bibliographic/concise/bd260.html<br>
   * Date of publication, distribution, etc. (R).<br>
   * May contain multiple dates (e.g., dates of publication and copyright).
   * 
   * <p>
   * 264c: https://www.loc.gov/marc/bibliographic/concise/bd264.html<br>
   * Date of production, publication, distribution, manufacture, or copyright
   * notice (R)
   * 
   * <p>
   * Comment: "Information in field 260 is similar to information in field 264
   * (Production, Publication, Distribution, Manufacture, and Copyright Notice).
   * Field 260 is useful for cases where the content standard or institutional
   * policies used do not make a distinction between functions"
   * 
   * The 264 essentially replaced the 260 when RDA was implemented. You can read
   * the PCC Guidelines.
   * 
   * I wouldn't change the fields. For the most part, AACR2 records and RDA
   * records should be able to co-exist in your ILS/discovery system.
   * 
   * Regarding copy cataloging or managing old records: I usually try not to
   * overwrite/delete data in my records, so if I have a 260 line but no 264, I'll
   * add the 264 for RDA compliance and leave the 260 for consistency with the
   * pre-RDA record. If I have a 264 and no 260, then I simply leave it as is
   * because that's covering the more up to date requirement.
   * 
   * Regarding original records: I use 264 only and follow RDA compliance."
   */
  private List<String> parseDates() {
	List<String> result = marcXml.getSubfieldsByTagAndCode("260", "c");

	if (result == null || result.isEmpty()) {
	  result = marcXml.getSubfieldsByTagAndCode("264", "c");
	}
	return result;
  }

  /**
   * <p>
   * 008 all: https://www.loc.gov/marc/bibliographic/concise/bd008a.html
   * 
   * <p>
   * 35-37 - Language<br>
   * Three-character alphabetic code that indicates the language of the item. Code
   * from: MARC Code List for Languages. Choice of a MARC code is based on the
   * predominant language of the item. Three fill characters (|||) may also be
   * used if no attempt is made to code the language or if only non-MARC language
   * coding is preferred (and coded in field 041 (Language code)).
   */
  private String parseLanguage() {
	String data = marcXml.getControlFieldByTag("008");
	String lang = data.substring(35, 38);
	return lang;
  }

  /**
   * <p>260ab: https://www.loc.gov/marc/bibliographic/concise/bd260.html
   * 
   * <p>$a - Place of publication, distribution, etc. (R)
   *  May contain the abbreviation [S.l.] when the place is unknown.
   * 
   * <p>$b - Name of publisher, distributor, etc. (R)
   *  May contain the abbreviation [s.n.] when the name is unknown.
   *  
   * <pre>
   * <xsl:for-each select="marc:datafield[@tag=260]">
   *   <dc:publisher>
   *     <xsl:call-template name="subfieldSelect">
   *       <xsl:with-param name="codes">ab</xsl:with-param>
   *     </xsl:call-template>
   *   </dc:publisher>
   * </xsl:for-each>
   * </pre>
   */
  private List<String> parsePublishers() {
	List<String> publishers = null;
	List<DataField> dataFields = marcXml.getDataFieldsByTag("260");
	for (DataField dataField : dataFields) {
	  if (publishers == null) {
		publishers = new ArrayList<>();
	  }
	  String subfields = dataField.getSubfieldsAsString("ab"); // $b - Name of publisher, distributor, etc. (R)
	  publishers.add(subfields);
	}
	return publishers;
  }
}
