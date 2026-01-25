package org.indunet.fastproto;

import lombok.val;
import org.indunet.fastproto.pipeline.Pipeline;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Deng Ran
 * @since 3.2.0
 */
public class PipelineTest {
    public static class P0 extends Pipeline<String> {
        @Override
        public void process(String context) {

        }

        @Override
        public long getCode() {
            return 0x01;
        }
    }

    public static class P1 extends Pipeline<String> {
        @Override
        public void process(String context) {

        }

        @Override
        public long getCode() {
            return 0x02;
        }
    }

    public static class P2 extends Pipeline<String> {
        @Override
        public void process(String context) {

        }

        @Override
        public long getCode() {
            return 0x04;
        }
    }

    @Test
    public void testCreate() {
        assertEquals("P0 -> P1 -> P2", Pipeline.create(new Class[] {P0.class, P1.class, P2.class}).toString());
        assertEquals("P0 -> P2", Pipeline.create(new Class[] {P0.class, P1.class, P2.class}, 0x02).toString());
    }

    @Test
    public void testAppend() {
        val pipeline = Pipeline.create(new Class[] {P0.class, P1.class});

        assertEquals("P0 -> P1 -> P2", pipeline.append(P2.class).toString());
    }
}
