package org.indunet.fastproto.pipeline;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FlowCodeTest {
	@Test
	public void testConstants() {
		long s = 0;
		s |= FlowCode.DECODE_FLOW_CODE;
		s |= FlowCode.VERIFY_FIXED_LENGTH_FLOW_CODE;
		s |= FlowCode.ENCODE_FLOW_CODE;
		s |= FlowCode.FIXED_LENGTH_FLOW_CODE;
		s |= FlowCode.INFER_LENGTH_FLOW_CODE;
		assertTrue(s != 0);
	}
} 