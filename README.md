# GeoIP4J [![Build Status](https://travis-ci.org/quarantyne/geoip4j.svg?branch=master)](https://travis-ci.org/quarantyne/geoip4j)
The great royalty-free GeoLite2 database bundled in a convenient, zero-deps Java 8 library.
Only IPv4 to Country lookups are supported forthe time being.

## Get it
### Gradle
Step 1. Add the JitPack repository to your build file 
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Step 2. Add the dependency
```
dependencies {
  implementation 'com.github.quarantyne:geoip4j:1.0.4'
}
```

### Maven
Step 1. Add the JitPack repository to your build file
```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Step 2. Add the dependency
```xml
<dependency>
    <groupId>com.github.quarantyne</groupId>
    <artifactId>geoip4j</artifactId>
    <version>1.0.4</version>
</dependency>
```

## Use it
```java
GeoIp4j geoIp4j = new GeoIp4jImpl();
Optional<GeoName> geoName = geoIp4j.getGeoName("87.247.19.126");
// Optional[GeoName{isoCode='KZ', englishName='Kazakhstan'}]
geoIp4j.getLongName("fr");
// <Optional[France]> , in case you stored iso code and need full name
```

## Implementation notes
 - The approximate ~320K CIDR blocks are held in memory and sharded by first quad to reduce lookup time. 
 - The index used is a `HashMap<String, List<GeoName>>` (known as `MultiMap`).

## License
[Apache 2](https://github.com/quarantyne/geoip4j/blob/master/LICENSE)
