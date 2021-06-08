package org.indunet.fastproto;

import org.indunet.fastproto.annotation.type.TimestampType;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Deng Ran
 * @since 1.3.0
 */
public class ProtocolTypeTest {
    @Test
    public void testJavaType() {
        ProtocolType type = ProtocolType.valueOf(TimestampType.class);

        assertEquals(type.javaType(), Timestamp.class);
    }
}
