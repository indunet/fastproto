package org.indunet.fastproto.decoder;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class DecodersTest {
    @Test
    public void testGet() {
        TypeDecoder<Boolean> decoder = Decoders.getDecoder(BooleanDecoder.class);

        assertNotNull(decoder);
    }
}
