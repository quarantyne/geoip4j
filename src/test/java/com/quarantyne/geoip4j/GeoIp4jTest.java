package com.quarantyne.geoip4j;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class GeoIp4jTest {
  GeoIp4jImpl geoIp4j = new GeoIp4jImpl();

  @Test
  public void testLookup() {
    assertThat(geoIp4j.getGeoName(null)).isEmpty();
    assertThat(geoIp4j.getGeoName("")).isEmpty();
    assertThat(geoIp4j.getGeoName("text")).isEmpty();
    assertThat(geoIp4j.getGeoName("87.247.19.126").get().getIsoCode()).isEqualTo("KZ");
    assertThat(geoIp4j.getGeoName("178.128.54.106").get().getIsoCode()).isEqualTo("SG");
    assertThat(geoIp4j.getGeoName("167.99.235.224").get().getIsoCode()).isEqualTo("US");
    assertThat(geoIp4j.getGeoName("34.251.180.230").get().getIsoCode()).isEqualTo("IE");
  }

  @Test
  public void testIsValidIp() {
    assertThat(geoIp4j.isValidIp(null)).isFalse();
    assertThat(geoIp4j.isValidIp("")).isFalse();
    assertThat(geoIp4j.isValidIp("text")).isFalse();
    assertThat(geoIp4j.isValidIp("1.2")).isFalse();
    assertThat(geoIp4j.isValidIp("1.2.3.4.5")).isFalse();
    assertThat(geoIp4j.isValidIp("0.0.0.0")).isTrue();
    assertThat(geoIp4j.isValidIp("255.255.255.255")).isTrue();
  }

  @Test
  public void testParser() {
    assertThat(geoIp4j.parse("1.2.3.4/5,fr,france"))
        .isEqualTo(new GeoIpRangeRecord("fr", "france", 16909060L, 134217728, "1"));
  }

  @Test
  public void testCodeToName() {
    assertThat(geoIp4j.getLongName("fr")).contains("France");
  }

  @Test
  public void testIpToLong() {
    assertThat(geoIp4j.ipToLong("202.166.205.242")).isEqualTo(3399929330L);
    assertThat(geoIp4j.ipToLong(new String[]{"46", "149", "35", "213"})).isEqualTo(781525973L);
  }

  @Test
  public void testCidrBlockSize() {
    assertThat(geoIp4j.getCidrBlockSize(4)).isEqualTo(268435456);
    assertThat(geoIp4j.getCidrBlockSize(17)).isEqualTo(32768);
  }
}
