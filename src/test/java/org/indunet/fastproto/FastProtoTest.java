package org.indunet.fastproto;

import org.indunet.fastproto.domain.AirConditioner;
import org.indunet.fastproto.domain.Tesla;
import org.junit.Test;

public class FastProtoTest {
    byte[] datagram = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

    @Test
    public void testDecode() {
        Tesla tesla = FastProto.decode(datagram, Tesla.class);
        System.out.println(tesla.toString());
    }

    @Test
    public void testEncode() {
        Tesla tesla = new Tesla();
        AirConditioner airConditioner = new AirConditioner();

        airConditioner.setIndoorTemperature(23.0);
        tesla.setAirConditioner(airConditioner);

        byte[] datagram = new byte[100];
        tesla.setMileage(100l);
        FastProto.encode(tesla, datagram);

        System.out.println(datagram);
    }
}
