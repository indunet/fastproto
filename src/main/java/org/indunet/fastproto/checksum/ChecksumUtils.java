package org.indunet.fastproto.checksum;

import org.indunet.fastproto.exception.CodecException;

/**
 * CRC utilities for CRC8/CRC16/CRC32.
 * - calculate(data, offset, length, type) operates on [offset, offset + length).
 * - Reuses singleton CRC engines (stateless, thread-safe under current design).
 * - Endianness does not affect calculation; it only applies when reading/writing values elsewhere.
 * - Throws CodecException if an unsupported type is requested.
 *
 * @author Deng Ran
 * @since 3.11.0
 */
public class ChecksumUtils {
    // Reuse single instances to avoid per-call allocations
    protected static final CRC8 CRC8_INSTANCE = new CRC8();
    protected static final CRC8SMBus CRC8_SMBUS_INSTANCE = new CRC8SMBus();
    protected static final CRC8Maxim CRC8_MAXIM_INSTANCE = new CRC8Maxim();
    protected static final BCC BCC_INSTANCE = new BCC();
    protected static final LRC LRC_INSTANCE = new LRC();

    protected static final CRC16 CRC16_INSTANCE = new CRC16();
    protected static final CRC16Modbus CRC16_MODBUS_INSTANCE = new CRC16Modbus();
    protected static final CRC16CCITT CRC16_CCITT_INSTANCE = new CRC16CCITT();

    protected static final CRC32 CRC32_INSTANCE = new CRC32();
    protected static final CRC32C CRC32C_INSTANCE = new CRC32C();

    protected static final CRC64ECMA182 CRC64_ECMA182_INSTANCE = new CRC64ECMA182();
    protected static final CRC64ISO CRC64_ISO_INSTANCE = new CRC64ISO();

    public static int crc8(byte[] data) { return CRC8_INSTANCE.calculate(data); }
    public static int crc8smbus(byte[] data) { return CRC8_SMBUS_INSTANCE.calculate(data); }
    public static int crc8maxim(byte[] data) { return CRC8_MAXIM_INSTANCE.calculate(data); }
    public static int bcc(byte[] data) { return BCC_INSTANCE.calculate(data); }
    public static int lrc(byte[] data) { return LRC_INSTANCE.calculate(data); }

    public static int crc16(byte[] data) { return CRC16_INSTANCE.calculate(data); }
    public static int crc16modbus(byte[] data) { return CRC16_MODBUS_INSTANCE.calculate(data); }
    public static int crc16ccitt(byte[] data) { return CRC16_CCITT_INSTANCE.calculate(data); }

    public static int crc32(byte[] data) { return CRC32_INSTANCE.calculate(data); }
    public static int crc32c(byte[] data) { return CRC32C_INSTANCE.calculate(data); }

    public static long crc64ecma182(byte[] data) { return CRC64_ECMA182_INSTANCE.calculate(data); }
    public static long crc64iso(byte[] data) { return CRC64_ISO_INSTANCE.calculate(data); }

    /** Calculate checksum for specific range. */
    public static long calculate(byte[] data, int offset, int length, org.indunet.fastproto.annotation.Checksum.Type type) {
        byte[] slice = new byte[length];
        System.arraycopy(data, offset, slice, 0, length);

        switch (type) {
            case CRC8:
                return crc8(slice) & 0xFF;
            case CRC8_SMBUS:
                return crc8smbus(slice) & 0xFF;
            case CRC8_MAXIM:
                return crc8maxim(slice) & 0xFF;
            case XOR8:
                return bcc(slice) & 0xFF;
            case LRC8:
                return lrc(slice) & 0xFF;
            case CRC16:
                return crc16(slice) & 0xFFFF;
            case CRC16_MODBUS:
                return crc16modbus(slice) & 0xFFFF;
            case CRC16_CCITT:
                return crc16ccitt(slice) & 0xFFFF;
            case CRC32:
                return crc32(slice) & 0xFFFFFFFFL;
            case CRC32C:
                return crc32c(slice) & 0xFFFFFFFFL;
            case CRC64_ECMA182:
                return crc64ecma182(slice);
            case CRC64_ISO:
                return crc64iso(slice);
            default:
                throw new CodecException("Unsupported checksum type: " + type);
        }
    }
}