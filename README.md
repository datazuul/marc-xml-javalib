# MARC-XML Java Library

A Java library for getting easily metadata from a MARC21-XML source without knowing the insides of MARC standard.

## Usage

Create an instance of `MarcXml`:

## from an input stream

```
InputStream is = ...;
Record record = MarcXmlRecord.from(is);
MarcXml marcXml = new MarcXml(record);
String dcDate = marcXml.getDCDate();
```

## from an URI:

```
URI uri = ...;
Record record = MarcXmlRecord.from(uri);
MarcXml marcXml = new MarcXml(record);
String dcDate = marcXml.getDCDate();
```

## Tests

Tests are based on the example Marc21-XML (from <https://www.loc.gov/standards/marcxml//Sandburg/sandburg.xml>):

```
"Arithmetic", Autor: Carl Sandburg; Ted Rand, Verlag: San Diego : Harcourt Brace Jovanovich, 1993
``` 
