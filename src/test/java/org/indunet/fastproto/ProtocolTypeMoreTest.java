package org.indunet.fastproto;

import org.indunet.fastproto.annotation.AutoType;
import org.indunet.fastproto.annotation.BoolType;
import org.indunet.fastproto.annotation.StringType;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;

public class ProtocolTypeMoreTest {
	static class AutoHolder {
		@AutoType(offset = {3})
		int f;
	}

	static class BoolHolder {
		@BoolType(byteOffset = 1, bitOffset = 7)
		boolean b;
	}

	@Test
	public void testAutoTypeProxyMappingAndException() throws Exception {
		AutoType auto = AutoHolder.class.getDeclaredField("f").getAnnotation(AutoType.class);
		StringType proxied = ProtocolType.proxy(auto, StringType.class);
		assertEquals(3, proxied.offset());
		// length() exists on StringType; when AutoType.length() is empty, calling length() should throw ResolvingException
		assertThrows(org.indunet.fastproto.exception.ResolvingException.class, () -> proxied.length());
	}

	@Test
	public void testTypeProxyBoolOffsetAndMeta() throws Exception {
		Annotation ann = BoolHolder.class.getDeclaredField("b").getAnnotation(BoolType.class);
		ProtocolType pt = ProtocolType.proxy(ann);
		assertEquals(BoolType.class, pt.getType());
		assertTrue(pt.size() > 0);
		// For BoolType, offset() should map to byteOffset()
		assertEquals(1, pt.offset());
		assertEquals(0, pt.length());
	}
} 