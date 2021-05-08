package org.indunet.fastproto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EndianPolicy {
    Big(0x01, "Big"),
    Little(0x02, "Little");

    int code;
    String name;
}
