# MARC-XML Java Library

A Java library for getting easily metadata from a MARC21-XML source without knowing the insides of [MARCXML standard](https://www.loc.gov/standards/marcxml/).

## Usage

1. Create an instance of `MarcXml`:

### from an input stream

```
InputStream is = ...;
Record record = MarcXmlRecord.from(is);
MarcXml marcXml = new MarcXml(record);
```

### from an URI:

```
URI uri = ...;
Record record = MarcXmlRecord.from(uri);
MarcXml marcXml = new MarcXml(record);
String dcDate = marcXml.getDCDate();
```

2. Read metadata of interest (e.g. DCDate):

```
String dcDate = marcXml.getDCDate();
```

## Documentation

* [MARC 21 Format for Bibliographic Data](https://www.loc.gov/marc/bibliographic/)
* Extracting Dublin Core fields following the XSL-Transformation [MARCXML to OAI Encoded Simple Dublin Core Stylesheet](https://www.loc.gov/standards/marcxml/xslt/MARC21slim2OAIDC.xsl)

## Tests

Tests are based

* on the example Marc21-XML (from <https://www.loc.gov/standards/marcxml/Sandburg/sandburg.xml>):

```
"Arithmetic", Autor: Carl Sandburg; Ted Rand, Verlag: San Diego : Harcourt Brace Jovanovich, 1993
``` 

* The Dublin Core representation of this book (<https://www.loc.gov/standards/marcxml/Sandburg/sandburgdc.xml>)
