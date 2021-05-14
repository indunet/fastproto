package org.indunet.fastproto;

import org.indunet.fastproto.annotation.type.BooleanType;
import org.indunet.fastproto.decoder.BooleanDecoder;
import org.indunet.fastproto.entity.Tesla;
import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FastProtoTest {
    FastProto fastProto = new FastProto();
    byte[] datagram = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

    Tesla tesla = new Tesla();
    //Motor motor = new Motor();

    @Test
    public void testDecode() throws InstantiationException, IllegalAccessException {
        TypeAssist assist = TypeAssist.create(Tesla.class);

        System.out.println(assist);

        Arrays.stream(BooleanDecoder.class.getGenericInterfaces())
                .filter(t -> t instanceof ParameterizedType)
                .map(t -> (ParameterizedType) t)
                .flatMap(t -> Arrays.stream(t.getActualTypeArguments()))
                .findFirst();
    }

    public <T, R> Function<T, R> get() {
        return (T x) -> (R) (x);
    }
}
