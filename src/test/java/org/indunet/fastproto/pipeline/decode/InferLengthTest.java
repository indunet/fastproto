package org.indunet.fastproto.pipeline.decode;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.Int8ArrayType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InferLengthTest {
    @Test
    public void testInferLength() {
        val object = new TestObject();
        val bytes = FastProto.toBytes(object);

        assertEquals(2, bytes.length);
    }

    public static class TestObject {
        @Int8ArrayType(offset = 0, length = 2)
        int[] ints = new int[] {0, 1};
    }
}
