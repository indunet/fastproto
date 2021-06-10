package org.indunet.fastproto.compress;

import org.indunet.fastproto.exception.CodecException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deng Ran
 * @since 1.4.0
 */
class CompressPolicyTest {
    @Test
    void testByName() {
        assertEquals(CompressPolicy.byName("gzip"), CompressPolicy.GZIP);
        assertEquals(CompressPolicy.byName("deflate"), CompressPolicy.DEFLATE);

        assertThrows(CodecException.class, () -> CompressPolicy.byName("unknown"));
    }
}