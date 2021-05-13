package org.indunet.fastproto;

import lombok.Builder;
import lombok.Data;
import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.DecodeIgnore;
import org.indunet.fastproto.annotation.EncodeIgnore;
import org.indunet.fastproto.decoder.TypeDecoder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Deng Ran
 * @version 1.0
 */
@Data
@Builder
public class TypeAssist {
    TypeAssist parent;
    Class<?> type;
    Optional<Field> field;
    Optional<Annotation> dataType;
    Class<? extends TypeDecoder> decoder;
    Class<? extends TypeDecoder> encoder;
    Optional<Class<? extends Function>> decodeFormula;
    Optional<Class<? extends Function>> encodeFormula;
    EndianPolicy endianPolicy;

    Boolean decodeIgnore;
    Boolean encodeIgnore;

    Boolean objectFlag;
    Boolean fieldFlag;

    List<TypeAssist> children;

    public void addChild(TypeAssist assist) {
        this.children.add(assist);
    }

    public static Optional<TypeAssist> create(Class<?> clazz) {
//        Predicate<Field> decodeIgnore = f -> f.isAnnotationPresent(DecodeIgnore.class);
//        Predicate<Field> encodeIgnore = f -> f.isAnnotationPresent(EncodeIgnore.class);
//        Predicate<Field> annotatedDataType = f -> Arrays.stream(f.getAnnotations())
//                .map(a -> a.annotationType())
//                .anyMatch(t -> t.isAnnotationPresent(DataType.class));
//
//
//        ClassAssistBuilder builder = TypeAssist.builder();
//
//        // this field;
//        builder.
//
//        // object child
//        builder.children(Arrays.stream(clazz.getDeclaredFields())
//                .filter(decodeIgnore.and(encodeIgnore))
//                .filter(annotatedDataType.negate())
//                .map(Field::getType)
//                .map(TypeAssist::create)
//                .collect(Collectors.toList()));
//
//        TypeAssist assist = builder.build();
//
//        assist.getChildren().stream()
//                .peek(a -> a.setParent(assist))
//                .peek(a -> a.setEndianPolicy(assist.getEndianPolicy()));

        return Optional.empty();
    }
}
