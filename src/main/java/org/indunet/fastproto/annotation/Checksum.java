package org.indunet.fastproto.annotation;

import org.indunet.fastproto.checksum.CRCType;

import java.lang.annotation.*;

/**
 * Annotation for CRC checksum field.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Checksum {
    CRCType type();
    /** start offset of bytes for calculation */
    int start();
    /** length of bytes for calculation */
    int length();
}
