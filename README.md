# *What is FastProto?*

[![Build Status](https://travis-ci.com/indunet/fastproto.svg?branch=master)](https://travis-ci.com/indunet/fastproto)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

FastProto is was conceived and designed in 2018. The original intention was to solve practical problems encountered in work. To put it simply, in the field of IoT, binary messages are usually used to transmit data between devices and servers, instead of the commonly used JSON format in Web. The server usually needs to parse binary messages according to the specified protocol. If send data to device, it also needs to perform data packets according to the specified protocol. Although it is not challenging for most developers, the whole process is extremely boring and error-prone. If there is an easier way to convert binary messages into Java objects, or convert Java objects into binary messages, developers can focus more on the business implementation of the system. Based on the above ideas, FastProto was born.

# *Features*
* Serialization and deserialization
* Support all basic Java data types
* Support String type and Timestamp type
* User-defined endian

# *Developing*
* Unsigned types
* Auto type

# *Maven*
```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>1.2.3</version>
</dependency>
```

# *Quick Start*
Define a Java class with FastProto annotations like below:
```java
public class Metrics {
    @ShortType(0)
    short id;

    @TimestampType(2)
    Timestamp time;

    @IntegerType(10)
    int humidity;

    @FloatType(14)
    float temperature;

    @DoubleType(18)
    double pressure;
}
```
And then convert the object into datagram or convert datagram into the object like below:
```java
byte[] datagram = new byte[64];

Metrics metrics = FastProto.decode(datagram, Metrics.class);
FastProto.encode(metrics, datagram);
```

# *FastProto Annotations*
|Annotation|Java|C/C++|Size| 
|:----:|:----:|:----:|:----:|
|@BooleanType|Boolean / boolean|bool|1 Bit|
|@CharacterType|Character / char|--|2 Bytes|
|@ByteType|Byte / byte|char|1 Byte|
|@ShortType|Short / short|short|2 Bytes|
|@IntegerType|Integer / int|long|4 Bytes|
|@LongType|Long / long|long long|8 Bytes|
|@FloatType|Float / float|float|4 Bytes|
|@DoubleType|Double / double|double|8 Bytes|
|@Integer8Type|Integer / int|char|1 Byte|
|@Integer16Type|Integer / int|short|2 Bytes|
|@UInteger8Type|Integer / int|unsigned char|1 Byte|
|@UInteger16Type|Integer / int|unsigned short|2 Bytes|
|@UInteger32Type|Long / long|unsigned long|4 Bytes|
|@BinaryType|byte[]|char[]|N Bytes|
|@StringType|java.lang.String|--|N Bytes|
|@TimestampType|java.sql.Timestamp|--|4 / 8 Bytes|


## *Build Requirements*
* Java 1.8+
* Maven 3.5+

## *License*

FastProto is released under the [Apache 2.0 license](license).

```
Copyright 1999-2020 indunet.org group Holding Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at the following link.

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
