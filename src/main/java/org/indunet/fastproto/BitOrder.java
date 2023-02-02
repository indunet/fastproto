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

    final int code;
    final String name;
}
