package org.indunet.fastproto.decoder;

import lombok.Builder;
import lombok.Data;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.encoder.TypeEncoder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * @author Deng Ran
 * @version 1.0
 */
@Data
@Builder
public class DecodeContext {
    Object object;
    byte[] datagram;

    Field field;
    EndianPolicy endian;
    Annotation dateType;

    Class<? extends TypeDecoder> decoder;
    Class<? extends TypeEncoder> encoder;

    Class<? extends Function> decodeFormula;
    Class<? extends Function> encodeFormula;

    public <T> T getDataType(Class<T> clazz) {
        return clazz.cast(this.dateType);
    }

    public <T> T getObject(Class<T> clazz) {
        return clazz.cast(this.object);
    }
}
