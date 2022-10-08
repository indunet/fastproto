![fastproto](logo.png "fastproto")

English | [中文](README-zh.md)

# *Fast Protocol*

[![Build Status](https://app.travis-ci.com/indunet/fastproto.svg?branch=master)](https://app.travis-ci.com/indunet/fastproto)
[![codecov](https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU)](https://codecov.io/gh/indunet/fastproto)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ed904d7aacd142f08b5cd50b16b1d74b)](https://www.codacy.com/gh/indunet/fastproto/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=indunet/fastproto&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/)
[![JetBrain Support](https://img.shields.io/badge/JetBrain-support-blue)](https://www.jetbrains.com/community/opensource)
[![License](https://img.shields.io/badge/license-Apache%202.0-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Gitee](https://img.shields.io/badge/repo-gitee-blue)](https://gitee.com/indunet/fastproto)

FastProto is a binary serialization & deserialization tool that can customize protocol through annotations. It is written in Java and 
solve the problem of cross-language and cross-platform data exchange, which is especially suitable for the Internet of Things (IoT).

## *Features*

* Binary serialization & deserialization, customize protocol through annotations
* Support primitive type, unsigned type, string type, time type and array type  
* Support reverse addressing, suitable for non-fixed length binary data
* Customize endianness (byte order)
* Support decoding formula & encoding formula

## *Under Developing*

* Support list type
* Code structure & performance optimization

## *Compared with ProtoBuf*

Although both ProtoBuf and FastProto are used to solve the problem of cross-language and cross-platform data exchange, 
they have completely different ways of solving the problem:

*   ProtoBuf customizes the protocol by writing schema, and FastProto customizes the protocol by annotations
*   ProtoBuf is available only when using by both side, while FastProto can be used by each side because of open protocol
*   FastProto performance is more superior, custom protocol granularity is more refined

FastProto is more recommended for the following scenarios:

* Binary data format & open protocol
* The performance requirements are demanding, and the performance loss caused by common data formats (JSON/XML) cannot be tolerated
* The data source contains a lot of binary content, such as data collected through fieldbus (CAN/MVB/RS-485), which is not suitable for text format

## *Maven*

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>3.6.2</version>
</dependency>
```

## *Quick Start*

Imagine such an application, there is a monitoring device collecting weather data in realtime and sends to
the weather station in binary format，the binary data has fixed length of 20 bytes:

>   65 00 7F 69 3D 84 7A 01 00 00 55 00 F1 FF 0D 00 00 00 07 00

The binary data contains 8 different types of signals, the specific protocol is as follows:

| Byte Offset | Bit Offset | Data Type(C/C++)  | Signal Name       | Unit |  Formula  |
|:-----------:|:----------:|:-----------------:|:-----------------:|:----:|:---------:|
| 0           |            |   unsigned char   | device id         |      |           |
| 1           |            |                   | reserved          |      |           |
| 2-9         |            |       long        | time              |  ms  |           |
| 10-11       |            |  unsigned short   | humidity          |  %RH |           |
| 12-13       |            |       short       | temperature       |  ℃  |            |
| 14-17       |            |   unsigned int    | pressure          |  Pa  | p * 0.1   |
| 18          | 0          |       bool        | temperature valid |      |           |
| 18          | 1          |       bool        | humidity valid    |      |           |
| 18          | 2          |       bool        | pressure valid    |      |           |
| 18          | 3-7        |                   | reserved          |      |           |
| 19          |            |                   | reserved          |      |           |

1. **Serialization & Deserialization**

After the weather station receives the data, it needs to be converted into Java data objects for subsequent business function development.
First, define the Java data object `Weather` according to the protocol, and then use the FastProto data type annotation to annotate each attribute.
It should be noted that the `offset` attribute of any data type annotation corresponds to the byte offset of the signal.

```java
import org.indunet.fastproto.annotation.type.*;

public class Weather {
    @UInt8Type(offset = 0)
    int id;

    @TimeType(offset = 2)
    Timestamp time;

    @UInt16Type(offset = 10)
    int humidity;

    @Int16Type(offset = 12)
    int temperature;

    @UInt32Type(offset = 14)
    long pressure;

    @BoolType(byteOffset = 18, bitOffset = 0)
    boolean temperatureValid;

    @BoolType(byteOffset = 18, bitOffset = 1)
    boolean humidityValid;

    @BoolType(byteOffset = 18, bitOffset = 2)
    boolean pressureValid;
}
```

Invoke the `FastProto::parse()` method to deserialize the binary data into the Java data object `Weather`

```java
// datagram sent by monitoring device.
byte[] datagram = ...   

Weather weather = FastProto.parse(datagram, Weather.class);
```

Invoke the `FastProto::toBytes()` method to serialize the Java data object `Weather` into binary data.
The second parameter of this method is the length of the binary data. 
If the user does not specify it, FastProto will automatically guess the length.

```java
byte[] datagram = FastProto.toBytes(weather, 20);
```

2. **Decoding Formula & Encoding Formula**

Perhaps you have noticed that the pressure signal corresponds to a conversion formula, usually requiring the user to multiply
the serialized result by 0.1, which is an extremely common operation in IoT data exchange.
To help users reduce intermediate steps, FastProto introduces encoding formulas and decoding formulas.

The custom decoding formula needs to implement the `java.lang.function.Function` interface, and then specify the decoding
formula through annotation `@DecodingFormula`

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

    @UInt32Type(offset = 14, decodingFormula = DecodeSpeedFormula.class)
    double pressure;
}
```

Similarly, In the same way, the encoding formula also needs to implement the `java.lang.function.Function` interface, and
then specify the encoding formula through annotation `@EncodingFormula`.

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
    
    @UInt32Type(offset = 14)
    @DecodingFormula(PressureDecodeFormula.class)
    @EncodingFormula(PressureEncodeFormula.class)
    double pressure;
}
```

## *Annotations*

### *Primitive Type*
FastProto supports Java primitive data types, time type, String type, enum type and byte array type, taking into account 
cross-language and cross-platform data exchange, FastProto also introduces unsigned types.

|      Annotation       |                 Java                  |     C/C++      |   Size    |
|:---------------------:|:-------------------------------------:|:--------------:|:---------:|
|       @BoolType       |           Boolean / boolean           |      bool      |   1 bit   |    
| @CharType(ASCII Only) |           Character / char            |      char      |  1 bytes  |   
|      @Int32Type       |             Integer / int             |      int       |  4 bytes  | 
|      @Int64Type       |              Long / long              |      long      |  8 bytes  |   
|      @FloatType       |             Float / float             |     float      |  4 bytes  |  
|      @DoubleType      |            Double / double            |     double     |  8 bytes  |  
|       @Int8Type       |      Byte / byte / Integer / int      |      char      |  1 byte   |  
|      @Int16Type       |     Short / short / Integer / int     |     short      |  2 bytes  |  
|      @UInt8Type       |             Integer / int             | unsigned char  |  1 byte   |   
|      @UInt16Type      |             Integer / int             | unsigned short |  2 bytes  |   
|      @UInt32Type      |              Long / long              |  unsigned int  |  4 bytes  |   
|      @UInt64Type      |              BigInteger               | unsigned long  |  8 bytes  |  
|      @StringType      | String / StringBuilder / StringBuffer |       --       |  N bytes  |   
|       @TimeType       |      Timestamp / Date / Calendar      |      long      |  8 bytes  |  
|       @EnumType       |                 enum                  |      enum      |  1 bytes  |

### *Array Annotations*

|    Annotation    |                 Java                  |      C/C++       |
|:----------------:|:-------------------------------------:|:----------------:|
|   @BinaryType    |            Byte[] / byte[]            |      char[]      |
|  @Int8ArrayType  |  Byte[] / byte[] / Integer[] / int[]  |      char[]      |
| @Int16ArrayType  | Short[] / short[] / Integer[] / int[] |     short[]      |
| @Int32ArrayType  |          Integer[] /  int[]           |      int[]       |
| @Int64ArrayType  |            Long[] / long[]            |      long[]      |
| @UInt8ArrayType  |           Integer[] / int[]           | unsigned char[]  |
| @UInt16ArrayType |           Integer[] / int[]           | unsigned short[] |
| @UInt32ArrayType |            Long[] / long[]            |  unsigned int[]  |
| @UInt64ArrayType |             BigInteger[]              | unsigned long[]  |
| @FloatArrayType  |           Float[] / float[]           |     float[]      |
| @DoubleArrayType |          Double[] / double[]          |     double[]     |

### *Other Annotations*
FastProto also provides some auxiliary annotations to help users further customize the binary format, decoding and encoding process.

|   Annotation    |   Scope   |                    Description                    |
|:---------------:|:---------:|:-------------------------------------------------:|
| @DefaultEndian  |   Class   |       Endianness, default as little endian.       |
| @DecodingIgnore |   Field   |          Ignore the field when decoding.          |
| @EncodingIgnore |   Field   |          Ignore the field when encoding.          |
|  @FixedLength   |   Class   |         Enable fixed length of datagram.          |


## Scala
FastProto supports case class，but Scala is not fully compatible with Java annotations, so please refer to FastProto as follows.

```scala
import org.indunet.fastproto.annotation.scala._
```

## *Benchmark*

*   macOS, m1 8 cores, 16gb
*   openjdk 1.8.0_292
*   datagram of 128 bytes and nested protocol class of 48 fields

|Benchmark |    Mode  | Samples  | Score |   Error   |   Units   |
|:--------:|:--------:|:--------:|:-----:|:---------:|:---------:|
| `FastProto::parse` |  throughput   |   10  |  132  | ± 1.6    |  ops/ms   |
| `FastProto::toBytes` | throughput  |   10  |  201  | ± 1.5    |  ops/ms   |

## *Build Requirements*

*   Java 1.8+
*   Maven 3.5+

## *Welcome*

FastProto has obtained the support of JetBrain Open Source Project, which can provide free license of all product pack for
all core contributors.
If you are interested in this project and want to join and undertake part of the work (development/testing/documentation),
please feel free to contact me via email <deng_ran@foxmail.com>

Gitee Repository: [gitee.com/indunet/fastproto](https://gitee.com/indunet/fastproto)

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
