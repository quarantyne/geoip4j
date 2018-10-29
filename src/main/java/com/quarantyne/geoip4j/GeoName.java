package com.quarantyne.geoip4j;

import java.util.Objects;

public final class GeoName {
  private String isoCode;
  private String englishName;

  public GeoName(String isoCode, String englishName) {
    this.isoCode = isoCode.toUpperCase();
    this.englishName = englishName;
  }

  public String getIsoCode() {
    return isoCode;
  }

  public String getEnglishName() {
    return englishName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoName geoName = (GeoName) o;
    return Objects.equals(getIsoCode(), geoName.getIsoCode());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getIsoCode());
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("GeoName{");
    sb.append("isoCode='").append(isoCode).append('\'');
    sb.append(", englishName='").append(englishName).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
