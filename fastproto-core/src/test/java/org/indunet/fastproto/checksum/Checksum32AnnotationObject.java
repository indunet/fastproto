package org.indunet.fastproto.checksum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.Checksum;
import org.indunet.fastproto.annotation.UInt8Type;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Checksum32AnnotationObject {
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

    // CRC32 big-endian stored at 5..8, computed over [0,5)
    @Checksum(start = 0, length = 5, offset = 5, type = Checksum.Type.CRC32, byteOrder = ByteOrder.BIG)
    long crc;
}
