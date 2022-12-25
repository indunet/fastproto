![fastproto](logo.png "fastproto")

English | [中文](README-zh.md)

# *Fast Protocol*

[![Build Status](https://app.travis-ci.com/indunet/fastproto.svg?branch=master)](https://app.travis-ci.com/indunet/fastproto)
[![codecov](https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU)](https://codecov.io/gh/indunet/fastproto)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ed904d7aacd142f08b5cd50b16b1d74b)](https://www.codacy.com/gh/indunet/fastproto/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=indunet/fastproto&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/)
[![JetBrain Support](https://img.shields.io/badge/JetBrain-support-blue)](https://www.jetbrains.com/community/opensource)
[![License](https://img.shields.io/badge/license-Apache%202.0-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

FastProto is a binary data processing tool written in Java. Developers can mark the field information in binary data (data type, byte offset, endianness, etc.) through annotations,
and then invoke simple API to realize parsing and packaging binary data.
It simplifies the process of binary data processing, and developers do not need to write complicated code.


## *Features*

* Mark field information through annotations, quickly parse and package binary data
* Support Java primitive type, unsigned type, string type, time type, array type and collection type, etc.
* Support reverse addressing, suitable for non-fixed-length binary data, for example -1 means the end of binary data
* Customize endianness (byte order)
* Support decoding formula & encoding formula including lambda expression


### *Under Developing*

* Add char type in working without annotations
* Write doc of Encoder api doc and Decoder api
* Add dynamic byte array
* Code structure & performance optimization

### *Maven*

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>3.8.3</version>
</dependency>
```


## *1. Quick Start*

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

### *1.1 Parse and Package Binary Data*

After the weather station receives the data, it needs to be converted into Java data objects for subsequent business function development.
First, define the Java data object `Weather` according to the protocol, and then use the FastProto data type annotation to annotate each attribute.
It should be noted that the `offset` attribute of annotation corresponds to the byte offset of the signal.

```java
import org.indunet.fastproto.annotation.*;

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

Invoke the `FastProto::parse()` method to parse the binary data into the Java data object `Weather`

```java
// datagram sent by monitoring device.
byte[] datagram = ...   

Weather weather = FastProto.parse(datagram, Weather.class);
```

Invoke the `FastProto::toBytes()` method to package the Java data object `Weather` into binary data.
The second parameter of this method is the length of the binary data. 
If the user does not specify it, FastProto will automatically guess the length.

```java
byte[] datagram = FastProto.toBytes(weather, 20);
```
### *1.2 Transformation Formula*

Perhaps you have noticed that the pressure signal corresponds to a conversion formula, usually requiring the user to multiply
the serialized result by 0.1, which is an extremely common operation in IoT data exchange.
To help users reduce intermediate steps, FastProto introduces decoding formula annotation `@DecodingFormula` and encoding formula annotation `@EncodingFormula`,
the above simple formula transformation can be implemented by Lambda expression.

```java
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.EncodingFormula;

public class Weather {
    ...

    @UInt32Type(offset = 14)
    @DecodingFormula(lambda = "x -> x * 0.1")
    @EncodingFormula(lambda = "x -> (long) (x * 10)")
    double pressure;
}
```


## *2. Annotations*

### *2.1 Primitive Data Type Annotations*

FastProto supports Java primitive data types, taking into account cross-language and cross-platform data exchange, unsigned types are also introduced.

| Annotation  |               Java                |     C/C++      |  Size   |
|:-----------:|:---------------------------------:|:--------------:|:-------:|
|  @BoolType  |          Boolean/boolean          |      bool      |  1 bit  |    
| @AsciiType  |          Character/char           |      char      | 1 bytes |   
|  @CharType  |          Character/char           |       --       | 2 bytes |   
| @Int32Type  |            Integer/int            |      int       | 4 bytes | 
| @Int64Type  |             Long/long             |      long      | 8 bytes |   
| @FloatType  |            Float/float            |     float      | 4 bytes |  
| @DoubleType |           Double/double           |     double     | 8 bytes |  
|  @Int8Type  |       Byte/byte/Integer/int       |      char      | 1 byte  |  
| @Int16Type  |     Short/short / Integer/int     |     short      | 2 bytes |  
| @UInt8Type  |            Integer/int            | unsigned char  | 1 byte  |   
| @UInt16Type |            Integer/int            | unsigned short | 2 bytes |   
| @UInt32Type |             Long/long             |  unsigned int  | 4 bytes |   
| @UInt64Type |            BigInteger             | unsigned long  | 8 bytes |  
| @StringType | String/StringBuilder/StringBuffer |       --       | N bytes |   
|  @TimeType  |  Timestamp/Date/Calendar/Instant  |      long      | 8 bytes |  
|  @EnumType  |               enum                |      enum      | 1 bytes |


### *2.2 Compound Data Type Annotations*

|      Annotation       |               Java                |     C/C++      |   Size    |
|:---------------------:|:---------------------------------:|:--------------:|:---------:|
|      @StringType      | String/StringBuilder/StringBuffer |       --       |  N bytes  |   
|       @TimeType       |  Timestamp/Date/Calendar/Instant  |      long      |  8 bytes  |  
|       @EnumType       |               enum                |      enum      |  1 bytes  |


### *2.3 Array Data Type Annotations*

|    Annotation    |                                       Java                                        |      C/C++       |
|:----------------:|:---------------------------------------------------------------------------------:|:----------------:|
|   @BinaryType    |                       Byte[]/byte[]/Collection&lt;Byte&gt;                        |      char[]      |
|  @BoolArrayType  |                    Boolean[]/boolean[]/Collection&lt;Boolean&gt;                     |      bool[]      |
|  @Int8ArrayType  |  Byte[]/byte[]/Integer[]/int[]/Collection&lt;Byte&gt;/Collection&lt;Integer&gt;   |      char[]      |
| @Int16ArrayType  | Short[]/short[]/Integer[]/int[]/Collection&lt;Short&gt;/Collection&lt;Integer&gt; |     short[]      |
| @Int32ArrayType  |                     Integer[]/int[]/Collection&lt;Integer&gt;                     |      int[]       |
| @Int64ArrayType  |                       Long[]/long[]/Collection&lt;Long&gt;                        |      long[]      |
| @UInt8ArrayType  |                     Integer[]/int[]/Collection&lt;Integer&gt;                     | unsigned char[]  |
| @UInt16ArrayType |                     Integer[]/int[]/Collection&lt;Integer&gt;                     | unsigned short[] |
| @UInt32ArrayType |                       Long[]/long[]/Collection&lt;Long&gt;                        |  unsigned int[]  |
| @UInt64ArrayType |                     BigInteger[]/Collection&lt;BigInteger&gt;                     | unsigned long[]  |
| @FloatArrayType  |                      Float[]/float[]/Collection&lt;Float&gt;                      |     float[]      |
| @DoubleArrayType |                    Double[]/double[]/Collection&lt;Double&gt;                     |     double[]     |


### *2.4 Supplementary Annotations*

FastProto also provides some auxiliary annotations to help users further customize the binary format, decoding and encoding process.

|    Annotation    | Scope |              Description              |
|:----------------:|:-----:|:-------------------------------------:|
|  @DefaultEndian  | Class | Endianness, default as little endian. |
| @DecodingIgnore  | Field |    Ignore the field when decoding.    |
| @EncodingIgnore  | Field |    Ignore the field when encoding.    |
|   @FixedLength   | Class |   Enable fixed length of datagram.    |
| @DecodingFormula | Field |           Decoding formula.           |
| @EncodingFormula | Field |           Encoding formula.           |
|    @AutoType     | Field |           Use default type.           |


#### *2.4.1 Endianness*

FastProto uses little endian by default. You can modify the global endian through `@DefaultEndian` annotation, or you can 
modify the endian of specific field through `endian` attribute which has a higher priority.

```java
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.DefaultEndian;

@DefaultEndian(EndianPolicy.BIG)
public class Weather {
    @UInt16Type(offset = 10, endian = EndianPolicy.LITTLE)
    int humidity;

    @UInt32Type(offset = 14)
    long pressure;
}
```

#### *2.4.2 Decoding and Encoding Formula*

Users can customize formula in two ways. For simple formulas, it is recommended to use Lambda expression, while for more 
complex formula, it is recommended to customize formula classes by implementing the `java.lang.function.Function` interface.

* *Lambda Expression*

```java
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.EncodingFormula;

public class Weather {
    ...

    @UInt32Type(offset = 14)
    @DecodingFormula(lambda = "x -> x * 0.1")           // pressure after parsing equals uint32 * 0.1
    @EncodingFormula(lambda = "x -> (long) (x * 10)")   // Data written into binary equals (pressure * 0.1) cast to long
    double pressure;
}

```

* *Custom Formula Class*

```java
import java.util.function.Function;

public class PressureDecodeFormula implements Function<Long, Double> {
    @Override
    public Double apply(Long value) {
        return value * 0.1;
    }
}
```

```java
import java.util.function.Function;

public class PressureEncodeFormula implements Function<Double, Long> {
    @Override
    public Long apply(Double value) {
        return (long) (value * 10);
    }
}
```

```java
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.EncodingFormula;

public class Weather {
    ...

    @UInt32Type(offset = 14)
    @DecodingFormula(PressureDecodeFormula.class)
    @EncodingFormula(PressureEncodeFormula.class)
    double pressure;
}
```

Users can specify only the encoding formula or only the decoding formula as needed. If both lambda expression and custom 
formula class are specified, the latter has a higher priority.

#### *2.4.3 AutoType*

FastProto can automatically infer type if field is annotated by `@AutoType`.

```java
import org.indunet.fastproto.annotation.AutoType;

public class Weather {
    @AutoType(offset = 10, endian = EndianPolicy.LITTLE)
    int humidity;   // default Int32Type

    @AutoType(offset = 14)
    long pressure;  // default Int64Type
}
```


#### *2.4.4 Ignore Field*
In special cases, if you want to ignore certain fields during parsing, or ignore certain fields during packaging, 
you can use `@DecodingIgnore` and `@EncodingIgnore`.

```java
import org.indunet.fastproto.annotation.*;

public class Weather {
    @DecodingFormula
    @Int16Type(offset = 10)
    int humidity;   // ignore when parsing

    @EncodingIgnore
    @Int32Type(offset = 14)
    long pressure; // ignore when packaging
}
```


## *3. Scala*
FastProto supports case class，but Scala is not fully compatible with Java annotations, so please refer to FastProto as follows.

```scala
import org.indunet.fastproto.annotation.scala._
```

## *4. Parse and Package without Annotations*

In some special cases, developers do not want or cannot use annotations to decorate data objects, for example, data objects 
come from third-party libraries, developers cannot modify the source code, and developers only want to create binary data blocks
in a simple way. FastProto provides simple API to solve the above problems, as follows:

### *4.1 Parse Binary Data*

* *Parse without data object*

```java
boolean f1 = FastProto.parse(bytes)
        .boolType(0, 0)
        .getAsBoolean();
int f2 = FastProto.parse(bytes)
        .int8Type(1)      // Parse signed 8-bit integer data at byte offset 1
        .getAsInt();
int f3 = FastProto.parse(bytes)
        .int16Type(2)     // Parse signed 16-bit integer data at byte offset 2
        .getAsInt();
```

* *Parse with data object*

```java
byte[] bytes = ... // Binary data to be parsed

public class DataObject {
    Boolean f1;
    Integer f2;
    Integer f3;
}

JavaObject obj = FastProto.parse(bytes)
        .boolType(0, 0, "f1")           
        .int8Type(1, "f2")              // Parse signed 8-bit integer data at byte offset 1, field name f2
        .int16Type(2, "f3")
        .mapTo(DataObject.class);       // Map parsing result into Java data object according to the field name
```

### *4.2 Create Binary Data Block*

```java
byte[] bytes = FastProto.toBytes()
        .length(16)             // The length of the binary data block
        .uint8Type(0, 1)        // Write unsigned 8-bit integer data 1 at byte offset 0
        .uint16Type(2, 3, 4)    // Write 2 unsigned 16-bit integer data 3 and 4 consecutively at byte offset 2
        .uint32Type(6, EndianPolicy.BIG, 32)
        .getMap();
```


## *5. Benchmark*

*   windows 11, i7 11th, 32gb
*   openjdk 1.8.0_292
*   binary data of 60 bytes and protocol class of 13 fields

|Benchmark |    Mode  | Samples  | Score | Error  |   Units   |
|:--------:|:--------:|:--------:|:-----:|:------:|:---------:|
| `FastProto::parse` |  throughput   |   10  |  240  | ± 4.6  |  ops/ms   |
| `FastProto::toBytes` | throughput  |   10  |  317  | ± 11.9 |  ops/ms   |


## *6. Build Requirements*

*   Java 1.8+
*   Maven 3.5+


## *7. Contribution*

FastProto has obtained the support of JetBrain Open Source Project, which can provide free license of all product pack for
all core contributors.
If you are interested in this project and want to join and undertake part of the work (development/testing/documentation),
please feel free to contact me via email <deng_ran@foxmail.com>


## *8. License*

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
