package org.indunet.fastproto;

import org.indunet.fastproto.object.Motor;
import org.indunet.fastproto.object.Tesla;
import org.junit.Test;

public class FastProtoTest {
    FastProto fastProto = new FastProto();
    byte[] datagram = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

    Tesla tesla = new Tesla();
    //Motor motor = new Motor();

    @Test
    public void testDecode() {
        fastProto.decode(datagram, tesla);

        System.out.println(tesla);
    }
}
