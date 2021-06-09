# *What is FastProto?*

[![Build Status](https://travis-ci.com/indunet/fastproto.svg?branch=master)](https://travis-ci.com/indunet/fastproto)
[![codecov](https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU)](https://codecov.io/gh/indunet/fastproto)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

FastProto is a binary serialization & deserialization tool written in Java. 
Different from other serialization tools, FastProto allows developers to accurately control the serialization process 
through annotations, including byte offset, bit offset, data types, endianness,  reverse addressing, data transformation formulas.

FastProto solves the problem of cross-language and cross-platform data exchange of Java in a new way, especially suitable for the field of Internet of Things.

## *Features*

* Binary serialization & deserialization
* Control the serialization process through annotations
* Support all Java primitive data types and their wrapper classes
* Support unsigned data types such as uint8, uint16 and uint32
* Custom endianness(big endian or little endian), datagram reverse addressing
* Support datagram compress and decompress(gzip, deflate)
* AutoType, automatically infer all Java primitive data types and their wrapper classes when using `@AutoType`

## *Under Developing*

* Cyclic Redundancy Check(CRC8, CRC16, CRC32)
* Kafka serializer & deserializer
* Netty decoder & encoder

## *Maven*

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>1.4.1</version>
</dependency>
```

# *Quick Start*

Imagine such an application, there is a monitoring device collecting weather data in realtime and sends it to 
the weather station server in the form of binary datagram, the datagram protocol is as follows:

| Byte Offset | Bit Offset | Data Type(C/C++)   | Signal Name       | Unit |
|:-----------:|:----------:|:--------------:|:-----------------:|:----:|
| 0           |            | unsigned char  | device id         |      |
| 1           |            |                | reserved          |      |
| 2-9         |            | long long      | time              |  ms  |
| 10-11       |            | unsigned short | humidity          |  %RH |
| 12-13       |            | short          | temperature       |  ℃  | 
| 14-17       |            | unsigned int   | pressure          |  Pa  |
| 18          | 0          | bool           | temperature valid |      |
| 18          | 1          | bool           | humidity valid    |      |
| 18          | 2          | bool           | pressure valid    |      |
| 18          | 3-7        |                | reserved          |      |
| 19          |            |                | reserved          |      |

The binary datagram contains 8 signals of different data types, define the Java data object and annotate it with FastProto
annotations according to the above datagram protocol.

```java
public class WeatherMetrics {
    @UInteger8Type(0)
    int id;

    @TimestampType(2)
    Timestamp time;

    @UInteger16Type(10)
    int humidity;

    @Integer16Type(12)
    int temperature;

    @UInteger32Type(14)
    long pressure;

    @BooleanType(value = 18, bitOffset = 0)
    boolean temperatureValid;

    @BooleanType(value = 18, bitOffset = 1)
    boolean humidityValid;

    @BooleanType(value = 18, bitOffset = 2)
    boolean pressureValid;
}
```
Deserialize the binary datagram into Java data object through `FastProto::decode()` method.

```
byte[] datagram = ...   // Datagram sent by monitoring device.

WeatherMetrics metrics = FastProto.decode(datagram, WeatherMetrics.class);
```

The serialization process is similar, serialize the Java data object into binary datagram through `FastProto::encode()` 
method, the second parameter is the datagram size.

```
byte[] datagram = FastProto.encode(metrics, 20);
```

# *FastProto Annotations*

FastProto's protocol type adopts Java naming rules. In addition to Java primitive data types and their wrapper classes, 
it also supports Timestamp, String and byte array. The above types can be replaced by `@AutoType`. FastProto automatically 
infer by the field type. Thinking of cross-platform data transmission, unsigned data types such as uint8, uint16 and
 uint32 are also introduced.

| Annotation      | Java               | C/C++          | Size        |   AutoType |
|:---------------:|:------------------:|:--------------:|:-----------:|:-----------:|
| `@BooleanType`    | Boolean / boolean  | bool           | 1 bit       | √        |    
| `@CharacterType`  | Character / char   | --             | 2 bytes     | √        |
| `@ByteType`       | Byte / byte        | char           | 1 byte      | √       |
| `@ShortType`      | Short / short      | short          | 2 bytes     | √       |
| `@IntegerType`    | Integer / int      | int            | 4 bytes     | √       |
| `@LongType`       | Long / long        | long long      | 8 bytes     | √       |
| `@FloatType`      | Float / float      | float          | 4 bytes     | √       |
| `@DoubleType`     | Double / double    | double         | 8 bytes     | √       |
| `@Integer8Type`   | Integer / int      | char           | 1 byte      | ×       |
| `@Integer16Type`  | Integer / int      | short          | 2 bytes     | ×       |
| `@UInteger8Type`  | Integer / int      | unsigned char  | 1 byte      | ×       |
| `@UInteger16Type` | Integer / int      | unsigned short | 2 bytes     | ×       |
| `@UInteger32Type` | Long / long        | unsigned long  | 4 bytes     | ×       |
| `@BinaryType`     | byte[]             | char[]         | N bytes     | √       |
| `@StringType`     | java.lang.String   | --             | N bytes     | √       |
| `@TimestampType`  | java.sql.Timestamp | --             | 4 / 8 bytes | √       |

In addition to protocol type annotations, FastProto also provides some annotation assistance to help developers control 
the serialization process more accurately.

| Annotation    | Scope        | Description                           |
|:-------------:|:------------:|:-------------------------------------:|
| `@Endian`       | Class & Field | Endianness, default as little endian. |
| `@DecodeIgnore` | Field        | Ignore the field when decoding.       |
| `@EncodeIgnore` | Field        | Ignore the field when encoding.       |
| `@Compress` | Class        | Compress or decompress datagram, default as GZIP. |

# *Performance Test*

* Windows 10
* JDK 1.8.0
* AMD Ryzen 5 3500U, 2.1GHz, 8 cores

The length of the test message is 128 bytes, and the test data object contains 48 fields(24 Boolean, 16 Integer, 8 Double). 
**FastProto can serialize 3000 objects or deserialize 1800 datagram within 1 second.**


# *Build Requirements*

* Java 1.8+
* Maven 3.5+

# *License*

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
```