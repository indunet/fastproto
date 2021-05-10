package org.indunet.fastproto.assist;

import lombok.Builder;
import lombok.Data;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.TypeDecoder;
import org.indunet.fastproto.encoder.TypeEncoder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Deng Ran
 * @version 1.0
 */
@Data
@Builder
public class FieldAssist {
    Class<?> parent;
    Field field;
    Object value;
    Optional<Function> formula;
    Optional<Annotation> dataType;
    EndianPolicy endianPolicy;
    Class<? extends TypeDecoder<?>> decoderClass;
    Class<TypeEncoder> encoderCLass;
    TypeDecoder<?> typeDecoder;
    TypeEncoder typeEncoder;

    protected FieldAssist() {

    }

    public static FieldAssist create(Class<?> parent, Field field) {
        Optional<Annotation> dataType = Arrays.stream(field.getAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(DataType.class))
                .findAny();
        EndianPolicy policy = Stream.of(field.getAnnotation(Endian.class), parent.getAnnotation(Endian.class))
                .map(Endian::value)
                .findFirst()
                .orElse(EndianPolicy.Little);
        Class<? extends TypeDecoder<?>> decoderClass = dataType.map()

        return FieldAssist.builder()
                .parent(parent)
                .field(field)
                .endianPolicy(Stream.of(field.getAnnotation(Endian.class), parent.getAnnotation(Endian.class))
                    .map(Endian::value)
                    .findFirst()
                    .orElse(EndianPolicy.Little))
                .decoderClass()
                .encoderCLass()
                .typeDecoder()
                .typeEncoder()
                .build();
    }

    public void decode(byte[] datagram, Object object) {
        DecodeContext context = DecodeContext.builder()
                .endian(this.endianPolicy)
                .datagram(datagram)
                .build();

        Object value = this.typeDecoder.decode(context);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
