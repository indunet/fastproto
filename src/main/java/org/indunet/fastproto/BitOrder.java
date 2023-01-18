package org.indunet.fastproto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.indunet.fastproto.exception.CodecException;

import java.util.Arrays;

/**
 * Bit order policy.
 *
 * @author Deng Ran
 * @since 3.9.0
 */
@Getter
@AllArgsConstructor
public enum BitOrder {
    LSB_0(0x01, "LSB_0"),
    MSB_0(0x02, "MSB_0");

    int code;
    String name;

    public static ByteOrder byName(String name) {
        return Arrays.stream(ByteOrder.values())
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CodecException("Invalid bit order, only LSB_0 or MSB_0 can be set."));
    }
}
