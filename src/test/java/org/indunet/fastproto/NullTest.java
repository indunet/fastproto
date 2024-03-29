package org.indunet.fastproto;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.annotation.Int8Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Null object test.
 *
 * @author Deng Ran
 * @since 3.1.1
 */
public class NullTest {
    @Test
    public void testToByteArray() {
        val motor = new Motor();
        motor.setVoltage(24);

        val bytes = FastProto.encode(motor, 8);
        assertNotNull(bytes);
    }

    @Data
    public static class Motor {
        @Int8Type(offset = 0)
        Integer speed;

        @Int8Type(offset = 2)
        Integer voltage;
    }
}
