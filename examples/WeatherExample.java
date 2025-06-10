package examples;

import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.domain.Weather;

import java.sql.Timestamp;
import java.util.Arrays;

/**
 * Demonstrates encoding and decoding a typical weather station datagram.
 */
public class WeatherExample {
    public static void main(String[] args) {
        // Build datagram using the fluent encoder
        byte[] bytes = FastProto.create(20)
                .writeUInt8(0, 101)
                .writeInt64(2, Timestamp.valueOf("2024-06-01 12:00:00").getTime())
                .writeUInt16(10, ByteOrder.BIG, 85)
                .writeInt16(12, -15)
                .writeUInt32(14, 13)
                .writeBool(18, 0, BitOrder.LSB_0, true)
                .writeBool(18, 1, true)
                .writeBool(18, 2, true)
                .get();

        // Decode the byte array back to an object
        Weather decoded = FastProto.decode(bytes, Weather.class);
        System.out.println("Decoded object: " + decoded);

        // Modify object and encode again
        decoded.setPressure(2.6);
        byte[] encoded = FastProto.encode(decoded, bytes.length);
        System.out.println("Re-encoded bytes: " + Arrays.toString(encoded));
    }
}
