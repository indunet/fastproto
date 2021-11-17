/*
 * Copyright 2019-2021 indunet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.indunet.fastproto;

import org.indunet.fastproto.annotation.type.ArrayType;
import org.indunet.fastproto.annotation.type.TimestampType;
import org.indunet.fastproto.annotation.type.UInteger16Type;
import org.indunet.fastproto.util.AnnotationUtils;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deng Ran
 * @since 1.3.0
 */
public class ProtocolTypeTest {
    @Test
    public void testJavaType() {
        ProtocolType type = ProtocolType.valueOf(AnnotationUtils.mock(TimestampType.class, 0));

        assertArrayEquals(type.javaTypes(), new Class<?>[] {Timestamp.class});
    }

    @Test
    public void testSize() {
        ProtocolType type = ProtocolType.valueOf(AnnotationUtils.mock(UInteger16Type.class, 0));

        assertEquals(type.size(), 2);
    }

    @Test
    public void testProtocolTypes() {
        ProtocolType type = ProtocolType.valueOf(AnnotationUtils.mock(TimestampType.class, 0));

        assertArrayEquals(type.protocolTypes(), new ProtocolType[] {ProtocolType.UINTEGER32, ProtocolType.LONG});
    }

    @Test
    public void testIsSupported() {
        assertTrue(ProtocolType.isSupported(Long.TYPE));
        assertTrue(ProtocolType.isSupported(Integer.TYPE));
        assertTrue(ProtocolType.isSupported(Timestamp.class));

        assertFalse(ProtocolType.isSupported(Class.class));
    }
}
