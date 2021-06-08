package org.indunet.fastproto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.CodecException.CodecError;

import java.util.Arrays;

/**
 * Endian policy.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum EndianPolicy {
    BIG(0x01, "Big"),
    LITTLE(0x02, "Little");

    int code;
    String name;

    public static EndianPolicy byName(String name) {
        return Arrays.stream(EndianPolicy.values())
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CodecException(CodecError.INVALID_ENDIAN_POLICY));
    }
}
