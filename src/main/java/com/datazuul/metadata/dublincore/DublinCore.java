package com.datazuul.metadata.dublincore;

import java.util.List;

/**
 * <p>This class represents Dublin Core metadata.
 * 
 * <p>Comment: "Each Dublin Core element is optional and repeatable, and there
 *  is no defined order of elements. The ordering of multiple occurrences of the
 *  same element (e.g., Creator) may have a significance intended by the provider,
 *  but ordering is not guaranteed to be preserved in every user environment.
 *  Ordering or sequencing may be syntax dependent; for instance, RDF/XML supports
 *  ordering, but HTML does not."
 * 
 * @see <a href="http://dublincore.org/specifications/dublin-core/dcmi-terms/2020-01-20/">http://dublincore.org/specifications/dublin-core/dcmi-terms/2020-01-20/ </a>
 * @see <a href="https://www.dublincore.org/resources/userguide/creating_metadata/">https://www.dublincore.org/resources/userguide/creating_metadata/</a>
 * @see <a href="https://www.dublincore.org/specifications/dublin-core/usageguide/">https://www.dublincore.org/specifications/dublin-core/usageguide/</a>
 */
public class DublinCore {

  /**
   * <p><b>Definition:</b> An entity responsible for making the resource.
   * 
   * <p><b>Comment:</b> Recommended practice is to identify the creator with a URI.
   *  If this is not possible or feasible, a literal value that identifies
   *  the creator may be provided.
   * 
   * @see <a href="http://purl.org/dc/terms/creator">http://purl.org/dc/terms/creator</a>
   */
  private List<String> creators;

  /**
   * <p><b>Definition:</b> A point or period of time associated with an event
   *  in the lifecycle of the resource.
   * 
   * <p><b>Comment:</b> Date may be used to express temporal information at any
   *  level of granularity. Recommended practice is to express the date,
   *  date/time, or period of time according to ISO 8601-1 [ISO 8601-1]
   *  or a published profile of the ISO standard, such as the W3C Note on
   *  Date and Time Formats [W3CDTF] or the Extended Date/Time Format
   *  Specification [EDTF]. If the full date is unknown, month and year
   *  (YYYY-MM) or just year (YYYY) may be used.
   *  Date ranges may be specified using ISO 8601 period of time specification
   *  in which start and end dates are separated by a '/' (slash) character.
   *  Either the start or end date may be missing.
   * 
   * @see <a href="http://purl.org/dc/terms/date">http://purl.org/dc/terms/date</a>
   */
  private List<String> dates;
  
  
  /**
   * <p><b>Definition:</b> An account of the resource.
   * 
   * <p><b>Comment:</b> Description may include but is not limited to: an abstract,
   *  a table of contents, a graphical representation, or a free-text account of the resource.
   * 
   * @see <a href="http://purl.org/dc/terms/description">http://purl.org/dc/terms/description</a>
   */
  private List<String> descriptions;
  
  /**
   * <p><b>Definition:</b> A language of the resource.
   * 
   * <p><b>Comment:</b> Recommended practice is to use either a non-literal value
   *  representing a language from a controlled vocabulary such as ISO 639-2 or ISO 639-3,
   *  or a literal value consisting of an IETF Best Current Practice 47 [IETF-BCP47] language tag.
   *  
   * @see <a href="http://purl.org/dc/terms/language">http://purl.org/dc/terms/language</a>
   */
  private String language;
  
  /**
   * <p><b>Definition:</b> An entity responsible for making the resource available.
   * 
   * @see <a href="http://purl.org/dc/terms/publisher">http://purl.org/dc/terms/publisher</a>
   * @see <a href="http://purl.org/dc/terms/Agent">http://purl.org/dc/terms/Agent</a>
   */
  private List<String> publishers;
  
  /**
   * <p><b>Definition:</b> A topic of the resource.
   * 
   * <p><b>Comment:</b> Recommended practice is to refer to the subject with a URI.
   *  If this is not possible or feasible, a literal value that identifies the subject may
   *  be provided. Both should preferably refer to a subject in a controlled vocabulary.
   * 
   * @see <a href="http://purl.org/dc/terms/subject">http://purl.org/dc/terms/subject</a>
   */
  private List<String> subjects;
  
  /**
   * <p><b>Definition:</b> A name given to the resource.
   * 
   * @see <a href="http://purl.org/dc/terms/title">http://purl.org/dc/terms/title</a>
   */
  private String title;
  
  /**
   * <p><b>Definition:</b> The nature or genre of the resource.
   * 
   * <p><b>Comment:</b> Recommended practice is to use a controlled vocabulary such
   *  as the DCMI Type Vocabulary [DCMI-TYPE]. To describe the file format, physical
   *  medium, or dimensions of the resource, use the property Format.
   * 
   * @see <a href="http://purl.org/dc/terms/type">http://purl.org/dc/terms/type</a>
   */
  private String type;
  
  public DublinCore() {

  }

  public List<String> getCreators() {
    return creators;
  }

  public List<String> getDates() {
    return dates;
  }

  public List<String> getDescriptions() {
    return descriptions;
  }

  public String getLanguage() {
    return language;
  }

  public List<String> getPublishers() {
    return publishers;
  }

  public List<String> getSubjects() {
    return subjects;
  }

  public String getTitle() {
    return title;
  }

  public String getType() {
    return type;
  }

  public void setCreators(List<String> creators) {
    this.creators = creators;
  }

  public void setDates(List<String> dates) {
    this.dates = dates;
  }

  public void setDescriptions(List<String> descriptions) {
    this.descriptions = descriptions;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setPublishers(List<String> publishers) {
    this.publishers = publishers;
  }

  public void setSubjects(List<String> subjects) {
    this.subjects = subjects;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setType(String type) {
    this.type = type;
  }
}
