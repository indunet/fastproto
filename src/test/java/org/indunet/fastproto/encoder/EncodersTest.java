package org.indunet.fastproto.encoder;

import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.type.BooleanType;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class EncodersTest {
    @Test
    public void testGet() {
        Class<? extends TypeEncoder> clazz = Optional.of(BooleanType.class)
                .map(c -> c.getAnnotation(Encoder.class))
                .map(Encoder::value)
                .get();
        Consumer consumer = Encoders.getEncoder(clazz);

        assertNotNull(consumer);
    }
}
