package org.indunet.fastproto.compress;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Deng Ran
 * @since 1.3.0
 */
@AllArgsConstructor
@Getter
public enum CompressPolicy {
    GZIP(0x01),
    DEFLATE(0x02);

    int code;
}
