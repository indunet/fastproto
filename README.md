![fastproto](logo.png "fastproto")

English | [中文](README-zh.md)

# *Fast Protocol*

[![Build Status](https://app.travis-ci.com/indunet/fastproto.svg?branch=master)](https://app.travis-ci.com/indunet/fastproto)
[![codecov](https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU)](https://codecov.io/gh/indunet/fastproto)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ed904d7aacd142f08b5cd50b16b1d74b)](https://www.codacy.com/gh/indunet/fastproto/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=indunet/fastproto&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://img.shields.io/maven-central/v/org.indunet/fastproto.svg?label=Maven%20Central)](https://search.maven.org/artifact/org.indunet/fastproto)
[![License](https://img.shields.io/badge/license-Apache%202.0-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

FastProto is a high-performance serialization/deserialization **library** designed for **binary communication protocols**. By defining complex byte stream structures with simple annotations, it enables Java developers to read and write custom binary packets in the most natural way for scenarios such as IoT, automotive systems, industrial control and energy metering.

## *Design Philosophy*

- **Declarative over imperative:** Protocol fields are defined via annotations, making the structure self‑evident so that code doubles as documentation.
- **Performance with maintainability:** Architectural optimizations such as reflection caching are built in to keep high throughput without sacrificing readability and maintainability.
- **Grounded in engineering reality:** Supports mixed endianness, bit fields, BCD, CRC and other traditional protocol features to integrate smoothly with existing systems.

## *Key Features*

- **Annotation‑driven protocol mapping:** Describe binary layouts with simple field annotations instead of manual offset and bit operations.
- **Rich type & layout support:** Primitives (including unsigned), strings, time types, arrays/collections, variable‑length fields, dynamic offsets and reverse addressing.
- **Precise control of representation:** Configurable byte/bit order, BCD, bit fields and custom encode/decode formulas.
- **Built‑in checksum/CRC:** Single `@Checksum` annotation or utility methods covering common CRC and checksum algorithms.
- **Ecosystem integrations:** Ready‑to‑use Netty codecs and Kafka Serializer/Deserializer/Serde, plus fluent APIs for use without annotations.

See the [CHANGELOG](CHANGELOG.md) for recent updates.

### *Under Development*

* Code structure & performance optimization
* Richer documentation (expanded core feature guides)


### *Documentation*

- Site: [https://indunet.github.io/fastproto/](https://indunet.github.io/fastproto/) (Next.js app in `docs-site/`; each push to `develop` rebuilds and pushes static `out/` to the `gh-pages` branch)
- Guides: [https://indunet.github.io/fastproto/help/quick-start](https://indunet.github.io/fastproto/help/quick-start)

### *Install*

* Maven

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>4.1.0</version>
</dependency>
```


## *1. Quick Start*

This quick example parses a 20‑byte packet sent from a weather device:

>   65 00 7F 69 3D 84 7A 01 00 00 55 00 F1 FF 0D 00 00 00 07 00

It contains eight signals as described below:

| Byte Offset | Bit Offset | Data Type(C/C++)  | Signal Name  | Unit |  Formula  |
|:-----------:|:----------:|:-----------------:|:------------:|:----:|:---------:|
| 0           |            |   unsigned char   |  device id   |      |           |
| 1           |            |                   |   reserved   |      |           |
| 2-9         |            |       long        |     time     |  ms  |           |
| 10-11       |            |  unsigned short   |   humidity   |  %RH |           |
| 12-13       |            |       short       | temperature  |  ℃  |            |
| 14-17       |            |   unsigned int    |   pressure   |  Pa  | p * 0.1   |
| 18          | 0          |       bool        | device valid |      |           |
| 18          | 3-7        |                   |   reserved   |      |           |
| 19          |            |                   |   reserved   |      |           |

Before using FastProto, a typical implementation would manually manage offsets and bit operations, for example:

```java
byte[] datagram = ...;

int id = datagram[0] & 0xFF;

long timeMillis =
        ((long) datagram[2] & 0xFF) |
        (((long) datagram[3] & 0xFF) << 8) |
        (((long) datagram[4] & 0xFF) << 16) |
        (((long) datagram[5] & 0xFF) << 24) |
        (((long) datagram[6] & 0xFF) << 32) |
        (((long) datagram[7] & 0xFF) << 40) |
        (((long) datagram[8] & 0xFF) << 48) |
        (((long) datagram[9] & 0xFF) << 56);
Timestamp time = new Timestamp(timeMillis);

int humidity = (datagram[10] & 0xFF) | ((datagram[11] & 0xFF) << 8);
int temperature = (short) ((datagram[12] & 0xFF) | ((datagram[13] & 0xFF) << 8));
long rawPressure =
        ((long) datagram[14] & 0xFF) |
        (((long) datagram[15] & 0xFF) << 8) |
        (((long) datagram[16] & 0xFF) << 16) |
        (((long) datagram[17] & 0xFF) << 24);
double pressure = rawPressure * 0.1;

boolean deviceValid = (datagram[18] & 0x01) != 0;
```

As the number of fields grows and protocols evolve, this kind of bit‑twiddling code becomes verbose and fragile—offsets, endianness and signedness are easy to get wrong. With FastProto, we can instead express the same protocol declaratively on the `Weather` class using annotations:

### *1.1 Decode and encode Binary Data*

After receiving the packet, convert it into a `Weather` object. Each field is annotated with its byte offset in the data.

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
    boolean deviceValid;
}
```

Use `FastProto::decode()` to parse the packet into a `Weather` object

```java
// datagram sent by monitoring device.
byte[] datagram = ...   

Weather weather = FastProto.decode(datagram, Weather.class);
```

Use `FastProto::encode()` to create the binary packet. The second argument is the byte length—omit it and FastProto will guess.

```java
byte[] datagram = FastProto.encode(weather, 20);
```
### *1.2 Transformation Formula*

The pressure field uses a simple conversion. FastProto provides `@DecodingFormula` and `@EncodingFormula` so this logic can be expressed directly with lambdas. See the [formulas documentation](docs/formulas.md) for details.

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

FastProto supports Java primitive data types, taking into account cross-language and cross-platform data exchange, unsigned types are also introduced. See the [annotation mapping documentation](docs/annotation-mapping.md) for details.

| Annotation  |               Java                |     C/C++      |  Size   |
|:-----------:|:---------------------------------:|:--------------:|:-------:|
|  @BoolType  |          Boolean/boolean          |      bool      |  1 bit  |    
| @AsciiType  |          Character/char           |      char      | 1 bytes |   
|  @CharType  |          Character/char           |       --       | 2 bytes |
|  @Int8Type  |       Byte/byte/Integer/int       |      char      | 1 byte  |  
| @Int16Type  |     Short/short/Integer/int       |     short      | 2 bytes |  
| @Int32Type  |            Integer/int            |      int       | 4 bytes | 
| @Int64Type  |             Long/long             |      long      | 8 bytes |
| @BitFieldType |         Integer/int             |      --        | 1..31 bits |
| @UInt8Type  |            Integer/int            | unsigned char  | 1 byte  |   
| @UInt16Type |            Integer/int            | unsigned short | 2 bytes |   
| @UInt32Type |             Long/long             |  unsigned int  | 4 bytes |   
| @UInt64Type |            BigInteger             | unsigned long  | 8 bytes |
| @FloatType  |            Float/float            |     float      | 4 bytes |  
| @DoubleType |           Double/double           |     double     | 8 bytes |
|  @BcdType   |            Integer/int            |     BCD        | N bytes |


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
|  @BoolArrayType  |                   Boolean[]/boolean[]/Collection&lt;Boolean&gt;                   |      bool[]      |
| @AsciiArrayType  |                  Character[]/char[]/Collection&lt;Character&gt;                   |      char[]      |
|  @CharArrayType  |                  Character[]/char[]/Collection&lt;Character&gt;                   |        --        |
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

|    Annotation     | Scope |                       Description                       |
|:-----------------:|:-----:|:-------------------------------------------------------:|
| @DefaultByteOrder | Class | Default byte order, use little endian if not specified. |
| @DefaultBitOrder  | Class |     Default bit order, use LSB_0 if not specified.      |
|  @DecodingIgnore  | Field |             Ignore the field when decoding.             |
|  @EncodingIgnore  | Field |             Ignore the field when encoding.             |
| @DecodingFormula  | Field |                    Decoding formula.                    |
| @EncodingFormula  | Field |                    Encoding formula.                    |
|     @AutoType     | Field |                    Use default type.                    |
|      @Expect      | Field |      Constant assertion at fixed offset; verify/write   |
|      @Expects     | Field |         Container for multiple @Expect entries          |


#### *2.4.1 Byte Order and Bit Order*

FastProto uses little endian by default. You can modify the global byte order through `@DefaultByteOrder` annotation, or you can modify the byte order of specific field through `byteOrder` attribute which has a higher priority. See the [byte and bit order documentation](docs/byte-and-bit-order.md) for details.

Similarly, FastProto uses LSB_0 by default. You can modify the global bit order through `@DefaultBitOrder` annotation, or you can
modify the bit order of specific field through `bitOrder` attribute which has a higher priority.

```java
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.DefaultBitOrder;
import org.indunet.fastproto.annotation.DefaultByteOrder;

@DefaultByteOrder(ByteOrder.BIG)
@DefaultBitOrder(BitOrder.LSB_0)
public class Weather {
    @UInt16Type(offset = 10, byteOrder = ByteOrder.LITTLE)
    int humidity;

    @BoolType(byteOffset = 18, bitOffset = 0, bitOrder = BitOrder.MSB_0)
    boolean deviceValid;
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
    @AutoType(offset = 10, byteOrder = ByteOrder.LITTLE)
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

### *2.5 Checksum/CRC*

Use `@Checksum` to define start, length and where to store the checksum. FastProto writes the checksum automatically when encoding and validates it when decoding (throws on mismatch). See the [checksum documentation](docs/checksum.md) for details.

- CRC16 (little-endian): compute over [0,5), write at 5..6
```java
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.*;

public class Packet {
    @UInt8Type(offset = 0) int b1;
    @UInt8Type(offset = 1) int b2;
    @UInt8Type(offset = 2) int b3;
    @UInt8Type(offset = 3) int b4;
    @UInt8Type(offset = 4) int b5;

    // Single annotation: start=0, length=5, CRC16 little-endian at 5..6
    @Checksum(start = 0, length = 5, offset = 5, type = Checksum.Type.CRC16, byteOrder = ByteOrder.LITTLE)
    int crc16;
}

Packet p = new Packet();
p.b1 = 0x31; p.b2 = 0x32; p.b3 = 0x33; p.b4 = 0x34; p.b5 = 0x35;
byte[] bytes = FastProto.encode(p, 7);  // write CRC automatically

// Decoding validates CRC automatically (throws DecodingException on mismatch)
Packet q = FastProto.decode(bytes, Packet.class);
```

- Or, compute the checksum directly via utilities (no annotations):
```java
import org.indunet.fastproto.annotation.Checksum;
import org.indunet.fastproto.checksum.ChecksumUtils;

byte[] bytes = new byte[]{0x31,0x32,0x33,0x34,0x35};
long crc = ChecksumUtils.calculate(bytes, /*start=*/0, /*length=*/5, Checksum.Type.CRC16);
// Or:
int crc16 = ChecksumUtils.crc16(bytes) & 0xFFFF;  // compute CRC16 for the whole array
```


## *3. Decode and encode without Annotations*

In some special cases, developers do not want or cannot use annotations to decorate data objects, for example, data objects 
come from third-party libraries, developers cannot modify the source code, and developers only want to create binary data blocks
in a simple way. FastProto provides simple API to solve the above problems, as follows:

### *3.1 Decode Binary Data*

* *Decode with data object*

```java
byte[] bytes = ... // Binary data to be decoded

public class DataObject {
    Boolean f1;
    Integer f2;
    Integer f3;
}

DataObject obj = FastProto.decode(bytes)
        .readBool("f1", 0, 0)       // Decode boolean data at byte offset 0 and bit offset 0
        .readInt8("f2", 1)          // Decode signed 8-bit integer data at byte offset 1
        .readInt16("f3", 2)         // Decode signed 8-bit integer data at byte offset 2
        .mapTo(DataObject.class);   // Map decoded result into Java data object according to the field name
```

* *Decode without data object*

```java
import org.indunet.fastproto.util.DecodeUtils;

byte[] bytes = ... // Binary data to be decoded
        
boolean f1 = DecodeUtils.readBool(bytes, 0, 0); // Decode boolean data at byte offset 0 and bit offset 0
int f2 = DecodeUtils.readInt8(bytes, 1);        // Decode signed 8-bit integer data at byte offset 1
int f3 = DecodeUtils.readInt16(bytes, 2);       // Decode signed 8-bit integer data at byte offset 2
```

### *3.2 Create Binary Data Block*

```java
byte[] bytes = FastProto.create(16)         // Create binary block with 16 bytes 
        .writeInt8(0, 1)                    // Write unsigned 8-bit integer 1 at byte offset 0
        .writeUInt16(2, 3, 4)               // Write 2 unsigned 16-bit integer 3 and 4 consecutively at byte offset 2
        .writeUInt32(6, ByteOrder.BIG, 256)  // Write unsigned 32-bit integer 256 at byte offset 6 with big endian
        .get();
```

```java
import org.indunet.fastproto.util.EncodeUtils;

byte[] bytes = new byte[16];

EncodeUtils.writeInt8(bytes, 0, 1);                     // Write unsigned 8-bit integer 1 at byte offset 0
EncodeUtils.writeUInt16(bytes, 2, 3, 4);                // Write 2 unsigned 16-bit integer 3 and 4 consecutively at byte offset 2
EncodeUtils.writeUInt32(bytes, 6, ByteOrder.BIG, 256);  // Write unsigned 32-bit integer 256 at byte offset 6 with big endian
```


## *4. Benchmark*

- windows 11, Intel Core Ultra 9 275HX, 64GB
- openjdk 11.0.20
- All performance tests were executed in a single-threaded environment, performance scales near-linearly in multi-threaded scenarios. 
- 60-byte binary data; protocol class with 13 fields.

1. API with annotations

|      Benchmark      |   Mode    | Samples | Score |  Error  |  Units  |
|:-------------------:|:---------:|:-------:|:-----:|:-------:|:-------:|
| `FastProto::decode` | throughput |   10    |  239  |  ± 4.6  | ops/ms  |
| `FastProto::encode` | throughput |   10    |  271  | ± 11.9  | ops/ms  |


2. API without annotations

|   Benchmark   |   Mode    | Samples | Score | Error |  Units  |
|:-------------:|:---------:|:-------:|:-----:|:-----:|:-------:|
| `decode`      | throughput |   10    | 1699  |  ± 17 | ops/ms  |
| `create`      | throughput |   10    | 10882 | ± 162 | ops/ms  |


## *6. Build Requirements*

*   Java 1.8+
*   Maven 3.5+


## *7. Contribution*

If you are interested in this project and want to join and undertake part of the work (development/testing/documentation),
please feel free to contact me via email <deng_ran@aliyun.com>

 *FastProto* is not built for profit. In restless moments, writing code brings me calm; if this library helps you, that is the motivation for me to keep refining it.


## *8. License*

FastProto is released under the [Apache 2.0 license](license).

```
Copyright 2019-2025 indunet.org

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

