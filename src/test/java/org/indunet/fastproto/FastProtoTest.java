package org.indunet.fastproto;

import com.alibaba.fastjson.JSON;
import org.indunet.fastproto.domain.Tesla;
import org.junit.Test;

public class FastProtoTest {
    FastProto fastProto = new FastProto();
    byte[] datagram = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

    Tesla tesla = new Tesla();
    //Motor motor = new Motor();

    @Test
    public void testDecode() throws InstantiationException, IllegalAccessException {
        fastProto.decode(datagram, Tesla.class);

        // System.out.println(JSON.toJSONString(tesla));
    }
}
