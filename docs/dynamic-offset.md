## Dynamic Offset with offsetRef

### Overview
Some formats store an absolute position in a header and place the actual data later in the file. Use `offsetRef` to point a field’s `offset()` to a previously decoded numeric field in the same class. The source field name must be prefixed with `$`.

### Rules
- The referenced field must appear earlier in the class and be decoded before the target.
- The referenced field must be a numeric type.
- Works for most data type annotations that expose `offset()` (booleans use `byteOffset/bitOffset`, not supported).
- If both `offset` and `offsetRef` are provided, `offsetRef` takes precedence (dynamic offset overrides static).
- Validation: At least one of `offset` or `offsetRef` must be specified; otherwise resolving fails.

### Example
```java
public class Packet {
  @UInt16Type(offset = 0)
  int payloadPos;

  // Read 3 bytes starting at dynamic offset = payloadPos
  @BinaryType(offsetRef = "$payloadPos", length = 3)
  byte[] payload;
}
```


