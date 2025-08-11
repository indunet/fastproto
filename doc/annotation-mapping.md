# Annotation Mapping

A practical guide to map binary data to Java fields using FastProto annotations.

## Overview

FastProto uses annotations placed on fields to describe how bytes map to values. During decode/encode, these annotations drive the binary-to-object conversion.

## Quick Mapping Steps

1. Identify each field’s byte offset (and bit offset if needed).
2. Pick the proper data type annotation (e.g., `@UInt16Type`).
3. Specify endianness only when it deviates from your class/global default.
4. For strings/arrays, specify `length`.
5. Validate no overlaps and that total length matches the buffer size.

## Core Concepts

- **Offsets**: `offset` is the start byte index (0‑based). Bit fields use `byteOffset` + `bitOffset`.
- **Byte/Bit Order**: Use `@DefaultByteOrder` / `@DefaultBitOrder` at class level; override per field only when necessary. See [Byte & Bit Order](byte-and-bit-order.md).
- **Lengths**: Strings and arrays require an explicit `length` (fixed-length mapping).

## Reference Tables

### Primitive & Unsigned Types

| Annotation  |               Java                |     C/C++      |  Size   |
|:-----------:|:---------------------------------:|:--------------:|:-------:|
|  @BoolType  |          Boolean/boolean          |      bool      |  1 bit  |
| @AsciiType  |          Character/char           |      char      | 1 byte  |
|  @CharType  |          Character/char           |       --       | 2 bytes |
|  @Int8Type  |       Byte/byte/Integer/int       |      char      | 1 byte  |
| @Int16Type  |     Short/short / Integer/int     |     short      | 2 bytes |
| @Int32Type  |            Integer/int            |      int       | 4 bytes |
| @Int64Type  |             Long/long             |      long      | 8 bytes |
| @UInt8Type  |            Integer/int            | unsigned char  | 1 byte  |
| @UInt16Type |            Integer/int            | unsigned short | 2 bytes |
| @UInt32Type |             Long/long             |  unsigned int  | 4 bytes |
| @UInt64Type |            BigInteger             | unsigned long  | 8 bytes |
| @FloatType  |            Float/float            |     float      | 4 bytes |
| @DoubleType |           Double/double           |     double     | 8 bytes |

### Composite Types

|   Annotation   |               Java                |  C/C++  |  Size   |
|:--------------:|:---------------------------------:|:-------:|:-------:|
|  @StringType   | String/StringBuilder/StringBuffer |   --    | N bytes |
|   @TimeType    |  Timestamp/Date/Calendar/Instant  |  long   | 8 bytes |
|   @EnumType    |               enum                |  enum   | 1 byte  |

### Array Types

|    Annotation    |                                       Java                                        |     C/C++      |
|:----------------:|:---------------------------------------------------------------------------------:|:--------------:|
|   @BinaryType    |                       Byte[]/byte[]/Collection<Byte>                              |     char[]     |
|  @BoolArrayType  |                   Boolean[]/boolean[]/Collection<Boolean>                         |     bool[]     |
| @AsciiArrayType  |                  Character[]/char[]/Collection<Character>                         |     char[]     |
|  @CharArrayType  |                  Character[]/char[]/Collection<Character>                         |      --        |
|  @Int8ArrayType  |  Byte[]/byte[]/Integer[]/int[]/Collection<Byte>/Collection<Integer>               |     char[]     |
| @Int16ArrayType  | Short[]/short[]/Integer[]/int[]/Collection<Short>/Collection<Integer>             |    short[]     |
| @Int32ArrayType  |                     Integer[]/int[]/Collection<Integer>                           |     int[]      |
| @Int64ArrayType  |                        Long[]/long[]/Collection<Long>                             |     long[]     |
| @UInt8ArrayType  |                     Integer[]/int[]/Collection<Integer>                           | unsigned char[]|
| @UInt16ArrayType |                     Integer[]/int[]/Collection<Integer>                           | unsigned short[]|
| @UInt32ArrayType |                        Long[]/long[]/Collection<Long>                             |  unsigned int[]|
| @UInt64ArrayType |                     BigInteger[]/Collection<BigInteger>                           | unsigned long[]|
| @FloatArrayType  |                      Float[]/float[]/Collection<Float>                            |     float[]    |
| @DoubleArrayType |                    Double[]/double[]/Collection<Double>                           |    double[]    |

### Supplementary Annotations

|    Annotation     | Scope |                     Description                      |
|:-----------------:|:-----:|:----------------------------------------------------:|
| @DefaultByteOrder | Class | Default byte order (fallback to little-endian)       |
|  @DefaultBitOrder | Class | Default bit order (fallback to LSB_0)                |
|  @DecodingIgnore  | Field | Ignore the field when decoding                        |
|  @EncodingIgnore  | Field | Ignore the field when encoding                        |
|  @DecodingFormula | Field | Decoding formula (lambda or Function class)           |
|  @EncodingFormula | Field | Encoding formula (lambda or Function class)           |
|      @AutoType    | Field | Infer type automatically (see defaults per Java type) |

## Examples

### Basic object
```java
import org.indunet.fastproto.annotation.*;

public class Weather {
  @UInt8Type(offset = 0) int id;
  @TimeType(offset = 2) java.sql.Timestamp time;
  @UInt16Type(offset = 10) int humidity;
  @Int16Type(offset = 12) int temperature;
  @UInt32Type(offset = 14) long pressure;
}
```

### Nested object
```java
public class Header {
  @UInt8Type(offset = 0) int version;
  @UInt8Type(offset = 1) int flags;
}

public class Frame {
  Header header;               // map a nested class
  @UInt16Type(offset = 2) int length;
}
```

## Advanced: Reverse Addressing

- Encode to a fixed-length buffer via `FastProto.encode(obj, length)` so fields near the end can be addressed safely.

## Best Practices

- Keep offsets aligned and avoid overlaps; leave explicit gaps with comments when reserving bytes.
- Prefer class-level defaults for byte/bit order, override only when needed. See [Byte & Bit Order](byte-and-bit-order.md).
- For checksums, use a single `@Checksum` (start/length/offset). See [Checksum/CRC](checksum.md).
