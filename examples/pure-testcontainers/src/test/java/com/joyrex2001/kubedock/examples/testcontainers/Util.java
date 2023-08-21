package com.joyrex2001.kubedock.examples.testcontainers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static org.assertj.core.api.Assertions.assertThat;

final class Util {
  private Util() {
  }

  static String readFromUrl(URL url) throws IOException {
    String content;
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    try {
      int responseCode = connection.getResponseCode();
      assertThat(responseCode).isEqualTo(200);
      content = readContent(connection);
    } finally {
      connection.disconnect();
    }
    return content;
  }

  private static String readContent(URLConnection connection) throws IOException {
    try (BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream()))) {
      String inputLine;
      StringBuilder content = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        content.append(inputLine);
      }
      return content.toString();
    }
  }
}