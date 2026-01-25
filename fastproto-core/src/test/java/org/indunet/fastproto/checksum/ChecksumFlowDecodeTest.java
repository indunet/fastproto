package org.indunet.fastproto.checksum;

import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.exception.DecodingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChecksumFlowDecodeTest {
	@Test
	public void testDecodePass() {
		ChecksumAnnotationObject obj = new ChecksumAnnotationObject(
				0x31, 0x32, 0x33, 0x34, 0x35,
				0, 0, 0, 0L, java.math.BigInteger.ZERO, java.math.BigInteger.ZERO);

		byte[] bytes = FastProto.encode(obj, 30);

		assertDoesNotThrow(() -> FastProto.decode(bytes, ChecksumAnnotationObject.class));
	}

	@Test
	public void testDecodeFailOnChecksumMismatch() {
		ChecksumAnnotationObject obj = new ChecksumAnnotationObject(
				0x31, 0x32, 0x33, 0x34, 0x35,
				0, 0, 0, 0L, java.math.BigInteger.ZERO, java.math.BigInteger.ZERO);

		byte[] bytes = FastProto.encode(obj, 30);
		// Corrupt one data byte to break all dependent checksums
		bytes[0] ^= 0x01;

		assertThrows(DecodingException.class, () -> FastProto.decode(bytes, ChecksumAnnotationObject.class));
	}
} 