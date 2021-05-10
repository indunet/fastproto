package org.indunet.fastproto;

import lombok.Data;
import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.DecodeIgnore;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.assist.FieldAssist;
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.TypeDecoder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class FastProto {
    // TODO, decode method.
    public static <T> T decode(byte[] datagram, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        Object object = clazz.newInstance();

        Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(DecodeIgnore.class))
                .filter(f -> Arrays.stream(f.getAnnotations())
                        .map(Annotation::annotationType)
                        .anyMatch(t -> t.isAnnotationPresent(DataType.class)))
                .map(f -> FieldAssist.builder()
                        .parent(object)
                        .field(f)
                        .fieldType(f.getType())
                        .build())
                .peek(as -> as.setDateType(Arrays.stream(as.getField().getAnnotations())
                        .filter(an -> an.annotationType().isAnnotationPresent(DataType.class))
                        .findAny()
                        .get()))
                // .peek(as -> as.setEndianPolicy(as.getField().getAnnotation(Endian.class).value()))
                .peek(as -> as.setDecoderClass(as.getDateType().annotationType().getAnnotation(Decoder.class).value()))
                .peek(as -> {
                    try {
                        as.setTypeDecoder(as.getDecoderClass().newInstance());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }).forEach(System.out::println);

        return null;
    }

    // TODO, encode method.
    public static void encode(Object object, byte[] datagram) {

    }
}
