# Using APIs without Annotations

Operate on byte buffers directly when you cannot or do not want to decorate classes with annotations.

## Overview

FastProto offers two complementary ways to work without annotations:
- Chain APIs: fluent builders for decode/encode with field names for later mapping
- Utility methods: static helpers to read/write primitive values

## When to Use
- Data objects come from third‑party libraries and cannot be modified
- You need a quick script/tool to inspect or craft binary frames
- Prototyping before locking a schema

## Quick Steps
1. Decide buffer length and create/obtain the `byte[]`
2. Use chain API or utilities to read/write values at specific offsets
3. If needed, map decoded values to a POJO by matching field names

## Core Concepts
- Addressing is 0‑based byte offsets; bit fields specify `byteOffset` + `bitOffset`
- Endianness: utilities accept `ByteOrder`; chain API variants have overloads too
- Reverse addressing: use a fixed total length when writing near the tail

## Decode Examples

### Chain API: decode then map to a class
```java
byte[] bytes = ...; // input buffer

class Obj { Boolean f1; Integer f2; Integer f3; }

Obj obj = FastProto.decode(bytes)
  .readBool("f1", 0, 0)
  .readInt8("f2", 1)
  .readInt16("f3", 2)
  .mapTo(Obj.class);
```

### Utilities: read directly
```java
import org.indunet.fastproto.util.DecodeUtils;
import org.indunet.fastproto.ByteOrder;

byte[] bytes = ...;
boolean f1 = DecodeUtils.readBool(bytes, 0, 0);     // bit 0 in byte 0
int     f2 = DecodeUtils.readInt8(bytes, 1);
int     f3 = DecodeUtils.readInt16(bytes, 2, ByteOrder.LITTLE);
long    u32 = DecodeUtils.readUInt32(bytes, 4, ByteOrder.BIG);
```

## Encode Examples

### Chain API: create and write
```java
import org.indunet.fastproto.ByteOrder;

byte[] out = FastProto.create(12)
  .writeInt8(0, 1)
  .writeUInt16(2, 3, 4)                  // two u16 values at 2..5
  .writeUInt32(6, ByteOrder.BIG, 256)    // u32 at 6..9 (big‑endian)
  .get();
```

### Utilities: write directly
```java
import org.indunet.fastproto.util.EncodeUtils;
import org.indunet.fastproto.ByteOrder;

byte[] out = new byte[12];
EncodeUtils.writeInt8(out, 0, 1);
EncodeUtils.writeUInt16(out, 2, 3);                       // little‑endian default
EncodeUtils.writeUInt32(out, 6, ByteOrder.BIG, 256);
```

## Checksums (optional without annotations)
```java
import org.indunet.fastproto.annotation.Checksum;
import org.indunet.fastproto.checksum.ChecksumUtils;

// Compute CRC16 over bytes[0..4] and place it at offset 5..6 (little‑endian) manually
byte[] buf = new byte[]{0x31,0x32,0x33,0x34,0x35,0,0};
long crc = ChecksumUtils.calculate(buf, 0, 5, Checksum.Type.CRC16);
EncodeUtils.writeUInt16(buf, 5, ByteOrder.LITTLE, (int) crc);
```

## Reverse Addressing
- Use `FastProto.encode(object, totalLength)` or `FastProto.create(totalLength)` to safely reference trailing regions by absolute offsets

## Best Practices & Troubleshooting
- Be explicit about `ByteOrder` for multi‑byte values to match your spec
- Validate offsets and lengths; add assertions in tests
- Keep a single source of truth for constant offsets (centralize in one class)
- If decoded values look byte‑swapped, check endianness settings first
- For bit flags, confirm the bit numbering (LSB_0 vs MSB_0) used by the producing system
