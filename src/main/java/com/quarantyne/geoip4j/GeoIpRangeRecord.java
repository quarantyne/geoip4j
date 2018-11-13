package com.quarantyne.geoip4j;

import java.util.Objects;

final class GeoIpRangeRecord {
  private final String countryCode;
  private final String fullName;
  private final Long ipRangeStart;
  private final Integer cidrBlockSize;
  private final String firstQuad;

  public GeoIpRangeRecord(
      String countryCode,
      String fullName,
      Long ipRangeStart,
      Integer cidrBlockSize,
      String firstQuad) {
    this.countryCode = countryCode;
    this.fullName = fullName;
    this.ipRangeStart = ipRangeStart;
    this.cidrBlockSize = cidrBlockSize;
    this.firstQuad = firstQuad;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public String getFullName() {
    return fullName;
  }

  public Long getIpRangeStart() {
    return ipRangeStart;
  }

  public Integer getCidrBlockSize() {
    return cidrBlockSize;
  }

  public boolean includes(Long ip) {
    return ipRangeStart <= ip && ip < (ipRangeStart + cidrBlockSize);
  }

  public String getFirstQuad() {
    return firstQuad;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoIpRangeRecord that = (GeoIpRangeRecord) o;
    return Objects.equals(getIpRangeStart(), that.getIpRangeStart())
        && Objects.equals(getCidrBlockSize(), that.getCidrBlockSize());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getIpRangeStart(), getCidrBlockSize());
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("GeoIpRangeRecord{");
    sb.append("countryCode='").append(countryCode).append('\'');
    sb.append(", fullName='").append(fullName).append('\'');
    sb.append(", ipRangeStart=").append(ipRangeStart);
    sb.append(", cidrBlockSize=").append(cidrBlockSize);
    sb.append('}');
    return sb.toString();
  }
}
