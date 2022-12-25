package org.indunet.fastproto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test of Decoder.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
public class DecoderTest {
    @Test
    public void testGetAsType() {
        val bytes = new byte[]{1, 2, 3, 0, 0, 0, 1, 0};

        assertEquals(true, FastProto.parse(bytes)
                .boolType(0, 0)
                .getAsBoolean());
        assertEquals(2, FastProto.parse(bytes)
                .int8Type(1)
                .getAsInt());
        assertEquals(3, FastProto.parse(bytes)
                .int16Type(2)
                .getAsInt());
        assertEquals(256, FastProto.parse(bytes)
                .int32Type(4, EndianPolicy.BIG)
                .getAsInt());
    }

    @Test
    public void testAsMap() {
        val bytes = new byte[] {1, 2, 3, 0, 0, 0, 0, 1};
        val map = FastProto.parse(bytes)
                .boolType(0, 0)
                .int8Type(1)
                .int16Type(2)
                .uint32Type(4, EndianPolicy.BIG)
                .getAsMap();

        assertEquals(true, map.get("0"));
        assertEquals(2, map.get("1"));
        assertEquals(3, map.get("2"));
        assertEquals(1l, map.get("3"));
    }

    @Test
    public void testMapTo() {
        val bytes = new byte[]{78, 0, 8, 0};
        val expected = new Wheel(78, 8);
        val actual = FastProto.parse(bytes)
                .int8Type(0, "diameter")
                .int16Type(2, "thickness")
                .mapTo(Wheel.class);

        assertEquals(expected.toString(), actual.toString());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Wheel {
        Integer diameter;
        Integer thickness;
    }

    @Test
    public void testMapToArg() {
        val bytes = new byte[]{78, 0, 8, 0, 0, 0, 0, 81};
        val expected = new DataObject(78, 8, 81l);
        val actual = FastProto.parse(bytes)
                .int8Type(0, "f1")
                .int16Type(2, "f2")
                .uint32Type(4, EndianPolicy.BIG, "f3")
                .mapTo(DataObject.class);

        assertEquals(expected.toString(), actual.toString());
    }

    @Data
    public static class DataObject {
        Integer f1;
        Integer f2;
        Long f3;

        public DataObject(Integer f1, Integer f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        public DataObject(Integer f1, Integer f2, Long f3) {
            this.f3 = f3;
            this.f2 = f2;
            this.f1 = f1;
        }
    }
}
