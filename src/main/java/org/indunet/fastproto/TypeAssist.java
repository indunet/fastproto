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
import org.indunet.fastproto.tuple.Pair;
import org.indunet.fastproto.tuple.Tuple;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    Optional<Field> field;
    Optional<Annotation> dataType;
    Optional<Class<? extends TypeDecoder>> decoder;
    Optional<Class<? extends TypeEncoder>> encoder;
    Optional<Class<? extends Function>> decodeFormula;
    Optional<Class<? extends Function>> encodeFormula;
    Optional<EndianPolicy> endianPolicy;

    Boolean decodeIgnore;
    Boolean encodeIgnore;

    Boolean objectFlag;
    Boolean fieldFlag;

    List<TypeAssist> children;

    public boolean hasChild() {
        return children != null && !children.isEmpty();
    }

    protected TypeAssist() {

    }

    public static TypeAssist create(Class<?> clazz) {
        Predicate<Field> isDataType = f -> Arrays.stream(f.getAnnotations())
                .map(a -> a.annotationType())
                .anyMatch(t -> t.isAnnotationPresent(DataType.class));

        Stream<TypeAssist> classStream = Arrays.stream(clazz.getDeclaredFields())
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
                })
                .map(Pair::getC2)
                .filter(TypeAssist::hasChild);

        Optional<EndianPolicy> policy = Optional.ofNullable(clazz.getAnnotation(Endian.class))
                .map(Endian::value);

        Stream<TypeAssist> fieldStream = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(DecodeIgnore.class)
                            && !f.isAnnotationPresent(EncodeIgnore.class))
                .filter(isDataType)
                .peek(f -> f.setAccessible(true))
                .map(TypeAssist::create);

        TypeAssist assist = TypeAssist.builder()
                .type(clazz)
                .field(Optional.empty())
                .dataType(Optional.empty())
                .decoder(Optional.empty())
                .encoder(Optional.empty())
                .decodeFormula(Optional.empty())
                .encodeFormula(Optional.empty())
                .endianPolicy(Optional.empty())
                .decodeIgnore(false)
                .objectFlag(true)
                .fieldFlag(false)
                .build();

        List<TypeAssist> children = Stream.concat(fieldStream, classStream)
                .peek(a -> a.setParent(assist))
                .collect(Collectors.toList());
        assist.setChildren(children);

        return assist;
    }

    protected static TypeAssist create(Field field) {
        Optional<EndianPolicy> policy =  Optional.ofNullable(field.getAnnotation(Endian.class))
                .map(Endian::value);
        Optional<Class<? extends Function>> decodeFormula = Optional.ofNullable(field.getAnnotation(DecodeFormula.class))
                .map(DecodeFormula::value);
        Optional<Class<? extends Function>> encodeFormula = Optional.ofNullable(field.getAnnotation(EncodeFormula.class))
                .map(EncodeFormula::value);
        Optional<Annotation> dataType = Arrays.stream(field.getAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(DataType.class))
                .findAny();
        Optional<Class<? extends TypeDecoder>> decoder = dataType.map(Annotation::annotationType)
                .map(t -> t.getAnnotation(Decoder.class))
                .map(Decoder::value);
        Optional<Class<? extends TypeEncoder>> encoder = dataType.map(Annotation::annotationType)
                .map(t -> t.getAnnotation(Encoder.class))
                .map(Encoder::value);
        Boolean decodeIgnore = field.isAnnotationPresent(DecodeIgnore.class);
        Boolean encodeIgnore = field.isAnnotationPresent(EncodeIgnore.class);

        return TypeAssist.builder()
                .type(field.getType())
                .field(Optional.of(field))
                .dataType(dataType)
                .decoder(decoder)
                .encoder(encoder)
                .decodeFormula(decodeFormula)
                .encodeFormula(encodeFormula)
                .endianPolicy(policy)
                .decodeIgnore(decodeIgnore)
                .encodeIgnore(encodeIgnore)
                .objectFlag(false)
                .fieldFlag(true)
                .build();
    }

    protected DecodeContext toDecodeContext(byte[] datagram, Object object) {
        return DecodeContext.builder()
                .object(object)
                .datagram(datagram)
                .dateType(dataType.get())
                .endian(endianPolicy.orElse(EndianPolicy.Little))
                .build();
    }

    protected List<DecodeContext> toDecodeContexts(byte[] datagram, Object object) {
        try {
            Object value = this.type.newInstance();

            if (object != null) {
                field.ifPresent(f -> {
                    try {
                        f.set(object, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
            }

            Stream<DecodeContext> fieldStream = this.children.stream()
                    .filter(TypeAssist::getFieldFlag)
                    .map(a-> a.toDecodeContext(datagram, value));

            Stream<DecodeContext> classStream = this.children.stream()
                    .filter(TypeAssist::getObjectFlag)
                    .flatMap(a -> a.toDecodeContexts(datagram, value).stream());

            return Stream.concat(fieldStream, classStream)
                    .collect(Collectors.toList());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<DecodeContext> toDecodeContexts(byte[] datagram) {
        return this.toDecodeContexts(datagram, null);
    }
}