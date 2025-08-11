# Byte & Bit Order

A practical guide to endianness (byte order) and bit order, how they are applied in FastProto, and how to avoid common mistakes.

## Overview

- Byte order controls how multi‑byte values are laid out in memory (e.g., UInt16/UInt32/UInt64, Int16/Int32/Int64).
- Bit order controls how bit positions within a byte are interpreted by bit‑level annotations (e.g., `@BoolType`).
- Set sensible defaults on the class, override per field only when needed.

## Quick Steps

1. Check your protocol spec for endianness and bit numbering conventions.
2. Set class‑level defaults: `@DefaultByteOrder(...)`, `@DefaultBitOrder(...)`.
3. Add per‑field `byteOrder` only where the spec deviates from the default.
4. For bit flags, choose the `bitOrder` that matches the spec (`LSB_0` or `MSB_0`).
5. Validate with known vectors (round‑trip encode/decode tests).

## Core Concepts

### Byte Order
- Global default: little‑endian.
- Precedence: Field `byteOrder` > Class `@DefaultByteOrder` > Global default (LITTLE).
- Affects only multi‑byte storage/reading; single‑byte types (`UInt8`, `Int8`, `Ascii/Char`) are unaffected.
- Checksum note: `@Checksum.byteOrder` affects how the checksum value is stored/read; it does not change the calculation over the data range. See [Checksum / CRC](checksum.md).

### Bit Order
- Global default: `LSB_0` (bit 0 is the least‑significant bit of the byte).
- You can override at class level via `@DefaultBitOrder(...)` or per field via `bitOrder`.
- Applies to bit‑level annotations like `@BoolType(byteOffset = x, bitOffset = y, bitOrder = ...)`.

## Examples

### Class‑level BIG endianness with per‑field LITTLE override
```java
import org.indunet.fastproto.*;
import org.indunet.fastproto.annotation.*;

@DefaultByteOrder(ByteOrder.BIG)
public class Example {
  @UInt16Type(offset = 0)             // BIG (class default)
  int a;

  @UInt16Type(offset = 2, byteOrder = ByteOrder.LITTLE) // field override
  int b;
}
```

### Bit flags with MSB_0
```java
import org.indunet.fastproto.*;
import org.indunet.fastproto.annotation.*;

@DefaultBitOrder(BitOrder.MSB_0)
public class Flags {
  @BoolType(byteOffset = 0, bitOffset = 0) // topmost bit is bit 0
  boolean valid;

  @BoolType(byteOffset = 0, bitOffset = 7) // least‑significant bit in MSB_0
  boolean lowFlag;
}
```

### Array with per‑element endianness
```java
import org.indunet.fastproto.*;
import org.indunet.fastproto.annotation.*;

public class Vector {
  @UInt32ArrayType(offset = 0, length = 3, byteOrder = {ByteOrder.LITTLE})
  long[] points; // each element is 4 bytes, stored little‑endian
}
```

## Advanced

- Network byte order is BIG‑endian; many industrial protocols mix endianness across fields—prefer class‑level defaults and minimal overrides.
- Reverse addressing (fixed total length) does not change endianness—only index origin.
- For checksums, the `byteOrder` only affects the stored checksum field’s byte order, not the CRC computation over data bytes.

## Best Practices

- Set class‑level defaults to reduce noise and mistakes.
- Keep endianness consistent across related fields where possible.
- Document any per‑field overrides with comments that reference the spec.
- Add unit tests with known byte vectors to lock in expected ordering.
