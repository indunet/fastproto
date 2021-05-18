package org.indunet.fastproto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EndianPolicy {
    BIG(0x01, "Big"),
    LITTLE(0x02, "Little");

    int code;
    String name;
}
