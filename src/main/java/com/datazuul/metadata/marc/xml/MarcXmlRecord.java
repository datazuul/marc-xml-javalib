package com.datazuul.metadata.marc.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.marc4j.MarcReader;
import org.marc4j.MarcXmlReader;
import org.marc4j.marc.Record;

public class MarcXmlRecord {

  public static Record from(URI uri) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    String xml = response.body();
    InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
    return from(inputStream);
  }

  public static Record from(InputStream is) {
    MarcReader reader = new MarcXmlReader(is);
    Record record = reader.next();
    return record;
  }
}
