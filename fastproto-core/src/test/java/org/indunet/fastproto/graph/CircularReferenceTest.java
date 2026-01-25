/*
 * Copyright 2019-2021 indunet.org
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

package org.indunet.fastproto.graph;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.Int8Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Deng Ran
 * @since 2.2.0
 */
public class CircularReferenceTest {
    @Data
    public static class Node1 {
        Node1 node1;
        Node2 node2;

        @Int8Type(offset = 0)
        int id;
    }

    @Data
    public static class Node2 {
        Node1 node1;
        Node2 node2;
        Node3 node3;

        @Int8Type(offset = 1)
        int id;
    }

    @Data
    public static class Node3 {
        Node1 node1;
        Node2 node2;

        @Int8Type(offset = 2)
        int id;
    }

    @Test
    public void testNode() {
        val datagram = new byte[10];

        datagram[0] = 1;
        datagram[1] = 2;
        datagram[2] = 3;

        val node1 = FastProto.decode(datagram, Node1.class);
        assertNotNull(node1.getNode1());
        assertNotNull(node1.getNode2());
        assertEquals(1, node1.getId());

        val node2 = node1.getNode2();
        assertNotNull(node2.getNode1());
        assertNotNull(node2.getNode2());
        assertNotNull(node2.getNode3());
        assertEquals(2, node2.getId());

        val node3 =node2.getNode3();
        assertNotNull(node3.getNode1());
        assertNotNull(node3.getNode2());
        assertEquals(3, node3.getId());
    }
}
