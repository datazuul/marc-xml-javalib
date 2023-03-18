package com.datazuul.metadata.marc.xml.converter;

import com.datazuul.metadata.dublincore.DublinCore;
import com.datazuul.metadata.marc.xml.MarcXml;

public class MarcXml2DublinCore implements MarcXmlConverter<DublinCore> {
  private MarcXml marcXml;

  public MarcXml2DublinCore(MarcXml marcXml) {
    this.marcXml = marcXml;
  }

  @Override
  public DublinCore convert() {
    return null;
  }
}
