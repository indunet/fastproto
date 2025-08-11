# Arrays & Strings

A focused guide on mapping strings and arrays: how `offset` and `length` work, element endianness, and common pitfalls.

## Overview

Strings and arrays are fixed-length in FastProto. You declare the starting byte offset and an explicit length (in bytes for strings, in elements for arrays). During encode/decode, FastProto reads/writes exactly that span.

## Quick Steps

1. Decide the start byte `offset` for the field.
2. For strings, specify `length` (bytes). For arrays, specify `length` (elements).
3. If numeric array elements need a specific endianness, set `byteOrder` on the array annotation.
4. Verify the declared region fits entirely in your buffer.

## Core Concepts

- **Fixed length**: Strings/arrays are not auto‑sized; the declared `length` is authoritative.
- **Padding/Truncation**: Producers should zero‑pad unused bytes in strings. On encode, longer content than `length` will be truncated to fit.
- **Encoding**: Choose a consistent character encoding at the system boundary (e.g., ASCII/UTF‑8) and ensure both sides agree.
- **Element endianness**: For numeric arrays, `byteOrder` applies per element.

## Reference

### String
```java
@StringType(offset = <byteOffset>, length = <byteLength>)
```
- `offset`: first byte index
- `length`: number of bytes to read/write for the string

### Byte/Binary array
```java
@BinaryType(offset = <byteOffset>, length = <byteLength>)
```
- Raw bytes, no element endianness

### Numeric arrays (examples)
```java
@UInt8ArrayType(offset = <byteOffset>, length = <count>)
@UInt16ArrayType(offset = <byteOffset>, length = <count>, byteOrder = {ByteOrder.BIG})
@UInt32ArrayType(offset = <byteOffset>, length = <count>, byteOrder = {ByteOrder.LITTLE})
@UInt64ArrayType(offset = <byteOffset>, length = <count>, byteOrder = {ByteOrder.BIG})
```
- `length`: number of elements
- `byteOrder`: per‑element endianness

## Examples

### Fixed‑length string
```java
import org.indunet.fastproto.annotation.*;

public class Msg {
  @StringType(offset = 0, length = 16)
  String name; // fixed 16 bytes, producer should zero‑pad
}
```

### Raw bytes
```java
public class Payload {
  @BinaryType(offset = 16, length = 32)
  byte[] body; // 32 bytes of opaque data
}
```

### Unsigned 16‑bit array (big‑endian elements)
```java
import org.indunet.fastproto.ByteOrder;

public class Data {
  @UInt16ArrayType(offset = 0, length = 4, byteOrder = {ByteOrder.BIG})
  int[] values; // 4 elements, each 2 bytes in big‑endian
}
```

### Mixed structure
```java
public class Frame {
  @StringType(offset = 0, length = 8)
  String topic;

  @UInt8ArrayType(offset = 8, length = 12)
  int[] flags; // 12 bytes
}
```

## Advanced

- **Variable‑length data**: If your protocol uses dynamic lengths, carry the size in another field and compute buffer layout in your application before calling `encode(object, totalLength)`. Arrays/strings still need a concrete `length` at mapping time.
- **Reverse addressing**: For layouts that reference the tail, use fixed total length (`FastProto.encode(obj, length)`) so trailing regions can be addressed safely.

## Best Practices & Pitfalls

- Ensure declared regions do not overlap and fit within the buffer.
- Align offsets deliberately; leave commented gaps for reserved bytes.
- Keep encoding consistent across systems; avoid mixing encodings.
- For numeric arrays, always specify `byteOrder` explicitly if your spec requires it.
