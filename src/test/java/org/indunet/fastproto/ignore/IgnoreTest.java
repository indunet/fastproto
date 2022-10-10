package org.indunet.fastproto.ignore;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.DecodingIgnore;
import org.indunet.fastproto.annotation.EncodingIgnore;
import org.indunet.fastproto.annotation.Int16Type;
import org.indunet.fastproto.annotation.Int8Type;
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
    }

    @Test
    public void testDecodeIgnore() {
        val bytes = new byte[] {1, 2, 3, 4};
        val vehicle = FastProto.parse(bytes, Vehicle.class);

        assertNotNull(vehicle.getSpeed());
        assertNull(vehicle.getMileage());
    }

    @Test
    public void testEncodeIgnore() {
        val vehicle = new Vehicle();

        vehicle.setSpeed(1);
        vehicle.setMileage(2);

        val bytes = FastProto.toBytes(vehicle, 4);
        assertArrayEquals(bytes, new byte[] {0, 0, 2, 0});
    }
}
