package org.indunet.fastproto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test of Decoder.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
public class DecoderTest {
    @Test
    public void testGet() {
        val bytes = new byte[]{1, 2, 3, 0};
        val map = FastProto.parse(bytes)
                .boolType("bool", 0, 0)
                .int8Type("int8", 1)
                .int16Type("int16", 2)
                .get();

        assertEquals(true, map.get("bool"));
        assertEquals(2, map.get("int8"));
        assertEquals(3, map.get("int16"));
    }

    @Test
    public void testMapTo() {
        val bytes = new byte[]{78, 0, 8, 0};
        val expected = new Wheel(78, 8);
        val actual = FastProto.parse(bytes)
                .int8Type("diameter", 0)
                .int16Type("thickness", 2)
                .mapTo(Wheel.class);

        assertEquals(expected.toString(), actual.toString());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Wheel {
        Integer diameter;
        Integer thickness;
    }
}
