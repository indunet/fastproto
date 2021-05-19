package org.indunet.fastproto;

import org.indunet.fastproto.domain.Tesla;
import org.junit.Test;

public class FastProtoTest {
    byte[] datagram = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

    @Test
    public void testDecode() {
        Tesla tesla = FastProto.decode(datagram, Tesla.class);
        System.out.println(tesla.toString());
    }
}
