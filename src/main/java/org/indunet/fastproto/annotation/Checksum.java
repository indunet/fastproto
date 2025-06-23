package org.indunet.fastproto.annotation;

import org.indunet.fastproto.ByteOrder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for checksum validation.
 * It can be used together with UInt16Type or UInt32Type to mark
 * a field storing CRC value.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Checksum {
    /** Start offset of the data for CRC calculation. */
    int start();

    /** Length of the data for CRC calculation. */
    int length();

    /** Checksum algorithm. */
    Type type() default Type.CRC16;

    /** Byte order when storing checksum value. */
    ByteOrder byteOrder() default ByteOrder.LITTLE;

    enum Type {
        CRC16, CRC32
    }
}
