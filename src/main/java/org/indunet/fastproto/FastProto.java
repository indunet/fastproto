package org.indunet.fastproto;

import lombok.Data;
import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.DecodeIgnore;
import org.indunet.fastproto.annotation.TypeDecoder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class FastProto {
    // TODO, decode method.
    public static <T> T decode(byte[] datagram, Class<T> clazz) {
        @Data
        class Tuple {
            Field field;

            Object value;
        }

        Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(DecodeIgnore.class))
                .filter(f -> Arrays.stream(f.getAnnotations())
                    .map(Annotation::annotationType)
                    .anyMatch(t -> t.isAnnotationPresent(DataType.class)))
                .map(f -> {
                    TypeDecoder::decode
                };

        return null;
    }

    public static void encode(Object object, byte[] datagram) {

    }
}
