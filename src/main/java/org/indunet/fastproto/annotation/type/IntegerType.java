package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.TypeDecoder;
import org.indunet.fastproto.annotation.TypeEncoder;
import org.indunet.fastproto.decoder.IntegerDecoder;
import org.indunet.fastproto.encoder.IntegerEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@DataType
@TypeDecoder(IntegerDecoder.class)
@TypeEncoder(IntegerEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerType {
    int SIZE = Integer.SIZE >> 3;
    int MAX = Integer.MAX_VALUE;
    int MIN = Integer.MIN_VALUE;

    int byteOffset();
}
