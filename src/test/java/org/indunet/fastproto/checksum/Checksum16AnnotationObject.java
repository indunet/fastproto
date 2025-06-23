package org.indunet.fastproto.checksum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.Checksum;
import org.indunet.fastproto.annotation.UInt16Type;
import org.indunet.fastproto.annotation.UInt8Type;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Checksum16AnnotationObject {
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

    @UInt16Type(offset = 5, byteOrder = ByteOrder.LITTLE)
    @Checksum(offset = 0, type = Checksum.Type.CRC16, byteOrder = ByteOrder.LITTLE)
    int crc;
}
