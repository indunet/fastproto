package org.indunet.fastproto;

import org.indunet.fastproto.annotation.UInt8Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbstractProtocolTest {
	public static class P extends AbstractProtocol {
		@UInt8Type(offset = 0)
		int a;
		@UInt8Type(offset = 1)
		int b;
		public P(int a, int b) { this.a = a; this.b = b; }
	}

	@Test
	public void testToByteArrayAndHexStringAutoLength() {
		P p = new P(0x12, 0xAB);
		byte[] bytes = p.toByteArray();
		// Auto length encodes only annotated fields (2 bytes)
		assertEquals(2, bytes.length);
		assertEquals(0x12, bytes[0] & 0xFF);
		assertEquals(0xAB, bytes[1] & 0xFF);
		assertEquals("12ab", p.toHexString());
	}

	@Test
	public void testToByteArrayWithFixedLength() {
		P p = new P(0x01, 0x02);
		p.length = 4;
		byte[] bytes = p.toByteArray();
		assertEquals(4, bytes.length);
		assertEquals("01020000", p.toHexString());
	}

	@Test
	public void testPrintHexString() {
		P p = new P(0x0F, 0xF0);
		// Just ensure it does not throw and prints something non-empty
		p.printHexString();
		assertTrue(p.toHexString().length() >= 4);
	}
} 