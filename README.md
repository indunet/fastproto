![fastproto](logo.png "fastproto")

English | [中文](README-zh.md)

# *Fast Protocol*

[![Build Status](https://app.travis-ci.com/indunet/fastproto.svg?branch=master)](https://app.travis-ci.com/indunet/fastproto)
[![codecov](https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU)](https://codecov.io/gh/indunet/fastproto)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ed904d7aacd142f08b5cd50b16b1d74b)](https://www.codacy.com/gh/indunet/fastproto/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=indunet/fastproto&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/)
[![JetBrain Support](https://img.shields.io/badge/JetBrain-support-blue)](https://www.jetbrains.com/community/opensource)
[![License](https://img.shields.io/badge/license-Apache%202.0-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

FastProto is a protocolized binary serialization & deserialization tool written in Java. It can not only customize the binary
protocol through annotations, but also supports data compression, encryption, and data integrity checksum, protocol version
verification. FastProto uses a new way to solve the problem of cross-language and cross-platform data exchange in Java, 
which is especially suitable for the Internet of Things (IoT).

## *Features*

*   Protocolized binary serialization & deserialization
    *   Support unsigned data type
    *   Support reverse addressing, suitable for non-fixed length binary data
    *   Customize endianness (byte order)
    *   Support [decoding formula & encoding formula][formula]
*   Support data [compress and decompress(gzip, deflate)][compression]
*   Support [protocol version verification][protocol-version]
*   Support [data integrity verification][checksum]
*   Support data decrypt & encrypt
*   Built-in [Kafka serializer & deserializer][kafka]
*   Built-in Netty decoder & encoder

## *Under Developing*

*   Code structure & performance optimization
*   Add test cases to increase unit test coverage
*   Parse multiple pieces of binary data into one data object

## Compared with ProtoBuf

Although both ProtoBuf and FastProto are used to solve the problem of cross-language and cross-platform data exchange, 
they have completely different ways of solving the problem:

*   ProtoBuf customizes the protocol by writing schema, and FastProto customizes the protocol by annotation
*   ProtoBuf can adapt to multiple languages, while FastProto only targets the Java language
*   FastProto performance is more superior, custom protocol granularity is more refined

FastProto is more recommended for the following scenarios:

*   The performance requirements are demanding, and the performance loss caused by common data formats (JSON/XML) cannot be tolerated
*   The data source contains a lot of binary content, such as data collected through fieldbus (CAN/MVB/RS-485), which is not suitable for text format
*   Restrictions on end software development can only be in binary format, and ProtoBuf is not supported. For example, 
    embedded devices use non-traditional programming methods (ladder diagram/function diagram/ST)

## *Maven*

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>3.1.1</version>
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

1. **Serialization & Deserialization**

After the weather station receives the data, it needs to be converted into Java data objects for subsequent business function development.
First, define the Java data object `Weather` according to the protocol, and then use the FastProto data type annotation to annotate each attribute.
It should be noted that the `value` attribute of any data type annotation corresponds to the byte offset of the signal.

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

Invoke the `FastProto::parseFrom()` method to deserialize the binary data into the Java data object `Weather`

```java
byte[] datagram = ...   // Datagram sent by monitoring device.

Weather weather = FastProto.parseFrom(datagram, Weather.class);
```

Invoke the `FastProto::toByteArray()` method to serialize the Java data object `Weather` into binary data.
The second parameter of this method is the length of the binary data. 
If the user does not specify it, FastProto will automatically guess the length.

```java
byte[] datagram = FastProto.toByteArray(weather, 20);
```

2. **Decoding Formula & Encoding Formula**

Perhaps you have noticed that the pressure signal corresponds to a conversion formula, usually requiring the user to multiply
the serialized result by 0.1, which is an extremely common operation in IoT data exchange.
To help users reduce intermediate steps, FastProto introduces encoding formulas and decoding formulas.

The custom decoding formula needs to implement the `java.lang.function.Function` interface, and then specify the decoding
formula through the `decodingFormula` attribute of the data type annotation.

```java
public class PressureDecodeFormula implements Function<Long, Double> {
    @Override
    public Double apply(Long value) {
        return value * 0.1;
    }
}
```

```java
public class Weather {
    ...

    @UInteger32Type(value = 14, decodingFormula = DecodeSpeedFormula.class)
    double pressure;
}
```

Similarly, In the same way, the encoding formula also needs to implement the `java.lang.function.Function` interface, and
then specify the encoding formula through the `encodingFormula` attribute of the data type annotation. [more][formula]

```java
public class PressureEncodeFormula implements Function<Double, Long> {
    @Override
    public Long apply(Double value) {
        return (long) (value * 10);
    }
}
```

```java
public class Weather {
    ...

    @UInteger32Type(value = 14, decodingFormula = PressureDecodeFormula.class, encodingFormula = PressureEncodeFormula.class)
    double pressure;
}
```

3. **Other Functions**

FastProto supports data compression, protocol version verification, data integrity verification, and data symmetric encryption.
Each function can be enabled by annotations.

```java
@EnableCrypto(value = CryptoPolicy.AES_ECB_PKCS5PADDING, key = "330926")
@EnableProtocolVersion(value = 78, version = 17)
@EnableCompress(value = CompressPolicy.DEFLATE, level = 2)
@EnableChecksum(value = -4, start = 0, length = -5, checkPolicy = CheckPolicy.CRC32, endianPolicy = EndianPolicy.BIG)
public class Weather {
    ...
}
```

## *Core Annotations*

FastProto supports Java primitive data types, Timestamp, String and byte array. The above types can be replaced by `@AutoType`.
Taking into account cross-language and cross-platform data exchange, FastProto also introduces unsigned types. [more][types]

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
| `@TimestampType`  | java.sql.Timestamp / java.util.Date | --             | 4 / 8 bytes |  √  |    
| `@ArrayType`     | primitive type array   | primitive type array    | N 字节     |  √ |    
| `@ListType`  | primitive type list | --             | N 字节 |  √  |    
| `@EnumType`     | enum   | enum             | N 字节     |  √ |

FastProto also provides some auxiliary annotations to help users further customize the binary format, decoding and encoding process.

| Annotation    | Scope        | Description                           |
|:-------------:|:------------:|:-------------------------------------:|
| `@Endian`       | Class & Field | Endianness, default as little endian. |
| `@DecodingIgnore` | Field        | Ignore the field when decoding.       |
| `@EncodingIgnore` | Field        | Ignore the field when encoding.       |
| `@EnableCompress` | Class        | Enable compress & decompress, default as deflate |
| `@EnableProtocolVersion` | Class     |  Enable protocol version verification  |
| `@EnableCheckSum`      |  Class      |  Enable checksum verification               |
| `@EnableCrypto`   |  Class | Enable encrypt & decrypt |
| `@EnableFixedLength`   |  Class | Enable fixed length of datagram |

## *Benchmark*

*   macOS, m1 8 cores, 16gb
*   openjdk 1.8.0_292
*   datagram of 128 bytes and nested protocol class of 48 fields

|Benchmark |    Mode  | Samples  |  Score  |   Error   |   Units   |
|:--------:|:--------:|:--------:|:-------:|:---------:|:---------:|
| `FastProto::parseFrom` |  throughput   |   10  |   291.2 | ± 1.6    |  ops/ms   |
| `FastProto::toByteArray` | throughput  |   10  |   285.7 | ± 1.5    |  ops/ms   |

## *Build Requirements*

*   Java 1.8+
*   Maven 3.5+

## *Welcome*

FastProto has obtained the support of JetBrain Open Source Project, which can provide free license of all product pack for
all core contributors.
If you are interested in this project and want to join and undertake part of the work (development/testing/documentation),
please feel free to contact me via email <deng_ran@foxmail.com>

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
[types]: https://github.com/indunet/fastproto/wiki/Data-Type-Annotations