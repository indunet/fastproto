## Variable Length and Struct Arrays

### Overview
Variable-length fields are common in binary protocols. FastProto supports them via the `lengthRef` attribute on many annotations, allowing a field to reference a length/count defined elsewhere in the same class. This works for strings, byte arrays, numeric arrays, boolean arrays, and struct arrays.

- Supported annotations (selected): `StringType`, `BinaryType`, `AsciiArrayType`, `CharArrayType`, `BoolArrayType`, `Int8ArrayType`, `Int16ArrayType`, `Int32ArrayType`, `Int64ArrayType`, `UInt8ArrayType`, `UInt16ArrayType`, `UInt32ArrayType`, `UInt64ArrayType`, `FloatArrayType`, `DoubleArrayType`, `StructArrayType`.
- `@AutoType` also supports `lengthRef` so you can write `@AutoType(offset=..., lengthRef="$count")` for arrays/strings/collections.

### Basics: length vs lengthRef
- `length`: a fixed number of elements/bytes.
- `lengthRef`: the name of a field in the same class that holds the length/count; you may optionally prefix it with `$` (e.g. `$count`).
- If both are set, `lengthRef` takes precedence for the effective length.

### Strings with lengthRef
```java
import org.indunet.fastproto.annotation.StringType;

public class Packet {
    @UInt16Type(offset = 0)
    int nameLen;

    // Read nameLen bytes at offset 2 as UTF-8 string
    @StringType(offset = 2, lengthRef = "$nameLen", charset = "UTF-8")
    String name;
}
```

### Arrays with lengthRef
```java
import org.indunet.fastproto.annotation.Int32ArrayType;

public class Packet {
    @UInt16Type(offset = 0)
    int count;

    // Read count int32 elements starting at offset 2 (little endian by default unless overridden)
    @Int32ArrayType(offset = 2, lengthRef = "$count")
    int[] values;
}
```

You can also use wrapper arrays, collections (e.g. `List<Integer>`) or `@AutoType`:
```java
public class Packet {
    @UInt16Type(offset = 0)
    int count;

    @AutoType(offset = 2, lengthRef = "$count")
    List<Integer> values; // inferred as Int32ArrayType
}
```

### Struct arrays (fixed and variable length)
For arrays of structured items, use `@StructArrayType`. Each element is a POJO annotated with FastProto data type annotations.

- Fixed length:
```java
@DefaultByteOrder(ByteOrder.LITTLE)
public class Item {            // 6 bytes per item: UInt16 + Int32
    @UInt16Type(offset = 0) int id;
    @Int32Type(offset = 2) int value;
}

public class PacketFixed {
    @StructArrayType(offset = 0, length = 2, element = Item.class)
    Item[] items;
}
```

- Variable length controlled by a count field:
```java
@DefaultByteOrder(ByteOrder.LITTLE)
public class Item {
    @UInt16Type(offset = 0) int id;
    @Int32Type(offset = 2) int value;
}

public class PacketVar {
    @UInt16Type(offset = 0)
    int count;

    // Read count items starting at offset 2
    @StructArrayType(offset = 2, element = Item.class, lengthRef = "$count")
    List<Item> items;
}
```

### Tips and caveats
- Ensure the referenced length field is decoded before the variable-length field (i.e., it appears earlier in the class with a lower offset).
- `lengthRef` accepts optional leading `$` and is case-sensitive with respect to the Java field name.
- For strings, you can also set `charset`.
- For boolean bit arrays, use `BoolArrayType` (byte-aligned). Per-bit packed variable-length booleans are not supported.
- With `@AutoType`, you must still supply `offset` and `lengthRef` for arrays/strings; FastProto infers only the data type, not the layout.

### See also
- Arrays & Strings: `docs/arrays-and-strings.md`
- Annotation Mapping: `docs/annotation-mapping.md`
- Byte and Bit Order: `docs/byte-and-bit-order.md` 