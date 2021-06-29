package org.indunet.fastproto;

import org.indunet.fastproto.flow.decode.DecompressFlow;
import org.indunet.fastproto.flow.decode.VerifyChecksumFlow;
import org.indunet.fastproto.flow.decode.VerifyProtocolVersionFlow;
import org.indunet.fastproto.flow.encode.CompressFlow;
import org.indunet.fastproto.flow.encode.InferLengthFlow;
import org.indunet.fastproto.flow.encode.WriteChecksumFlow;
import org.indunet.fastproto.flow.encode.WriteProtocolVersionFlow;

public final class CodecFeature {
    public static final int DEFAULT = 0;
    public static final int IGNORE_ENABLE_COMPRESS = CompressFlow.FLOW_CODE | DecompressFlow.FLOW_CODE;
    public static final int IGNORE_PROTOCOL_VERSION = VerifyProtocolVersionFlow.FLOW_CODE | WriteProtocolVersionFlow.FLOW_CODE;
    public static final int IGNORE_DATA_INTEGRITY = VerifyChecksumFlow.FLOW_CODE | WriteChecksumFlow.FLOW_CODE;
    public static final int NON_INFER_LENGTH = InferLengthFlow.FLOW_CODE;
}
