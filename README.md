# geoip4j
Royalty-free GeoLite2 database bundled in a convenient java library.

## Get it
GeoIp4j is published as a maven artifact to Jitpack.io. Install instructions are available at https://jitpack.io/#quarantyne/geoip4j/1.0.0

## Use it
```java
GeoIp4j geoIp4j = new GeoIp4jImpl()
assertThat(geoIp4j.getGeoName("87.247.19.126").get().getIsoCode()).isEqualTo("KZ");
```

## License
Apache 2 (c) Edouard Swiac
