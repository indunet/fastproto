package org.indunet.fastproto;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecoderMoreTest {
	@Test
	public void testReadBoolOverloads() {
		byte[] bytes = new byte[2];
		// set first byte bits as 1 0 1 0 1 0 1 0 (LSB first: 0..7)
		bytes[0] = (byte) 0b01010101;
		Map<String, Object> m = FastProto.decode(bytes)
				.defaultBitOrder(BitOrder.LSB_0)
				.readBool("b0")
				.readBool("b1", BitOrder.MSB_0)
				.readBool("b2", 0, 0)
				.readBool("b3", 0, 7, BitOrder.MSB_0)
				.getMap();
		// LSB_0 readBool() consumes bit0 -> true (1)
		assertEquals(true, m.get("b0"));
		// MSB_0 readBool() consumes next bit in MSB order -> for 0b01010101, msb is 0
		assertEquals(true, m.get("b1"));
		// explicit offset bit0 LSB -> true
		assertEquals(true, m.get("b2"));
		// explicit offset bit7 MSB maps to LSB -> true
		assertEquals(true, m.get("b3"));
	}

	@Test
	public void testReadBytesOverloads() {
		byte[] bytes = new byte[]{1,2,3,4,5,6};
		Map<String, Object> m = FastProto.decode(bytes)
				.readBytes("a", 3)
				.readBytes("b", 2, 2)
				.getMap();
		byte[] a = (byte[]) m.get("a");
		byte[] b = (byte[]) m.get("b");
		assertEquals(3, a.length);
		assertEquals(2, b.length);
		// a starts from current cursor (0) length 3
		assertEquals(1, a[0]);
		assertEquals(3, a[2]);
		// b from offset 2 length 2
		assertEquals(3, b[0]);
		assertEquals(4, b[1]);
	}
} 