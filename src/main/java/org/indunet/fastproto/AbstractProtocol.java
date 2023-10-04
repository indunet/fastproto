package org.indunet.fastproto;

/**
 * Abstract protocol.
 *
 * @author Deng Ran
 * @since 3.11.0
 */
public abstract class AbstractProtocol {
    public static final int NON_FIXED_LENGTH = -1;

    protected int length = NON_FIXED_LENGTH;
    protected BitOrder bitOrder = BitOrder.LSB_0;
    protected ByteOrder byteOrder = ByteOrder.nativeOrder();

    public byte[] toByteArray() {
        if (length == NON_FIXED_LENGTH) {
            return FastProto.encode(this);
        } else {
            return FastProto.encode(this, length);
        }
    }

    public String toHexString() {
        StringBuilder hexString = new StringBuilder();

        for (byte b : toByteArray()) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }

    public void printHexString() {
        System.out.println(toHexString());
    }
}
