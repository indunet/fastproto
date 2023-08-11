package org.indunet.fastproto.ignore;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.DecodingIgnore;
import org.indunet.fastproto.annotation.EncodingIgnore;
import org.indunet.fastproto.annotation.type.Integer16Type;
import org.indunet.fastproto.annotation.type.Integer8Type;
import org.indunet.fastproto.annotation.type.UInteger16Type;
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
        @Integer8Type(0)
        Integer speed;

        @DecodingIgnore
        @Integer16Type(2)
        Integer mileage;

        @DecodingIgnore
        @EncodingIgnore
        Motor motor = new Motor();
    }

    @Data
    public static class Motor {
        @UInteger16Type(4)
        int voltage = 24;

        @UInteger16Type(6)
        int current = 10;
    }

    @Test
    public void testDecodeIgnore() {
        val bytes = new byte[] {1, 2, 3, 4};
        val vehicle = FastProto.parseFrom(bytes, Vehicle.class);

        assertNotNull(vehicle.getSpeed());
        assertNull(vehicle.getMileage());
    }

    @Test
    public void testEncodeIgnore() {
        val vehicle = new Vehicle();

        vehicle.setSpeed(1);
        vehicle.setMileage(2);

        val bytes = FastProto.toByteArray(vehicle, 4);
        assertArrayEquals(bytes, new byte[] {0, 0, 2, 0});
    }
}
