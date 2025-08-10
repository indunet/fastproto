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
    protected static final CRC16 CRC16_INSTANCE = new CRC16();
    protected static final CRC32 CRC32_INSTANCE = new CRC32();

    public static int crc8(byte[] data) {
        return CRC8_INSTANCE.calculate(data);
    }
    public static int crc16(byte[] data) {
        return CRC16_INSTANCE.calculate(data);
    }

    public static int crc32(byte[] data) {
        return CRC32_INSTANCE.calculate(data);
    }

    /** Calculate checksum for specific range. */
    public static long calculate(byte[] data, int offset, int length, org.indunet.fastproto.annotation.Checksum.Type type) {
        byte[] slice = new byte[length];
        
        System.arraycopy(data, offset, slice, 0, length);
        
        switch (type) {
            case CRC8:
                return crc8(slice) & 0xFF;
            case CRC16:
                return crc16(slice) & 0xFFFF;
            case CRC32:
                return crc32(slice) & 0xFFFFFFFFL;
            default:
                throw new CodecException("Unsupported checksum type: " + type);
        }
    }
}