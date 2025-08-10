package org.indunet.fastproto.annotation;

import org.indunet.fastproto.ByteOrder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for checksum validation.
 * Single-annotation configuration: specify data start, length, and where to store/read the checksum.
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
        CRC8,
        CRC16,
        CRC32
    }
}
