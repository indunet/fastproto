## Netty Integration

This guide shows how to integrate FastProto with Netty to encode/decode objects from/to ByteBuf.

### Dependency

Add the Netty dependency in your application (FastProto itself declares it as provided):

```xml
<dependency>
  <groupId>io.netty</groupId>
  <artifactId>netty-all</artifactId>
  <version>4.1.x</version>
</dependency>
```

### Codecs

- Inbound: `FastProtoDecoder<T>` converts `ByteBuf` to a target object.
- Outbound: `FastProtoEncoder` converts an object to `ByteBuf`.
- Both can be annotated with `@Sharable` and reused across channels.

Framing (fixed length, length-field, delimiter-based, etc.) should be handled by Netty.

### Example: Fixed 60-Byte Frame

```java
ch.pipeline()
  .addLast(new FixedLengthFrameDecoder(60))
  .addLast(new FastProtoDecoder<>(MyPojo.class))
  .addLast(new FastProtoEncoder());
```

`MyPojo` is a FastProto-annotated class. Example 60-byte layout:

```java
public class MyPojo {
  @Int32Type(offset = 0) int id;
  @Int64Type(offset = 4) long timestamp;
  @StringType(offset = 12, length = 16) String name;
  @BinaryType(offset = 28, length = 32) byte[] payload;
}
```

### Multiple Types

- Use a dedicated "type field" in the frame and route in an inbound handler.
- Alternatively, bind a type per-channel and construct `FastProtoDecoder<>(Type.class)` accordingly.

### Zero-Copy (Optional)

Current implementation copies `ByteBuf` to `byte[]`. A zero-copy path can be added later to read directly from `ByteBuf`.

### Testing

Use `EmbeddedChannel` for unit tests:

```java
EmbeddedChannel ch = new EmbeddedChannel(
  new FixedLengthFrameDecoder(60),
  new FastProtoDecoder<>(MyPojo.class),
  new FastProtoEncoder()
);
``` 