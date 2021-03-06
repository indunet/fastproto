![fastproto](logo.png "fastproto")

English | [中文](README-zh.md)

# *FastProto*

[![Build Status](https://travis-ci.com/indunet/fastproto.svg?branch=master)](https://travis-ci.com/indunet/fastproto)
[![codecov](https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU)](https://codecov.io/gh/indunet/fastproto)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ed904d7aacd142f08b5cd50b16b1d74b)](https://www.codacy.com/gh/indunet/fastproto/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=indunet/fastproto&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

FastProto is a protocolized binary serialization & deserialization tool written in Java, which allows users to
customize protocol through annotations to decode and encode binary data. 
It solves the problem of cross-language and cross-platform data exchange of Java in a new form, especially suitable for 
the field of Internet of Things(IoT).

## *Features*

*   Protocolized binary serialization & deserialization
    *   Support unsigned data type
    *   Support reverse addressing
    *   Customize endianness
    *   Support [decoding formula & encoding formula][formula]
*   Support data [compress and decompress(gzip, deflate)][compression]
*   Support [protocol version verification][protocol-version]
*   Support [data integrity check][checksum]
*   Support data decrypt and encrypt
*   Built-in [Kafka serializer & deserializer][kafka]
*   Built-in Netty decoder & encoder

## *Under Developing*

*   Performance optimization
*   Circular reference
    Arbitrary type array

## Compared with ProtoBuf

For Java alone, it can be said that the two solve the same problem in different ways.
In contrast, FastProto is more suitable for the Internet of Things (IoT). FastProto is recommended for the following scenarios:

* Due to bandwidth or traffic limitations, smaller serialization results are required
* Embedded devices use C/C++ programming, which is inconvenient to process data in JSON/XML format
* ProtoBuf cannot be used due to compatibility issues
* Embedded devices use non-traditional programming methods, such as ladder diagram, function diagram and ST, etc., and data can only be exchanged in binary format
* Collect field bus data (CAN/MVB/RS-485) through the gateway and forward it in binary format

## *Maven*

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>1.6.2</version>
</dependency>
```

## *Quick Start*

Imagine such an application, there is a monitoring device collecting weather data in realtime and sends to
the weather station in binary format，the binary data has fixed length of 20 bytes:

>   65 00 7F 69 3D 84 7A 01 00 00 55 00 F1 FF 0D 00 00 00 07 00

The binary data contains 8 different types of signals, the specific protocol is as follows:

| Byte Offset | Bit Offset | Data Type(C/C++)   | Signal Name       | Unit |  Formula  |
|:-----------:|:----------:|:--------------:|:-----------------:|:----:|:---------:|
| 0           |            | unsigned char  | device id         |      |           |
| 1           |            |                | reserved          |      |           |
| 2-9         |            | long long      | time              |  ms  |           |
| 10-11       |            | unsigned short | humidity          |  %RH |           |
| 12-13       |            | short          | temperature       |  ℃  |            |
| 14-17       |            | unsigned int   | pressure          |  Pa  | p * 0.1   |
| 18          | 0          | bool           | temperature valid |      |           |
| 18          | 1          | bool           | humidity valid    |      |           |
| 18          | 2          | bool           | pressure valid    |      |           |
| 18          | 3-7        |                | reserved          |      |           |
| 19          |            |                | reserved          |      |           |

After the weather station receives the binary data, it hopes to convert it into a Java data object for subsequent business
function development. Therefore, the Java data object is defined in accordance with the protocol and the corresponding fields
are annotated with FastProto data type annotations.
It should be noted that the `value` field of any data type annotation corresponds to the byte offset of the signal.

```java
public class Weather {
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
Deserialize the binary data into the Java data object through `FastProto::parseFrom()` method.

```java
byte[] datagram = ...   // Datagram sent by monitoring device.

Weather weather = FastProto.parseFrom(datagram, Weather.class);
```

Similarly, serialize Java data object into binary data through the `FastProto::toByteArray()` method.
The second parameter of this method is the length of the data message. If the user does not specify it, FastProto will
automatically infer the length.

```java
byte[] datagram = FastProto.toByteArray(weather, 20);
```

It’s important to note that the pressure signal corresponds to a conversion formula, which usually requires the user to
multiply the serialized result by 0.1. To help users reduce intermediate steps, FastProto implements the above process 
through encoding formulas and decoding formulas.

The custom decoding formula needs to implement the `java.lang.function.Function` interface, and then specify the decoding
formula through the `afterDecode` field of the data type annotation.

```java
public class PressureDecodeFormula implements Function<Long, Double> {
    @Override
    public Double apply(Long value) {
        return value * 0.1;
    }
}
```

```java
@UInteger32Type(value = 14, afterDecode = PressureDecodeFormula.class)
double pressure;
```

Similarly, the encoding formula also needs to implement the `java.lang.function.Function` interface, and then specify
the encoding formula through the `beforeEncode` field of the data type annotation. [more][formula]

```java
public class PressureEncodeFormula implements Function<Double, Long> {
    @Override
    public Long apply(Double value) {
        return (long) (value * 10);
    }
}
```

```java
@UInteger32Type(value = 14, afterDecode = PressureDecodeFormula.class, beforeEncode = PressureEncodeFormula.class)
double pressure;
```

## *FastProto Annotations*

FastProto supports Java primitive data types, Timestamp, String and byte array. The above types can be replaced by `@AutoType`.
Taking into account cross-language and cross-platform data exchange, FastProto also introduces unsigned types.

| Annotation      | Java               | C/C++          | Size        |   AutoType |
|:---------------:|:------------------:|:--------------:|:-----------:|:-----------:|
| `@BooleanType`    | Boolean / boolean  | bool           | 1 bit       |  √ |    
| `@CharacterType`  | Character / char   | --             | 2 bytes     |  √  |    
| `@ByteType`       | Byte / byte        | char           | 1 byte      |  √  |    
| `@ShortType`      | Short / short      | short          | 2 bytes     |  √  |    
| `@IntegerType`    | Integer / int      | int            | 4 bytes     |  √ |    
| `@LongType`       | Long / long        | long long      | 8 bytes     |  √ |    
| `@FloatType`      | Float / float      | float          | 4 bytes     |  √  |    
| `@DoubleType`     | Double / double    | double         | 8 bytes     |  √ |    
| `@Integer8Type`   | Integer / int      | char           | 1 byte      |  ×  |    
| `@Integer16Type`  | Integer / int      | short          | 2 bytes     |  × |    
| `@UInteger8Type`  | Integer / int      | unsigned char  | 1 byte      |  ×  |    
| `@UInteger16Type` | Integer / int      | unsigned short | 2 bytes     |  × |    
| `@UInteger32Type` | Long / long        | unsigned long  | 4 bytes     |  × |    
| `@UInteger64Type` | BigInteger        | unsigned long long | 8 bytes  |  √ |    
| `@BinaryType`     | byte[]             | char[]         | N bytes     |  √  |    
| `@StringType`     | java.lang.String   | --             | N bytes     |  √ |    
| `@TimestampType`  | java.sql.Timestamp | --             | 4 / 8 bytes |  √  |    

FastProto also provides some auxiliary annotations to help users further customize the binary format, decoding and encoding process.

| Annotation    | Scope        | Description                           |
|:-------------:|:------------:|:-------------------------------------:|
| `@Endian`       | Class & Field | Endianness, default as little endian. |
| `@DecodeIgnore` | Field        | Ignore the field when decoding.       |
| `@EncodeIgnore` | Field        | Ignore the field when encoding.       |
| `@EnableCompress` | Class        | Enable compress & decompress, default as deflate |
| `@EnableProtocolVersion` | Class     |  Enable protocol version verification  |
| `@EnableCheckSum`      |  Class      |  Enable checksum verification               |
| `@EnableCrypto`   |  Class | Enable encrypt & decrypt |

## *Benchmark*

*   macOS, m1 8 cores, 16gb
*   openjdk 1.8.0_292
*   datagram of 128 bytes and nested protocol class of 48 fields

|Benchmark |    Mode  | Samples  |  Score  |   Error   |   Units   |
|:--------:|:--------:|:--------:|:-------:|:---------:|:---------:|
| `FastProto::parseFrom` |  throughput   |   10  |   115.2 | ± 1.6    |  ops/ms   |
| `FastProto::toByteArray` | throughput  |   10  |   285.7 | ± 1.5    |  ops/ms   |

## *Build Requirements*

*   Java 1.8+
*   Maven 3.5+

## *License*

FastProto is released under the [Apache 2.0 license](license).

```
Copyright 2019-2021 indunet.org

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

[formula]: https://github.com/indunet/fastproto/wiki/Formula
[kafka]: https://github.com/indunet/fastproto/wiki/Work-with-Kafka
[checksum]: https://github.com/indunet/fastproto/wiki/Data-Integrity-Check
[protocol-version]: https://github.com/indunet/fastproto/wiki/Protocol-Version
[compression]: https://github.com/indunet/fastproto/wiki/Compression