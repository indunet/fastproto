package org.indunet.fastproto.api.character;

import lombok.Data;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.AsciiType;
import org.indunet.fastproto.annotation.CharType;

/**
 * Data object of unit test
 *
 * @author Deng Ran
 * @since 3.8.4
 */
@Data
public class DataObject {
    @AsciiType(offset = 0)
    char ascii1 = 'a';
    @AsciiType(offset = 1)
    Character ascii2 = 'b';
    @CharType(offset = 2, byteOrder = ByteOrder.LITTLE)
    char char1 = '中';
    @CharType(offset = 4, byteOrder = ByteOrder.BIG)
    Character char2 = '文';

    public byte[] toBytes() {
        byte[] bytes = new byte[6];

        bytes[0] = (byte) this.ascii1;
        bytes[1] = (byte) this.ascii2.charValue();

        bytes[2] = (byte) ('中' & 0xFF);
        bytes[3] = (byte) ('中' >>> 8 & 0xFF);

        bytes[5] = (byte) ('文' & 0xFF);
        bytes[4] = (byte) ('文' >>> 8 & 0xFF);

        return bytes;
    }
}
