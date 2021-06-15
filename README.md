![fastproto](logo.png "fastproto")

# *What is FastProto?*

[![Build Status](https://travis-ci.com/indunet/fastproto.svg?branch=master)](https://travis-ci.com/indunet/fastproto)
[![codecov](https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU)](https://codecov.io/gh/indunet/fastproto)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

FastProto is a protocolized binary serialization & deserialization tool written in Java, which allows developers to 
customize binary format through annotations. It solves the problem of cross-language and cross-platform data sharing 
of Java in a new form, especially suitable for the field of Internet of Things(IoT).

[formula]: https://github.com/indunet/fastproto/wiki/Conversion-Formula
[kafka]: https://github.com/indunet/fastproto/wiki/Work-with-Kafka

## *Features*

* Binary serialization & deserialization
* Support [decoding formula & encoding formula][formula]
* Customize binary format through annotations
* Support unsigned data types such as uint8, uint16, uint32 and uint64
* Custom endianness(big endian or little endian), datagram reverse addressing
* Support datagram compress and decompress(gzip, deflate)
* AutoType, automatically infer all Java primitive data types and their wrapper classes when using `@AutoType`
* Built-in [Kafka serializer & deserializer][kafka]

## *Under Developing*

* Cyclic Redundancy Check(CRC8, CRC16, CRC32)
* Netty decoder & encoder

## *Maven*

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>1.5.2</version>
</dependency>
```

# *Quick Start*

Imagine such an application, there is a monitoring device collecting weather data in realtime and sends it to 
the weather station server in the form of binary datagram, the datagram protocol is as follows:

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
annotations according to the above datagram protocol.

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
Deserialize the binary datagram into Java data object through `FastProto::parseFrom()` method.

```java
byte[] datagram = ...   // Datagram sent by monitoring device.

Weather weather = FastProto.parseFrom(datagram, Weather.class);
```

The serialization process is similar, serialize the Java data object into binary datagram through `FastProto::toByteArray()` 
method, the second parameter is the datagram size.

```java
byte[] datagram = FastProto.toByteArray(weather, 20);
```

Perhaps you have noticed that the pressure signal in the protocol table corresponds to a conversion formula, which 
needs to be multiplied by 0.1, that is very common in IoT communications. In order to help developers reduce intermediate 
steps, FastProto supports specifing decoding formulas and encoding formulas.

First define a decoding conversion formula, which must implement the `java.lang.function.Function`.

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

An encoding formula is needed if serialize the object, which also need to 
implement the `java.lang.function.Function`.

```java
public class PressureEncodeFormula implements Function<Double, Long> {
    @Override
    public Long apply(Double value) {
        return (int) (value * 10);
    }
}
```

Modify the annotation of the pressure field.

```java
@UInteger32Type(value = 14, afterDecode = PressureDecodeFormula.class, beforeEncode = PressureEncodeFormula.class)
double pressure;
```

# *FastProto Annotations*

FastProto's protocol type adopts Java naming rules. In addition to Java primitive data types and their wrapper classes, 
it also supports Timestamp, String and byte array. The above types can be replaced by `@AutoType`. FastProto automatically 
infer by the field type. Thinking of cross-platform data transmission, unsigned data types such as uint8, uint16 and
 uint32 are also introduced.

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

In addition to protocol type annotations, FastProto also provides some annotation assistance to help developers control 
the serialization process more accurately.

| Annotation    | Scope        | Description                           |
|:-------------:|:------------:|:-------------------------------------:|
| `@Endian`       | Class & Field | Endianness, default as little endian. |
| `@DecodeIgnore` | Field        | Ignore the field when decoding.       |
| `@EncodeIgnore` | Field        | Ignore the field when encoding.       |
| `@EnableCompress` | Class        | Compress or decompress datagram, default as gzip. |
| `@ProtocolVersion` | Class     |  Add protocol version to datagram and validate when deserializing  |

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