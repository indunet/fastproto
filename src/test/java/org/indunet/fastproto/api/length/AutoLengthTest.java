package org.indunet.fastproto.api.length;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.BoolType;
import org.indunet.fastproto.annotation.FloatType;
import org.indunet.fastproto.annotation.Int8ArrayType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test of auto length.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
public class AutoLengthTest {
    @Test
    public void testAutoLength() {
        val object1 = new TestObject1();

        assertEquals(2, FastProto.toBytes(object1).length);

        val object2 = new TestObject2();

        assertEquals(9, FastProto.toBytes(object2).length);

        val object3 = new TestObject3();

        assertEquals(12, FastProto.toBytes(object3).length);
    }

    public static class TestObject1 {
        @Int8ArrayType(offset = 0, length = 2)
        int[] values = new int[] {0, 1};
    }

    public static class TestObject2 {
        @BoolType(byteOffset = 8, bitOffset = 7)
        Boolean value = false;
    }

    public static class TestObject3 {
        @FloatType(offset = 8)
        float value = 2.718f;
    }
}
