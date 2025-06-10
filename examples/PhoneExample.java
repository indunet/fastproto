package examples;

import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.domain.color.Phone;
import org.indunet.fastproto.domain.color.Color;
import org.indunet.fastproto.util.DecodeUtils;

import java.util.Arrays;

/**
 * Demonstrates encoding and decoding an object with enum fields.
 */
public class PhoneExample {
    public static void main(String[] args) {
        // Create a phone object with several features enabled
        Phone phone = Phone.getDefault();

        // Encode the object to binary data
        byte[] datagram = FastProto.encode(phone, Phone.getLength());

        // Decode object using annotations
        Phone decoded = FastProto.decode(datagram, Phone.class);
        System.out.println("Decoded object: " + decoded);

        // Manually decode selected fields
        boolean nfc = DecodeUtils.readBool(datagram, 2, 0, BitOrder.MSB_0);
        int years = DecodeUtils.readInt32(datagram, 3, ByteOrder.BIG);
        char model = (char) DecodeUtils.readInt8(datagram, 7);
        Color back = Color.values()[DecodeUtils.readInt8(datagram, 0)];
        System.out.println("Manual decode -> " + nfc + ", " + years + ", " + model + ", " + back);

        // Modify and re-encode
        decoded.setWarrantyYears(5);
        byte[] encoded = FastProto.encode(decoded, Phone.getLength());
        System.out.println("Re-encoded bytes: " + Arrays.toString(encoded));
    }
}
