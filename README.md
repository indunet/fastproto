![fastproto](logo.png "fastproto")

# *FastProto*

[![Build Status](https://travis-ci.com/indunet/fastproto.svg?branch=master)](https://travis-ci.com/indunet/fastproto)
[![codecov](https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU)](https://codecov.io/gh/indunet/fastproto)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ed904d7aacd142f08b5cd50b16b1d74b)](https://www.codacy.com/gh/indunet/fastproto/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=indunet/fastproto&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

FastProto is a protocolized binary serialization & deserialization tool written in Java, which allows developers to 
customize binary format through annotations. It solves the problem of cross-language and cross-platform data exchange 
of Java in a new form, especially suitable for the field of Internet of Things(IoT).

## *Features*

*   Protocolized binary serialization & deserialization
*   Customize binary format through annotations.    
*   Support [decoding formula & encoding formula][formula]   
*   Support unsigned data types such as uint8, uint16, uint32 and uint64 
*   Support data [compress and decompress(gzip, deflate)][compression]  
*   Support [protocol version verification][version]
*   Support [data integrity check][integrity]
*   Built-in [Kafka serializer & deserializer][kafka]

## *Under Developing*

*   Netty decoder & encoder  

## Compared with ProtoBuf

Compared with ProtoBuff, FastProto does not have any obvious advantages, it can only be said that FastProto and
ProtoBuf solve the same problem in different ways. It is recommended to use FastProto in the following scenarios:

*   Due to network bandwidth and traffic limitations, smaller serialization results are required
*   Unable to unify technical routes between systems, especially for embedded systems
*   Due to debugging requirements, the serialized result must be human readable
*   Read & write binary format files

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
the weather station server in binary format. The datagram protocol is as follows:

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

The binary datagram contains 8 signals of different data types, define the Java data object and annotate it with FastProto
annotations according to the above datagram protocol. The default parameter of the data type annotation is the byte offset.

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
Deserialize the binary datagram into the Java data object through `FastProto::parseFrom()` method.

```java
byte[] datagram = ...   // Datagram sent by monitoring device.

Weather weather = FastProto.parseFrom(datagram, Weather.class);
```

Serialize the Java data object into binary datagram through `FastProto::toByteArray()` method, the second parameter is
 the datagram size.

```java
byte[] datagram = FastProto.toByteArray(weather, 20);
```

Perhaps you have noticed that the pressure signal in the protocol table corresponds to a conversion formula, which 
needs to be multiplied by 0.1. In order to help developers reduce intermediate steps, FastProto supports customizing 
conversion formula.

Define a decoding conversion formula, which must implement `java.lang.function.Function`

```java
public class PressureDecodeFormula implements Function<Long, Double> {
    @Override
    public Double apply(Long value) {
        return value * 0.1;
    }
}
```

Modify the annotation and data type of the pressure field.

```java
@UInteger32Type(value = 14, afterDecode = DecodeSpeedFormula.class)
double pressure;
```

An encoding formula is needed if need to perform serialization, which also need to implement the `java.lang.function.Function`

```java
public class PressureEncodeFormula implements Function<Double, Long> {
    @Override
    public Long apply(Double value) {
        return (long) (value * 10);
    }
}
```

Modify the annotation of the pressure field.

```java
@UInteger32Type(value = 14, afterDecode = PressureDecodeFormula.class, beforeEncode = PressureEncodeFormula.class)
double pressure;
```

## *FastProto Annotations*

In addition to Java primitive data types and their wrapper classes, FastProto also supports Timestamp, String and byte array, 
all above type annotations can be replaced by `@AutoType`. Taking into account cross-language data sharing, unsigned types, 
int8, int16 are also introduced. 

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

In addition to data type annotations, FastProto also provides other annotations to help developers Help users customize 
the binary format.

| Annotation    | Scope        | Description                           |
|:-------------:|:------------:|:-------------------------------------:|
| `@Endian`       | Class & Field | Endianness, default as little endian. |
| `@DecodeIgnore` | Field        | Ignore the field when decoding.       |
| `@EncodeIgnore` | Field        | Ignore the field when encoding.       |
| `@EnableCompress` | Class        | Compress or decompress datagram, default as gzip. |
| `@ProtocolVersion` | Class     |  Add protocol version to datagram and validate when deserializing  |
| `@CheckSum`      |  Class      |  Data integrity check               |

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
Copyright 2019-2021 indunet

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

[formula]: https://github.com/indunet/fastproto/wiki/Conversion-Formula
[kafka]: https://github.com/indunet/fastproto/wiki/Work-with-Kafka
[integrity]: https://github.com/indunet/fastproto/wiki/Data-Integrity-Check
[version]: https://github.com/indunet/fastproto/wiki/Protocol-Version
[compression]: https://github.com/indunet/fastproto/wiki/Compression