package org.indunet.fastproto;

import org.indunet.fastproto.exception.CodecException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deng Ran
 * @since 1.4.0
 */
class EndianPolicyTest {
    @Test
    void testByName() {
        assertEquals(EndianPolicy.byName("Big"), EndianPolicy.BIG);
        assertEquals(EndianPolicy.byName("Little"), EndianPolicy.LITTLE);

        assertThrows(CodecException.class, () -> EndianPolicy.byName("Unknown"));
    }
}