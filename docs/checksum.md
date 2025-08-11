# Checksum / CRC

A practical guide to defining and validating checksums in FastProto.

## Overview

FastProto provides a single annotation `@Checksum` to describe:
- The data range to compute over
- Where to store/read the checksum
- Which algorithm and byte order to use for the stored value

During encode, FastProto computes and writes the checksum automatically. During decode, it recomputes and validates.

## Quick Steps

1. Identify the data span: `start` (inclusive) and `length` (bytes).
2. Decide the storage location: `offset` where the checksum value resides.
3. Choose an algorithm `type` and value `byteOrder`.
4. Ensure the data span does not overlap the checksum storage bytes.

## Core Concepts

- Data range: `[start, start + length)` (checksum value itself is excluded).
- Storage: `offset` points to the first byte of the checksum field.
- Byte order: affects how the checksum value is written/read for multi‑byte types; it does not change the computation over the data bytes.
- Width: implied by `type` (CRC8=1B, CRC16=2B, CRC32=4B, CRC64=8B).

## Annotation

```java
@Checksum(start = <start>, length = <length>, offset = <storeOffset>, type = <Type>, byteOrder = <ByteOrder>)
```
- `start`: start byte index (inclusive)
- `length`: number of bytes to include
- `offset`: where to write/read the checksum value
- `type`: algorithm, e.g. `CRC8`, `CRC8_SMBUS`, `CRC8_MAXIM`, `CRC16`, `CRC16_MODBUS`, `CRC16_CCITT`, `CRC32`, `CRC32C`, `CRC64_ECMA182`, `CRC64_ISO`, `XOR8`, `LRC8`
- `byteOrder`: affects only how the checksum value is written/read (multi-byte), not the calculation

## Supported Algorithms

| Type            | Width | Polynomial           | Init               | Refin/Refout | XorOut              | Notes                          |
|:----------------|:-----:|:---------------------|:-------------------|:------------:|:--------------------|:-------------------------------|
| CRC8            |  8    | 0x07                 | 0x00               | false/false  | 0x00                | SMBus-PEC base                 |
| CRC8_SMBUS      |  8    | 0x07                 | 0x00               | false/false  | 0x00                | Alias of CRC8(0x07,0x00)       |
| CRC8_MAXIM      |  8    | 0x31 (reflected 0x8C)| 0x00               | true/true    | 0x00                | Dallas/Maxim                    |
| XOR8            |  8    | —                    | —                  | —            | —                   | Byte-wise XOR (BCC)            |
| LRC8            |  8    | —                    | —                  | —            | —                   | Two's complement of sum        |
| CRC16 (IBM)     | 16    | 0x8005               | 0x0000             | true/true    | 0x0000              | Also known as CRC-16/IBM       |
| CRC16_MODBUS    | 16    | 0x8005               | 0xFFFF             | true/true    | 0x0000              | MODBUS RTU                     |
| CRC16_CCITT     | 16    | 0x1021               | 0xFFFF             | false/false  | 0x0000              | CCITT-FALSE                    |
| CRC32 (IEEE)    | 32    | 0x04C11DB7           | 0xFFFFFFFF         | true/true    | 0xFFFFFFFF          | Ethernet/ZIP                   |
| CRC32C          | 32    | 0x1EDC6F41           | 0xFFFFFFFF         | true/true    | 0xFFFFFFFF          | Castagnoli                     |
| CRC64_ECMA182   | 64    | 0x42F0E1EBA9EA3693   | 0x0000000000000000 | false/false  | 0x0000000000000000  | ECMA-182                       |
| CRC64_ISO       | 64    | 0x000000000000001B   | 0xFFFFFFFFFFFFFFFF | true/true    | 0xFFFFFFFFFFFFFFFF  | ISO                            |

## Example (CRC16 Little-Endian)
```java
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.*;

public class Packet {
  @UInt8Type(offset = 0) int b1;
  @UInt8Type(offset = 1) int b2;
  @UInt8Type(offset = 2) int b3;
  @UInt8Type(offset = 3) int b4;
  @UInt8Type(offset = 4) int b5;

  @Checksum(start = 0, length = 5, offset = 5, type = Checksum.Type.CRC16, byteOrder = ByteOrder.LITTLE)
  int crc16;
}
```

## Utilities

You can also compute checksums without annotations:
```java
import org.indunet.fastproto.annotation.Checksum;
import org.indunet.fastproto.checksum.ChecksumUtils;

byte[] bytes = new byte[]{0x31,0x32,0x33,0x34,0x35};
long v1 = ChecksumUtils.calculate(bytes, 0, 5, Checksum.Type.CRC16);
int  v2 = ChecksumUtils.crc16(bytes) & 0xFFFF;
```

## Best Practices
- Keep `[start, start+length)` disjoint from the checksum storage bytes `[offset, offset+width)`.
- Choose `byteOrder` to match the on‑wire representation of the checksum value.
- Prefer a single `@Checksum` per checksum field; avoid overlapping ranges.
- Add tests with known vectors to ensure both encode and decode paths are validated.

## Troubleshooting
- If decode throws `DecodingException`, recompute the expected checksum and compare against the stored bytes with the specified `byteOrder`.
- Confirm the buffer is long enough to include the entire range and checksum field.
