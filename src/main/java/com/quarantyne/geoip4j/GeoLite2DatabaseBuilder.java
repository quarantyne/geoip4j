package com.quarantyne.geoip4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Internal class used to regenrate geoip.csv from MaxMind's DB
 */
final class GeoLite2DatabaseBuilder {
  static class IpBlock {
    String ipBlock;
    GeoName geoName;

    public IpBlock(String ipBlock, GeoName geoName) {
      this.ipBlock = ipBlock;
      this.geoName = geoName;
    }

    public String toCsv() {
      return String.format("%s,%s,%s", ipBlock, geoName.getIsoCode(), geoName.getEnglishName());
    }

    @Override
    public String toString() {
      final StringBuffer sb = new StringBuffer("IpBlock{");
      sb.append("ipBlock='").append(ipBlock).append('\'');
      sb.append(", geoName=").append(geoName);
      sb.append('}');
      return sb.toString();
    }
  }
  private static Path IP_BLOCKS_FILE = Paths.get("GeoLite2-Country-Blocks-IPv4.csv");
  private static Path COUNTRY_LOCATIONS_FILE = Paths.get("GeoLite2-Country-Locations-en.csv");
  private static Path FILE_OUTPUT = Paths.get("src/main/resources/geoip.csv");

  private static Pattern COMMA = Pattern.compile(",");
  private static String LF = "\n";

  public static void main(String...args) throws IOException {
    String geoLiteDir = "/Users/eswiac/Downloads/GeoLite2-Country-CSV_20181016";
    if (args.length == 2) {
      geoLiteDir = args[1];
    }
    Path geoLitePath = Paths.get(geoLiteDir).toAbsolutePath();
    Map<String, GeoName> geoNameMap = makeGeoNameIndex(geoLitePath);
    List<IpBlock> ipBlocks = readIpBlocks(geoLitePath, geoNameMap);
  }

  static List<IpBlock> readIpBlocks(Path geoLitePath, Map<String, GeoName> geoNameMap) throws IOException {
    int ipBlockIdx = 0;
    int geoNameId = 1;
    List<IpBlock> ipBlocks = new ArrayList<>();
    FILE_OUTPUT.toFile().createNewFile();
    BufferedWriter writer = Files.newBufferedWriter(FILE_OUTPUT);
    try (BufferedReader reader = Files.newBufferedReader(geoLitePath.resolve(IP_BLOCKS_FILE))) {
      String line;
      String[] cols;
      reader.readLine(); // skip header
      GeoName geoName;
      while ((line = reader.readLine()) != null) {
        cols = COMMA.split(line);
        geoName = geoNameMap.get(cols[geoNameId]);
        if (geoName != null) {
          writer.write(new IpBlock(cols[ipBlockIdx], geoName).toCsv());
          writer.write(LF);
        }
      }
    }
    writer.close();
    return ipBlocks;
  }

  static Map<String, GeoName> makeGeoNameIndex(Path geoLitePath) throws IOException {
    int idIdx = 0;
    int isoCodeIdx = 4;
    int nameIdx = 5;
    Map<String, GeoName> geoNameIndex = new HashMap<>();
    try (BufferedReader reader = Files.newBufferedReader(geoLitePath.resolve(COUNTRY_LOCATIONS_FILE))) {
      String line;
      String[] cols;
      reader.readLine(); // skip header
      while ((line = reader.readLine()) != null) {
        cols = COMMA.split(line);
        if (!cols[isoCodeIdx].isEmpty()) {
          geoNameIndex.put(cols[idIdx],  new GeoName(cols[isoCodeIdx].toLowerCase(), cols[nameIdx]));
        }
      }
    }
    return geoNameIndex;
  }

  static List<String[]> loadCsv(Path file) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(file)) {
      List<String[]> lines = new ArrayList<>();
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(COMMA.split(line));
        System.out.println(line);
      }
      return lines;
    }
  }
}
