package org.indunet.fastproto.decoder;

import lombok.val;
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
    public void testGetDecoder1() {
        val clazz = Optional.of(BooleanType.class)
                .map(c -> c.getAnnotation(Decoder.class))
                .map(Decoder::value)
                .get();
        val func = Decoders.getDecoder(clazz);

        assertNotNull(func);
    }

    @Test
    public void testGetDecoder2() {
        Class<? extends TypeDecoder> clazz = Optional.of(BooleanType.class)
                .map(c -> c.getAnnotation(Decoder.class))
                .map(Decoder::value)
                .get();
        val func = Decoders.getDecoder(clazz, Formula.class);

        assertNotNull(func);
    }

    @Test
    public void testGetFormula() {
        val func = Decoders.getFormula(Formula.class);

        assertNotNull(func);
    }

    public static class Formula implements Function<Boolean, Integer> {
        @Override
        public Integer apply(Boolean value) {
            return value ? 1 : 0;
        }
    }
}
