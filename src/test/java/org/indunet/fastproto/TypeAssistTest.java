package org.indunet.fastproto;

import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.entity.Tesla;
import org.junit.Test;

import java.util.List;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class TypeAssistTest {
    @Test
    public void testCreate() {
        TypeAssist assist = TypeAssist.create(Tesla.class);
        // List<DecodeContext> contexts = assist.toDecodeContext(new byte[100]);

        System.out.println("Successfully");
    }
}
