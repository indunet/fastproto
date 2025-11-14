# Expect Assertions

Declare constant values that must appear at fixed offsets in a packet.

On decode, FastProto verifies these constants; on mismatch it throws a DecodingException. On encode, FastProto writes these constants into the output buffer automatically.

## When to use

- Magic byte/header guards (e.g., 0xA5, 0xAA55, 0x55AA7E...)
- Fixed protocol version numbers at known offsets
- Framing flags that must be present before decoding the rest

Use @Expect for simple fixed-value checks/writes. If you need a computed or context‑dependent rule, keep using @DecodingFormula.

## Annotations

- @Expect: declare one assertion
- @Expects: container for multiple @Expect on the same field (repeatable support)

```java
import org.indunet.fastproto.annotation.Expect;
import org.indunet.fastproto.annotation.Expects;
import org.indunet.fastproto.ByteOrder;

public class Packet {
  // Single-byte magic 0xA5 at offset 0
  @Expect(offset = 0, value = 0xA5, size = 1)
  transient int _magic;

  // Two-byte header 0xAA 0x55 at offset 1
  @Expect(offset = 1, bytes = {(byte)0xAA, (byte)0x55})
  transient int _header;

  // Four-byte version 0x00010002 at offset 3 (little-endian)
  @Expect(offset = 3, value = 0x00010002L, size = 4, byteOrder = ByteOrder.LITTLE)
  transient int _version;

  // Multiple assertions in one place
  @Expects({
    @Expect(offset = 7, value = 0x7E, size = 1),
    @Expect(offset = 8, bytes = {(byte)0xAA, (byte)0xBB, (byte)0xCC})
  })
  transient int _expects;
}
```

## Parameters

- offset: byte offset for assertion/read/write
- bytes: exact bytes to assert/write (takes precedence when non-empty)
- value: unsigned integer to assert/write when bytes is empty
- size: size (bytes) of value when using value; valid: 1, 2, 4, 8
- byteOrder: byte order for multi-byte values (default LITTLE)

Either bytes or (value + size) must be provided.

## Behavior

- Decode:
  - All @Expect attached to the class (via its fields) are verified.
  - Any mismatch throws DecodingException; decoding stops.
- Encode:
  - All @Expect are written into the output buffer at their specified offsets.

### Multiple @Expect on the same field

- Semantics: AND. All assertions must be satisfied on decode.
- Overlap: if write ranges overlap during encode, later declarations override earlier ones (by source order).
  - Recommendation: avoid conflicting ranges or declare identical values when overlapping.

## Flow order & interaction

- ExpectFlow runs both in encode and decode pipelines before ChecksumFlow.
  - Encode: constants are written first, then checksums are calculated and written.
  - Decode: constants are verified first, then checksums are verified.

This ensures magic/version bytes participate in checksum as they should.

## Comparison with DecodingFormula

- @Expect: fixed constants at fixed offsets, no dependency on the decoded field value.
- @DecodingFormula: general transform/validation after a field is decoded (can throw DecodingException).

Pick @Expect for static guards; pick @DecodingFormula for computed or context-based rules.

## Tips

- Keep guard bytes near offset 0 for faster fail-fast checks.
- Prefer bytes for multi-byte signatures; prefer value+size for integer constants (with explicit byteOrder).


