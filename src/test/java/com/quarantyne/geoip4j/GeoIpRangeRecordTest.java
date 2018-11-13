package com.quarantyne.geoip4j;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class GeoIpRangeRecordTest {
  @Test
  public void testInclude() {
    GeoIpRangeRecord record = new GeoIpRangeRecord(
        "cn",
        "china",
        GeoIp4jImpl.ipToLong("1.1.8.0"),
        GeoIp4jImpl.getCidrBlockSize(21),
        "1");

    assertThat(record.includes(GeoIp4jImpl.ipToLong("1.1.7.255"))).isFalse();
    assertThat(record.includes(GeoIp4jImpl.ipToLong("1.1.8.0"))).isTrue(); // first ip
    assertThat(record.includes(GeoIp4jImpl.ipToLong("1.1.8.10"))).isTrue();
    assertThat(record.includes(GeoIp4jImpl.ipToLong("1.1.12.255"))).isTrue();
    assertThat(record.includes(GeoIp4jImpl.ipToLong("1.1.15.255"))).isTrue(); // last ip
    assertThat(record.includes(GeoIp4jImpl.ipToLong("1.1.16.0"))).isFalse(); // last ip
  }
}
