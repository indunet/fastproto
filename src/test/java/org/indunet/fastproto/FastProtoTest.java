package org.indunet.fastproto;

import org.indunet.fastproto.entity.Tesla;
import org.junit.Test;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FastProtoTest {
    FastProto fastProto = new FastProto();
    byte[] datagram = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

    Tesla tesla = new Tesla();
    //Motor motor = new Motor();

    @Test
    public void testDecode() throws InstantiationException, IllegalAccessException {
        fastProto.decode(datagram, Tesla.class);


        Optional.of(1)
                .flatMap(x -> Optional.of(x + 1))
                .ifPresent(System.out::println);
        // System.out.println(JSON.toJSONString(tesla));

        Stream.of(1, 2, 3)
                .flatMap(i -> IntStream.range(0, i).mapToObj(String::valueOf))
                .forEach(System.out::println);
    }
}
