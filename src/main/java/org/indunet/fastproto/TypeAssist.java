package org.indunet.fastproto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.TypeDecoder;
import org.indunet.fastproto.encoder.EncodeContext;
import org.indunet.fastproto.encoder.TypeEncoder;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Deng Ran
 * @version 1.0
 * @since 1.8
 */
@Data
@Builder
@AllArgsConstructor
@ToString(exclude = {"parent"})
public class TypeAssist {
    TypeAssist parent;
    Class<?> type;
    Field field;
    Annotation dataType;
    EndianPolicy endianPolicy;
    Boolean decodeIgnore;
    Boolean encodeIgnore;
    ElementType elementType;
    List<TypeAssist> elements;
    Class<? extends TypeDecoder> decoderClass;
    Class<? extends TypeEncoder> encoderClass;
    Class<? extends Function> decodeFormula;
    Class<? extends Function> encodeFormula;
    Function<DecodeContext, ?> decoder;
    Consumer<?> encoder;

    protected TypeAssist() {

    }

    public boolean hasElement() {
        return this.elements != null && !this.elements.isEmpty();
    }

    public void setValue(Object object, Object value) {
        try {
            this.field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new DecodeException(
                    MessageFormat.format(DecodeError.FAIL_ASSIGN_VALUE.getMessage(), this.field.getName()), e);
        }
    }

    public static TypeAssist of(Class<?> clazz) {
        Predicate<Field> isType = f -> Arrays.stream(f.getAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(t -> t.isAnnotationPresent(Type.class));

        // Nested types.
        Stream<TypeAssist> typeStream = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(DecodeIgnore.class)
                        && !f.isAnnotationPresent(EncodeIgnore.class))
                .filter(isType.negate())
                .map(f -> {
                    f.setAccessible(true);
                    Class<?> c = f.getType();
                    TypeAssist a = TypeAssist.of(c);
                    Boolean decodeIgnore = f.isAnnotationPresent(DecodeIgnore.class);
                    Boolean encodeIgnore = f.isAnnotationPresent(EncodeIgnore.class);
                    a.setDecodeIgnore(decodeIgnore);
                    a.setEncodeIgnore(encodeIgnore);
                    a.setField(f);

                    return a;
                }).filter(TypeAssist::hasElement);

        // Default as little endian.
        EndianPolicy endianPolicy = Optional.ofNullable(clazz.getAnnotation(Endian.class))
                .map(Endian::value)
                .orElse(EndianPolicy.LITTLE);
        Boolean decodeIgnore = clazz.isAnnotationPresent(DecodeIgnore.class);
        Boolean encodeIgnore = clazz.isAnnotationPresent(EncodeIgnore.class);

        Stream<TypeAssist> fieldStream = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(DecodeIgnore.class)
                        && !f.isAnnotationPresent(EncodeIgnore.class))
                .filter(isType)
                .peek(f -> f.setAccessible(true))
                .map(TypeAssist::of)
                .peek(a -> {
                    if (a.getEndianPolicy() == null) {
                        a.setEndianPolicy(endianPolicy);
                    }
                });

        TypeAssist assist = TypeAssist.builder()
                .type(clazz)
                .field(null)
                .dataType(null)
                .decoderClass(null)
                .encoderClass(null)
                .decodeFormula(null)
                .encodeFormula(null)
                .endianPolicy(endianPolicy)
                .decodeIgnore(decodeIgnore)
                .encodeIgnore(encodeIgnore)
                .elementType(ElementType.TYPE)
                .build();

        List<TypeAssist> elements = Stream.concat(fieldStream, typeStream)
                .peek(a -> a.setParent(assist))
                .collect(Collectors.toList());
        assist.setElements(elements);

