package org.indunet.fastproto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.TypeDecoder;
import org.indunet.fastproto.encoder.TypeEncoder;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.tuple.Pair;
import org.indunet.fastproto.tuple.Tuple;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
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

    public static TypeAssist create(Class<?> clazz) {
        Predicate<Field> isDataType = f -> Arrays.stream(f.getAnnotations())
                .map(a -> a.annotationType())
                .anyMatch(t -> t.isAnnotationPresent(DataType.class));

        // Nested types.
        Stream<TypeAssist> typeStream = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(DecodeIgnore.class)
                        && !f.isAnnotationPresent(EncodeIgnore.class))
                .filter(isDataType.negate())
                .peek(f -> f.setAccessible(true))
                .map(f -> Tuple.get(f, f.getType()))
                .map(t -> Tuple.get(t.getC1(), TypeAssist.create(t.getC2())))
                .peek(t -> {
                    Boolean decodeIgnore = t.getC1().isAnnotationPresent(DecodeIgnore.class);
                    Boolean encodeIgnore = t.getC1().isAnnotationPresent(EncodeIgnore.class);
                    t.getC2().setDecodeIgnore(decodeIgnore);
                    t.getC2().setEncodeIgnore(encodeIgnore);
                    t.getC2().setField(t.getC1());
                })
                .map(Pair::getC2)
                .filter(TypeAssist::hasElement);

        EndianPolicy endianPolicy = Optional.ofNullable(clazz.getAnnotation(Endian.class))
                .map(Endian::value)
                .get();
        Boolean decodeIgnore = clazz.isAnnotationPresent(DecodeIgnore.class);
        Boolean encodeIgnore = clazz.isAnnotationPresent(EncodeIgnore.class);

        Stream<TypeAssist> fieldStream = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(DecodeIgnore.class)
                        && !f.isAnnotationPresent(EncodeIgnore.class))
                .filter(isDataType)
                .peek(f -> f.setAccessible(true))
                .map(TypeAssist::create);
                // TODO, set default endian policy.

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

    protected static TypeAssist create(Field field) {
        EndianPolicy policy = Optional.ofNullable(field.getAnnotation(Endian.class))
                .map(Endian::value)
                .get();
        Boolean decodeIgnore = field.isAnnotationPresent(DecodeIgnore.class);
        Boolean encodeIgnore = field.isAnnotationPresent(EncodeIgnore.class);
        Annotation dataType = Arrays.stream(field.getAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(DataType.class))
                .findAny()
                .orElseThrow(CodecException::new);
        Class<? extends TypeDecoder> decoder = Optional.of(dataType)
                .map(Annotation::annotationType)
                .map(t -> t.getAnnotation(Decoder.class))
                .map(Decoder::value)
                .orElseThrow(DecodeException::new);
        Class<? extends TypeEncoder> encoder = Optional.of(dataType)
                .map(Annotation::annotationType)
                .map(t -> t.getAnnotation(Encoder.class))
                .map(Encoder::value)
                .orElseThrow(EncodeException::new);
        Class<? extends Function> decodeFormula = Optional.ofNullable(field.getAnnotation(DecodeFormula.class))
                .map(DecodeFormula::value)
                .get();
        Class<? extends Function> encodeFormula = Optional.ofNullable(field.getAnnotation(EncodeFormula.class))
                .map(EncodeFormula::value)
                .get();

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

    public boolean hasElement() {
        return this.elements != null && !this.elements.isEmpty();
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
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        throw new CodecException();
    }

    public List<DecodeContext> toDecodeContexts(byte[] datagram) {
        return this.toDecodeContexts(datagram, null);
    }
}