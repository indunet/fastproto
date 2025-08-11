# FAQ

## Why does checksum validation fail on decode?
- Ensure the `@Checksum` range `[start, start+length)` excludes the checksum storage bytes.
- Confirm `byteOrder` matches how the device writes multi-byte checksum values.

## How to choose between CRC16 variants?
- MODBUS: field devices/PLC
- CCITT: telecom/embedded
- Keep polynomial/init/refin/refout consistent with the spec.

## Can I use Big-Endian globally?
- Yes. Use `@DefaultByteOrder(ByteOrder.BIG)` and override per field if needed.