        return assist;
    }

    protected static TypeAssist of(Field field) {
        EndianPolicy policy = Optional.ofNullable(field.getAnnotation(Endian.class))
                .map(Endian::value)
                .orElse(null);
        Boolean decodeIgnore = field.isAnnotationPresent(DecodeIgnore.class);
        Boolean encodeIgnore = field.isAnnotationPresent(EncodeIgnore.class);

        Annotation dataType = Arrays.stream(field.getAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(Type.class))
                .findAny()
                .orElseThrow(CodecException::new);
        Class<? extends TypeDecoder> decoder = Optional.of(dataType)
                .map(Annotation::annotationType)
                .map(t -> t.getAnnotation(Decoder.class))
                .map(Decoder::value)
                .orElse(null);
        Class<? extends TypeEncoder> encoder = Optional.of(dataType)
                .map(Annotation::annotationType)
                .map(t -> t.getAnnotation(Encoder.class))
                .map(Encoder::value)
                .orElse(null);
        Class<? extends Function> decodeFormula = Optional.ofNullable(field.getAnnotation(DecodeFormula.class))
                .map(DecodeFormula::value)
                .orElse(null);
        Class<? extends Function> encodeFormula = Optional.ofNullable(field.getAnnotation(EncodeFormula.class))
                .map(EncodeFormula::value)
                .orElse(null);

        return TypeAssist.builder()
                .type(field.getType())
                .field(field)
                .dataType(dataType)
                .decoderClass(decoder)
                .encoderClass(encoder)
                .decodeFormula(decodeFormula)
                .encodeFormula(encodeFormula)
                .endianPolicy(policy)
                .decodeIgnore(decodeIgnore)
                .encodeIgnore(encodeIgnore)
                .elementType(ElementType.FIELD)
                .build();
    }

    protected DecodeContext toDecodeContext(byte[] datagram, Object object) {
        return DecodeContext.builder()
                .object(object)
                .datagram(datagram)
                .typeAssist(this)
                .build();
    }

    protected List<DecodeContext> toDecodeContexts(byte[] datagram, Object object) {
        try {
            Object value = this.type.newInstance();

            if (object != null && field != null) {
                try {
                    this.field.set(object, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            Stream<DecodeContext> fieldStream = this.elements.stream()
                    .filter(a -> a.getElementType() == ElementType.FIELD)
                    .map(a -> a.toDecodeContext(datagram, value));

            Stream<DecodeContext> classStream = this.elements.stream()
                    .filter(a -> a.getElementType() == ElementType.TYPE)
                    .flatMap(a -> a.toDecodeContexts(datagram, value).stream());

            return Stream.concat(fieldStream, classStream)
                    .collect(Collectors.toList());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new DecodeException(
                    MessageFormat.format(DecodeError.FAIL_INITIALIZING_DECODE_OBJECT.getMessage(), this.type.getName()), e);
        }
    }

    public List<DecodeContext> toDecodeContexts(byte[] datagram) {
        return this.toDecodeContexts(datagram, null);
    }

    public EncodeContext toEncodeContext(Object object, byte[] datagram) {
        try {

            return EncodeContext.builder()
                    .datagram(datagram)
                    .typeAssist(this)
                    .value(this.field.get(object))
                    .build();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new EncodeException(
                    MessageFormat.format(EncodeError.FAIL_GETTING_FIELD_VALUE.getMessage(), this.field.getName()), e);
        }
    }

    public List<EncodeContext> toEncodeContexts(Object object, byte[] datagram) {
        Stream<EncodeContext> fieldStream = this.elements.stream()
                .filter(a -> a.getElementType() == ElementType.FIELD)
                .map(a -> a.toEncodeContext(object, datagram));

        Stream<EncodeContext> classStream = this.elements.stream()
                .filter(a -> a.getElementType() == ElementType.TYPE)
                .flatMap(a -> {
                    try {
                        if (object != null && a.field.get(object) != null) {
                            return a.toEncodeContexts(a.field.get(object), datagram).stream();
                        } else {
                            return Stream.empty();
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new DecodeException(
                                MessageFormat.format(EncodeError.FAIL_GETTING_FIELD_VALUE.getMessage(), this.type.getName()), e);
                    }
                });

        return Stream.concat(fieldStream, classStream)
                .filter(c -> c.getValue() != null)
                .collect(Collectors.toList());
    }
}