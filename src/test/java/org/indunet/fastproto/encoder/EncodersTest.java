package org.indunet.fastproto.encoder;

import lombok.val;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.type.BooleanType;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class EncodersTest {
    @Test
    public void testGetEncoder() {
        val clazz = Optional.of(BooleanType.class)
                .map(c -> c.getAnnotation(Encoder.class))
                .map(Encoder::value)
                .get();
        val consumer = Encoders.getEncoder(clazz);

        assertNotNull(consumer);
    }

    @Test
    public void testGetFormula() {
        Function func = Encoders.getFormula(Formula.class);

        assertNotNull(func);
    }

    public static class Formula implements Function<Boolean, Integer> {
        @Override
        public Integer apply(Boolean value) {
            return value ? 1 : 0;
        }
    }
}
