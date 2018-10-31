package com.quarantyne.geoip4j;

import java.util.Optional;

public interface GeoIp4j {
  Optional<GeoName> getGeoName(String ip);
  Optional<String> getLongName(String isoCode);
}
