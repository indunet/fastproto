package org.indunet.fastproto;

import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.iot.tesla.Tesla;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Deng Ran
 * @see TypeAssist
 * @since 1.0.0
 */
public class TypeAssistTest {
    @Test
    public void testOf() {
        TypeAssist assist = TypeAssist.of(Tesla.class);
        List<DecodeContext> contexts = assist.toDecodeContexts(new byte[100]);

        assertNotNull(contexts);
    }
}
