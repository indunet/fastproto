package org.indunet.fastproto;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
}
