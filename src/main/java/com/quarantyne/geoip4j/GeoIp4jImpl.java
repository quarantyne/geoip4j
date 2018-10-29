package com.quarantyne.geoip4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class GeoIp4jImpl implements GeoIp4j {
  private Map<String, List<GeoIpRangeRecord>> map;
  private static String CSV_SEP = ",";
  private static String IP_SEP = "\\.";
  private static String CIDR_SEP = "/";

  public GeoIp4jImpl()  {
    this.map = new HashMap<>();
    Path file = Paths.get(getClass().getResource("/geoip.csv").getFile());
    GeoIpRangeRecord record;
    List<GeoIpRangeRecord> geoIpRangeRecordList;

    try (BufferedReader reader = Files.newBufferedReader(file)) {
      String line;
      while ((line = reader.readLine()) != null) {
        record = parse(line);
        if (map.containsKey(record.getFirstQuad())) {
          geoIpRangeRecordList = map.get(record.getFirstQuad());
          geoIpRangeRecordList.add(record);
        } else {
          geoIpRangeRecordList = new ArrayList<>();
          geoIpRangeRecordList.add(record);
          map.put(record.getFirstQuad(), geoIpRangeRecordList);
        }
      }
    } catch (IOException ioex) {
      ioex.printStackTrace();
    }
  }

  @Override
  public Optional<GeoName> getGeoName(String ip) {
    if (!isValidIp(ip)) {
      return Optional.empty();
    }
    String key = ip.split("\\.")[0];
    List<GeoIpRangeRecord> shard = map.get(key);
    Optional<GeoName> result = Optional.empty();
    for (GeoIpRangeRecord record: shard) {
      if (record.includes(ipToLong(ip))) {
        result = Optional.of(new GeoName(record.getCountryCode(), record.getFullName()));
        break;
      }
    }
    return result;
  }

  boolean isValidIp(String ip) {
    if (null == ip || ip.isEmpty()) {
      return false;
    }
    if (ip.length() < 7 || ip.length() > 15) {
      return false;
    }
    int dotCount = 0;
    for(char c: ip.toCharArray()) {
      if (c == '.') {
        dotCount += 1;
      }
    }
    return dotCount == 3;
  }

  GeoIpRangeRecord parse(String str) {
    String[] cols = str.split(CSV_SEP);
    String[] ipChunks = cols[0].split(CIDR_SEP);
    String[] quads = ipChunks[0].split(IP_SEP);

    int cidrBlockSize = getCidrBlockSize(Integer.parseInt(ipChunks[1]));

    return new GeoIpRangeRecord(cols[1], cols[2], ipToLong(quads), cidrBlockSize, quads[0]);
  }

  long ipToLong(String[] ipAddressInArray) {
    long result = 0;
    for (int i = 3; i >= 0; i--) {
      long ip = Long.parseLong(ipAddressInArray[3 - i]);
      result |= ip << (i * 8);
    }
    return result;
  }

  long ipToLong(String ipAddress) {
    return ipToLong(ipAddress.trim().split("\\."));
  }

  int getCidrBlockSize(int cidr) {
    if (0 < cidr  && cidr <= 32) {
      return (int) Math.pow(2, 32 - cidr);
    } else {
      return -1;
    }
  }
}
