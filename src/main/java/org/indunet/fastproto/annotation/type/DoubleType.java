package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.TypeDecoder;
import org.indunet.fastproto.annotation.TypeEncoder;
import org.indunet.fastproto.decoder.DoubleDecoder;
import org.indunet.fastproto.encoder.DoubleEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Deng Ran
 * @version 1.0
 */
@DataType
@TypeDecoder(DoubleDecoder.class)
@TypeEncoder(DoubleEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleType {
    int SIZE = Double.SIZE >> 3;
    double MAX = Double.MAX_VALUE;
    double MIN = Double.MIN_VALUE;

    int byteOffset();
}