package org.indunet.fastproto.annotation;

import org.indunet.fastproto.ByteOrder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CRC utilities for CRC8/CRC16/CRC32.
 * Single-annotation configuration: specify data range and checksum storage address.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Checksum {
    // Start byte address (inclusive) of data participating in checksum
    int start();

    // Length in bytes of data participating in checksum
    int length();

    // Byte address where the checksum value is stored/read
    int offset();

    // Checksum algorithm
    Type type() default Type.CRC16;

    // Byte order when storing/reading checksum value
    ByteOrder byteOrder() default ByteOrder.LITTLE;

    enum Type {
        // 8-bit
        CRC8,
        CRC8_SMBUS,
        CRC8_MAXIM,
        XOR8,
        LRC8,
        // 16-bit
        CRC16,
        CRC16_MODBUS,
        CRC16_CCITT,
        // 32-bit
        CRC32,
        CRC32C,
        // 64-bit
        CRC64_ECMA182,
        CRC64_ISO
    }
}
