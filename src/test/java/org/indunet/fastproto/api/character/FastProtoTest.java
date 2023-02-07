package org.indunet.fastproto.api.character;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test of FastProto api
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public class FastProtoTest {
    @Test
    public void testDecode() {
        val expected = new DataObject();
        val actual = FastProto.decode(expected.toBytes(), DataObject.class);

        assertEquals(expected, actual);
    }

    @Test
    public void testEncode() {
        val object = new DataObject();
        val expected = object.toBytes();
        val actual = FastProto.encode(object, 6);

        assertArrayEquals(expected, actual);
    }
}
