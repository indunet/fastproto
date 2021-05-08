package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.TypeDecoder;
import org.indunet.fastproto.annotation.TypeEncoder;
import org.indunet.fastproto.decoder.BinaryDecoder;
import org.indunet.fastproto.encoder.BinaryEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Deng Ran
 * @version 1.0
 */
@DataType
@TypeDecoder(BinaryDecoder.class)
@TypeEncoder(BinaryEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BinaryType {
    int byteOffset();

    int length();
}
