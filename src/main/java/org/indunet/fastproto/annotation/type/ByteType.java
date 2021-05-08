package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.TypeDecoder;
import org.indunet.fastproto.annotation.TypeEncoder;
import org.indunet.fastproto.decoder.ByteDecoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Deng Ran
 * @version 1.0
 */
@DataType
@TypeDecoder(ByteDecoder.class)
@TypeEncoder(TypeEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ByteType {
    int SIZE = Byte.SIZE >> 3;
    int MAX = Byte.MAX_VALUE;
    int MIN = Byte.MIN_VALUE;

    int byteOffset();
}
