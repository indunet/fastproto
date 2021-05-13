package org.indunet.fastproto.decoder;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Decoder;

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

    public <T> T getDataType(Class<T> clazz) {
        return clazz.cast(this.dateType);
    }

    public TypeDecoder<?> getDecoder() {
        try {
            return dateType.annotationType().getAnnotation(Decoder.class).value().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
