package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.type.BooleanType;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class DecodersTest {
    @Test
    public void testGet() {
        Class<? extends TypeDecoder> clazz = Optional.ofNullable(BooleanType.class)
                .map(c -> c.getAnnotation(Decoder.class))
                .map(Decoder::value)
                .get();
        Function func = Decoders.getDecoder(clazz);

        assertNotNull(func);
    }
}
