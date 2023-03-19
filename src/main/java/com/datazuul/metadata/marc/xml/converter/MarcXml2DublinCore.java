package com.datazuul.metadata.marc.xml.converter;

import java.util.ArrayList;
import java.util.List;

import org.marc4j.marc.DataField;
import org.marc4j.marc.Subfield;

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
	dc.setTitles(parseTitles());
	dc.setType(parseType());

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
   * Meeting or conference name used as a main entry in a bibliographic
   * record.<br>
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
   * Added entry in which the name is not controlled in an authority file or list.
   * It is also used for names that have not been formulated according to
   * cataloging rules. Names may be of any type (e.g., personal, corporate,
   * meeting).<br>
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
   * </pre>
   */
  private List<String> parseCreators() {
	List<String> result = null;
	List<String> tags = List.of("100", "110", "111", "700", "710", "711", "720");
	for (String tag : tags) {
	  List<String> specifiedTagsContent = marcXml.getSubfieldsByTagAndCodes(tag, "abcdefghijklmnopqrstuvwxyz");
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
	List<String> result = marcXml.getSubfieldsByTagAndCodes("260", "c");

	if (result == null || result.isEmpty()) {
	  result = marcXml.getSubfieldsByTagAndCodes("264", "c");
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
   * <p>
   * 260ab: https://www.loc.gov/marc/bibliographic/concise/bd260.html
   * 
   * <p>
   * $a - Place of publication, distribution, etc. (R) May contain the
   * abbreviation [S.l.] when the place is unknown.
   * 
   * <p>
   * $b - Name of publisher, distributor, etc. (R) May contain the abbreviation
   * [s.n.] when the name is unknown.
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
	List<String> publishers = marcXml.getSubfieldsByTagAndCodes("260", "ab");
	return publishers;
  }

  /**
   * <p>
   * 245: https://www.loc.gov/marc/bibliographic/concise/bd245.html<br>
   * Title and statement of responsibility area of the bibliographic description
   * of a work.<br>
   * For more specific indicators see above linked documentation
   * 
   * <pre>
   * <xsl:for-each select="marc:datafield[@tag=245]">
   *   <dc:title>
   *     <xsl:call-template name="subfieldSelect">
   *       <xsl:with-param name="codes">abfghk</xsl:with-param>
   *     </xsl:call-template>
   *   </dc:title>
   * </xsl:for-each>
   * </pre>
   */
  private List<String> parseTitles() {
	List<String> result = marcXml.getSubfieldsByTagAndCodes("245", "abfghk");
	return result;
  }

  /**
   * <p>
   * leader: https://www.loc.gov/marc/bibliographic/concise/bdleader.html<br>
   * Fixed field that comprises the first 24 character positions (00-23) of each
   * bibliographic record and consists of data elements that contain numbers or
   * coded values that define the parameters for the processing of the record.
   * 
   * <p>
   * leader6 (position 7):
   * https://www.loc.gov/marc/bibliographic/concise/bdleader.html Type of record.
   * One-character alphabetic code used to define the characteristics and
   * components of the record. <br>
   * For more specific indicators see above linked documentation
   * 
   * <p>
   * leader7 (position 8):
   * https://www.loc.gov/marc/bibliographic/concise/bdleader.html Bibliographic
   * level.One-character alphabetic code indicating the bibliographic level of the
   * record. <br>
   * For more specific indicators see above linked documentation
   * 
   * <p>
   * 655: https://www.loc.gov/marc/bibliographic/concise/bd655.html<br>
   * Terms indicating the genre, form, and/or physical characteristics of the
   * materials being described. A genre term designates the style or technique
   * of the intellectual content of textual materials or, for graphic materials,
   * aspects such as vantage point, intended purpose, characteristics of the creator,
   * publication status, or method of representation. A form term designates
   * historically and functionally specific kinds of materials distinguished by
   * their physical character, the subject of their intellectual content, or the
   * order of information within them. Physical characteristic terms designate
   * historically and functionally specific kinds of materials as distinguished by
   * an examination of their physical character, subject of their intellectual
   * content, or the order of information with them.<br>
   * For more specific indicators see above linked documentation
   * 
   * <pre>
   * <dc:type>
   *   <xsl:if test="$leader7='c'">
   *     <!--Remove attribute 6/04 jer-->
   *     <!--<xsl:attribute name="collection">yes</xsl:attribute>-->
   *     <xsl:text>collection</xsl:text>
   *   </xsl:if>
   *   <xsl:if test=
  "$leader6='d' or $leader6='f' or $leader6='p' or $leader6='t'">
   *     <!--Remove attribute 6/04 jer-->
   *     <!--<xsl:attribute name="manuscript">yes</xsl:attribute>-->
   *     <xsl:text>manuscript</xsl:text>
   *   </xsl:if>
   *   <xsl:choose>
   *     <xsl:when test="$leader6='a' or $leader6='t'">text</xsl:when>
   *     <xsl:when test="$leader6='e' or $leader6='f'">cartographic</xsl:when>
   *     <xsl:when test="$leader6='c' or $leader6='d'">notated music</xsl:when>
   *     <xsl:when test="$leader6='i' or $leader6='j'">sound recording</xsl:when>
   *     <xsl:when test="$leader6='k'">still image</xsl:when>
   *     <xsl:when test="$leader6='g'">moving image</xsl:when>
   *     <xsl:when test="$leader6='r'">three dimensional object</xsl:when>
   *     <xsl:when test="$leader6='m'">software, multimedia</xsl:when>
   *     <xsl:when test="$leader6='p'">mixed material</xsl:when>
   *   </xsl:choose>
   * </dc:type>
   * <xsl:for-each select="marc:datafield[@tag=655]">
   *   <dc:type>
   *     <xsl:value-of select="."/>
   *   </dc:type>
   * </xsl:for-each>
   * </pre>
   */
  private String parseType() {
	String result = "";
	char leader6 = marcXml.getTypeOfRecord();
	char leader7 = marcXml.getLeader(7);

	if (leader7 == 'c') {
	  result += "collection";
	}
	if (leader6 == 'd' || leader6 == 'f' || leader6 == 'p' || leader6 == 't') {
	  result += "manuscript";
	}
	switch (leader6) {
	case 'a', 't' -> result += "text";
	case 'e', 'f' -> result += "cartographic";
	case 'c', 'd' -> result += "notated music";
	case 'i', 'j' -> result += "sound recording";
	case 'k' -> result += "still image";
	case 'g' -> result += "moving image";
	case 'r' -> result += "three dimensional object";
	case 'm' -> result += "software, multimedia";
	case 'p' -> result += "mixed material";
	}
	List<String> list655 = marcXml.getSubfieldsByTagAndCodes("655", "abcvxyz");
	if (list655 != null && !list655.isEmpty()) {
		result += String.join(" ", list655);
	}
	return result;
  }
}
