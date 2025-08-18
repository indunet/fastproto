## Quick Start

FastProto lets you map binary protocols to plain Java classes using annotations, then decode/encode with one-liners. This guide shows the essentials.

## Installation

Add the dependency via Maven or Gradle.

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>3.12.0</version>
</dependency>
```

```gradle
implementation "org.indunet:fastproto:3.12.0"
```

## Define Your Protocol Model

Suppose you receive a 20-byte packet from a weather device. Annotate a Java class with offsets to map fields to bytes/bits.

```java
import java.sql.Timestamp;
import org.indunet.fastproto.annotation.BoolType;
import org.indunet.fastproto.annotation.TimeType;
import org.indunet.fastproto.annotation.UInt16Type;
import org.indunet.fastproto.annotation.Int16Type;
import org.indunet.fastproto.annotation.UInt32Type;

public class Weather {
    @org.indunet.fastproto.annotation.UInt8Type(offset = 0)
    int id;

    @TimeType(offset = 2)
    Timestamp time;

    @UInt16Type(offset = 10)
    int humidity;          // %RH

    @Int16Type(offset = 12)
    int temperature;       // ℃

    @UInt32Type(offset = 14)
    long pressureRaw;      // Pa (raw)

    @BoolType(byteOffset = 18, bitOffset = 0)
    boolean deviceValid;
}
```

## Decode and Encode Data

```java
import org.indunet.fastproto.FastProto;

byte[] datagram = /* 20 bytes from device */ new byte[20];

// Decode bytes into a Weather object
Weather weather = FastProto.decode(datagram, Weather.class);

// Encode object back to bytes (explicit length optional)
byte[] out = FastProto.encode(weather, 20);
```

## Transform Values with Formulas

Use formulas to transform values on the fly (e.g., convert raw pressure to engineering units).

```java
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.EncodingFormula;

public class WeatherWithPressure {
    @org.indunet.fastproto.annotation.UInt32Type(offset = 14)
    @DecodingFormula(lambda = "x -> x * 0.1")          // raw -> Pa*0.1
    @EncodingFormula(lambda = "x -> (long) (x * 10)")  // Pa -> raw
    double pressure;                                    // Pa
}
```

For complex logic, implement `java.util.function.Function` and reference the class. See `docs/formulas.md`.

## Configure Byte and Bit Order

Set defaults at the class level and override per field as needed.

```java
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.DefaultBitOrder;
import org.indunet.fastproto.annotation.DefaultByteOrder;
import org.indunet.fastproto.annotation.UInt16Type;
import org.indunet.fastproto.annotation.BoolType;

@DefaultByteOrder(ByteOrder.BIG)
@DefaultBitOrder(BitOrder.LSB_0)
public class WeatherOrderDemo {
    @UInt16Type(offset = 10, byteOrder = ByteOrder.LITTLE)
    int humidity;

    @BoolType(byteOffset = 18, bitOffset = 0, bitOrder = BitOrder.MSB_0)
    boolean deviceValid;
}
```

Details: `docs/byte-and-bit-order.md`.

## Checksum/CRC with a Single Annotation

Compute and store checksums automatically during encode, and validate on decode.

```java
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.Checksum;
import org.indunet.fastproto.annotation.UInt8Type;

public class Packet {
    @UInt8Type(offset = 0) int b1;
    @UInt8Type(offset = 1) int b2;
    @UInt8Type(offset = 2) int b3;
    @UInt8Type(offset = 3) int b4;
    @UInt8Type(offset = 4) int b5;

    // Compute CRC16 over [0,5) and write LE result at offsets 5..6
    @Checksum(start = 0, length = 5, offset = 5, type = Checksum.Type.CRC16, byteOrder = ByteOrder.LITTLE)
    int crc16;
}
```

Utilities are also available: `docs/checksum.md`.

## Use FastProto Without Annotations (Builder/Utils)

Decode directly into a POJO by field names:

```java
class DataObject { Boolean f1; Integer f2; Integer f3; }

byte[] bytes = /* incoming data */ new byte[16];

DataObject obj = org.indunet.fastproto.FastProto.decode(bytes)
    .readBool("f1", 0, 0)
    .readInt8("f2", 1)
    .readInt16("f3", 2)
    .mapTo(DataObject.class);
```

Create binary blocks imperatively:

```java
import org.indunet.fastproto.ByteOrder;

byte[] out = org.indunet.fastproto.FastProto.create(16)
    .writeInt8(0, 1)
    .writeUInt16(2, 3, 4)
    .writeUInt32(6, ByteOrder.BIG, 256)
    .get();
```

Or via utility methods:

```java
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.util.EncodeUtils;

byte[] buf = new byte[16];
EncodeUtils.writeInt8(buf, 0, 1);
EncodeUtils.writeUInt16(buf, 2, 3, 4);
EncodeUtils.writeUInt32(buf, 6, ByteOrder.BIG, 256);
```

More: `docs/without-annotations.md`. For Android notes, see `docs/android.md`. 