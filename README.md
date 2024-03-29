# MARC-XML Java Library

A Java library for getting easily metadata from a MARC21-XML source without knowing the insides of [MARCXML standard](https://www.loc.gov/standards/marcxml/).

## Usage

1. Create an instance of `MarcXml`:

* from an input stream

```
InputStream is = ...;
Record record = MarcXmlRecord.from(is);
MarcXml marcXml = new MarcXml(record);
```

* from an URI:

```
URI uri = ...;
Record record = MarcXmlRecord.from(uri);
MarcXml marcXml = new MarcXml(record);
```

2. Read metadata of interest:

TODO

3. Convert to other standard object (e.g. DublinCore) and get metadata:

```
DublinCore dc = marcXml.toDublinCore();

List<String> dcCreators = dc.getCreators();
List<String> dcDates = dc.getDates();
String dcLanguage = dc.getLanguage();
List<String> dcPublishers = dc.getPublishers();
```

## Documentation

* [MARC 21 Formats](https://www.loc.gov/marc/marcdocz.html)
* [MARC 21 Format for Bibliographic Data](https://www.loc.gov/marc/bibliographic/)
* Documentation for Dublin Core see [DCMI Metadata Terms](https://www.dublincore.org/specifications/dublin-core/dcmi-terms/)
* Extracting Dublin Core fields following the XSL-Transformation [MARCXML to OAI Encoded Simple Dublin Core Stylesheet](https://www.loc.gov/standards/marcxml/xslt/MARC21slim2OAIDC.xsl) and <http://www.loc.gov/standards/marcxml/xslt/MARC21slimUtils.xsl>

## Tests

Tests are based

* on the example Marc21-XML (from <https://www.loc.gov/standards/marcxml/Sandburg/sandburg.xml>):

```
"Arithmetic", Autor: Carl Sandburg; Ted Rand, Verlag: San Diego : Harcourt Brace Jovanovich, 1993
```

* The Dublin Core representation of this book (<https://www.loc.gov/standards/marcxml/Sandburg/sandburgdc.xml>)
* The XSLT-transformation output `marc21-sandburg.dc.xml` (applied `MARC21slim2OAIDC.xsl` on `marc21-sandburg.xml`)
