package org.indunet.fastproto.ignore;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Chance
 * @since 1.0.0
 */
public class IgnoreTest {
    @Data
    public static class Vehicle {
        @EncodingIgnore
        @Int8Type(offset = 0)
        Integer speed;

        @DecodingIgnore
        @Int16Type(offset = 2)
        Integer mileage;

        @DecodingIgnore
        @EncodingIgnore
        Motor motor = new Motor();
    }

    @Data
    public static class Motor {
        @Int8Type(offset = 4)
        int current;

        @UInt16Type(offset = 6)
        int voltage;
    }

    @Test
    public void testDecodeIgnore() {
        val bytes = new byte[] {1, 2, 3, 4};
        val vehicle = FastProto.decode(bytes, Vehicle.class);

        assertNotNull(vehicle.getSpeed());
        assertNull(vehicle.getMileage());
    }

    @Test
    public void testEncodeIgnore() {
        val vehicle = new Vehicle();

        vehicle.setSpeed(1);
        vehicle.setMileage(2);

        val bytes = FastProto.encode(vehicle);
        assertArrayEquals(bytes, new byte[] {0, 0, 2, 0});
    }
}
