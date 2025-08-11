package org.indunet.fastproto.checksum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.Checksum;
import org.indunet.fastproto.annotation.UInt32Type;
import org.indunet.fastproto.annotation.UInt64Type;
import org.indunet.fastproto.annotation.UInt8Type;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChecksumAnnotationObject {
    @UInt8Type(offset = 0)
    int b1;
    @UInt8Type(offset = 1)
    int b2;
    @UInt8Type(offset = 2)
    int b3;
    @UInt8Type(offset = 3)
    int b4;
    @UInt8Type(offset = 4)
    int b5;

    // CRC8 stored at 5, computed over [0,5)
    @Checksum(start = 0, length = 5, offset = 5, type = Checksum.Type.CRC8)
    int crc8;

    // CRC8-MAXIM at 6, computed over [0,5)
    @Checksum(start = 0, length = 5, offset = 6, type = Checksum.Type.CRC8_MAXIM)
    int crc8Maxim;

    // CRC16-CCITT at 8..9 (leave 1 pad at 7), little-endian for value storage
    @Checksum(start = 0, length = 5, offset = 8, type = Checksum.Type.CRC16_CCITT, byteOrder = ByteOrder.LITTLE)
    int crc16ccitt;

    // CRC32C at 10..13, big-endian
    @UInt32Type(offset = 10, byteOrder = ByteOrder.BIG)
    @Checksum(start = 0, length = 5, offset = 10, type = Checksum.Type.CRC32C, byteOrder = ByteOrder.BIG)
    long crc32c;

    // CRC64-ECMA at 14..21, big-endian (use BigInteger for storage)
    @UInt64Type(offset = 14, byteOrder = ByteOrder.BIG)
    @Checksum(start = 0, length = 5, offset = 14, type = Checksum.Type.CRC64_ECMA182, byteOrder = ByteOrder.BIG)
    BigInteger crc64ecma;

    // CRC64-ISO at 22..29, little-endian
    @UInt64Type(offset = 22, byteOrder = ByteOrder.LITTLE)
    @Checksum(start = 0, length = 5, offset = 22, type = Checksum.Type.CRC64_ISO, byteOrder = ByteOrder.LITTLE)
    BigInteger crc64iso;
}
