package org.indunet.fastproto.compress;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.CodecException.CodecError;

import java.util.Arrays;

/**
 * @author Deng Ran
 * @since 1.3.0
 */
@AllArgsConstructor
@Getter
public enum CompressPolicy {
    GZIP(0x01, "gzip"),
    DEFLATE(0x02, "deflate");

    int code;
    String name;

    public static CompressPolicy byName(String name) {
        return Arrays.stream(CompressPolicy.values())
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CodecException(CodecError.INVALID_COMPRESS_POLICY));
    }
}
