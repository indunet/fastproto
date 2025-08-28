package org.indunet.fastproto;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class EncoderMoreTest {
	@Test
	public void testWriteOverloadsAndBytes() {
		byte[] expected = new byte[24];
		// writeByte at offset 0..2
		expected[0] = 0x11;
		expected[1] = 0x22;
		expected[2] = 0x33;
		// writeShort LE at 4 (2 bytes) => 0x0405 -> 05 04
		expected[4] = 0x05;
		expected[5] = 0x04;
		// writeInt32 BE at 6 => 0x0A0B0C0D -> 0A 0B 0C 0D (Encoder writes BE differently: value laid MSB first to low index)
		expected[6] = 0x0A;
		expected[7] = 0x0B;
		expected[8] = 0x0C;
		expected[9] = 0x0D;
		// writeInt64 LE at 10 => 0x0102030405060708
		expected[10] = 0x08;
		expected[11] = 0x07;
		expected[12] = 0x06;
		expected[13] = 0x05;
		expected[14] = 0x04;
		expected[15] = 0x03;
		expected[16] = 0x02;
		expected[17] = 0x01;
		// writeUInt32 BE at 18 => 0xA0B0C0D0 -> A0 B0 C0 D0
		expected[18] = (byte) 0xA0;
		expected[19] = (byte) 0xB0;
		expected[20] = (byte) 0xC0;
		expected[21] = (byte) 0xD0;
		// writeBytes at 22 (2 bytes)
		expected[22] = (byte) 0xFE;
		expected[23] = (byte) 0xEF;

		byte[] actual = FastProto.create(24)
				.defaultByteOrder(ByteOrder.LITTLE)
				.defaultBitOrder(BitOrder.LSB_0)
				.writeByte(0, new byte[]{0x11, 0x22, 0x33})
				.writeShort(4, ByteOrder.LITTLE, (short) 0x0405)
				.writeInt32(6, ByteOrder.BIG, 0x0A0B0C0D)
				.writeInt64(10, ByteOrder.LITTLE, 0x0102030405060708L)
				.writeUInt32(18, ByteOrder.BIG, 0xA0B0C0D0L)
				.writeBytes(22, new byte[]{(byte) 0xFE, (byte) 0xEF})
				.get();

		assertArrayEquals(expected, actual);
	}

	@Test
	public void testWriteUInt64Orders() {
		// BIG should place high 32-bits first
		byte[] big = FastProto.create(8)
				.writeUInt64(0, ByteOrder.BIG, new BigInteger("0000000100000002", 16))
				.get();
		// high=1 -> at [0..3] big-endian, low=2 -> at [4..7] big-endian
		assertArrayEquals(new byte[]{0,0,0,1, 0,0,0,2}, big);

		byte[] little = FastProto.create(8)
				.writeUInt64(0, ByteOrder.LITTLE, new BigInteger("0000000200000001", 16))
				.get();
		// low=1 little-endian at [0..3], high=2 little-endian at [4..7]
		assertArrayEquals(new byte[]{1,0,0,0, 2,0,0,0}, little);
	}
} 