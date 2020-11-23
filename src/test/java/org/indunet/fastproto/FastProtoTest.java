package org.indunet.fastproto;

import org.indunet.fastproto.object.Motor;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

public class FastProtoTest {
    FastProto fastProto = new FastProto();
    byte[] datagram = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};
    Motor motor = new Motor();

    @Test
    public void testDecode() throws InvocationTargetException, IllegalAccessException {
        fastProto.decode(datagram, motor);

        System.out.print(motor);
    }
}
