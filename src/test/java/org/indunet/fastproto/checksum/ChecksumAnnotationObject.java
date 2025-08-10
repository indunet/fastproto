package org.indunet.fastproto.checksum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.Checksum;
import org.indunet.fastproto.annotation.UInt8Type;

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
    int crc;
}
