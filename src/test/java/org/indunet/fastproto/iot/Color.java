package org.indunet.fastproto.iot;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Color {
    GREEN(0x01),
    RED(0x08),
    YELLOW(0x09);

    int code;
}
