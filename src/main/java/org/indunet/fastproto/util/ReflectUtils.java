package org.indunet.fastproto.util;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.decoder.Decoder;
import org.indunet.fastproto.encoder.Encoder;
import org.indunet.fastproto.formula.Formula;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReflectUtils {
    protected static final String FORMULA_METHOD = "transform";

    public static Optional<Endian> getEndian(final Class<?> objectClass) {
        return Optional.ofNullable(objectClass.getAnnotation(EndianMode.class))
                .map(annotation -> annotation.value());
    }

    public static Optional<Endian> getEndian(final Field field) {
        return Optional.ofNullable(field.getAnnotation(EndianMode.class))
                .map(annotation -> annotation.value());
    }

    public static Optional<Field> getPrimaryKeyField(final Class<?> objectClass) {
        return Arrays.stream(objectClass.getDeclaredFields())
                // .filter(field -> field.isAnnotationPresent(PrimaryKey.class))
                .peek(field -> field.setAccessible(true))
                .findFirst();
    }

    public static Optional<String> getPrimaryKeyFieldValue(final Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                // .filter(field -> field.isAnnotationPresent(PrimaryKey.class))
                .peek(field -> field.setAccessible(true))
                .map(field -> {
                    try {
                        return field.get(object).toString();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    return null;
                })
                .findFirst();
    }

//    public static boolean isPrimaryKeyField(final Field field) {
//        return field.isAnnotationPresent(PrimaryKey.class);
//    }

    public static Optional<String> getDatagramName(final Class<?> objectClass) {
        return Optional.ofNullable(objectClass.getAnnotation(Datagram.class))
                .map(annotation -> annotation.value());
    }

    public static Optional<String> getDatagramName(final Field field) {
        return Optional.ofNullable(field.getAnnotation(Datagram.class))
                .map(annotation -> annotation.value());
    }

    public static Optional<Class<? extends Formula>> getDecodeFormula(final Field field) {
        return Optional.ofNullable(field.getAnnotation(DecodeFormula.class))
                .filter(annotation -> Formula.class.isAssignableFrom(annotation.value()))
                .map(annotation -> annotation.value());
    }

    public static Optional<Class<? extends Formula>> getEncodeFormula(final Field field) {
        return Optional.ofNullable(field.getAnnotation(EncodeFormula.class))
                .filter(annotation -> Formula.class.isAssignableFrom(annotation.value()))
                .map(annotation -> annotation.value());
    }

    public static List<Method> getBeforeAfterCodecMethod(final Class<?> objectClass) {
        return Arrays.stream(objectClass.getMethods())
                .filter(method -> method.isAnnotationPresent(BeforeDecode.class))
                .filter(method -> method.isAnnotationPresent(AfterDecode.class))
                .filter(method -> method.isAnnotationPresent(BeforeEncode.class))
                .filter(method -> method.isAnnotationPresent(AfterEncode.class))
                .peek(method -> method.setAccessible(true))
                .collect(Collectors.toList());
    }

    public static Annotation getBeforeAfterCodecAnnotation(final Method method) {
        return Arrays.stream(method.getAnnotations())
                .filter(annotation -> annotation instanceof BeforeDecode
                        || annotation instanceof BeforeDecode
                        || annotation instanceof BeforeDecode
                        || annotation instanceof AfterEncode)
                .findFirst()
                .get();
    }

    public static Optional<Class<?>> getFormulaInputType(final Class<? extends Formula> formulaClass) {
        return Arrays.stream(formulaClass.getDeclaredMethods())
                .filter(method -> method.getName().equals(FORMULA_METHOD))
                .flatMap(method -> Arrays.stream(method.getParameterTypes()))
                .findAny();
    }

    public static Optional<? extends Class<?>> getFormulaOutputType(final Class<? extends Formula> formulaClass) {
        return Arrays.stream(formulaClass.getDeclaredMethods())
                .filter(method -> method.getName().equals(FORMULA_METHOD))
                .map(method -> method.getReturnType())
                .findAny();
    }

    public static Optional<Method> getFormulaMethod(final Class<? extends Formula> formulaClass) {
        return Arrays.stream(formulaClass.getDeclaredMethods())
                .filter(method -> method.getName().equals(FORMULA_METHOD))
                .findAny();
    }

    public static boolean isDecodeIgnore(final Field field) {
        return field.isAnnotationPresent(DecodeIgnore.class);
    }

    public static boolean isEncodeIgnore(final Field field) {
        return field.isAnnotationPresent(EncodeIgnore.class);
    }

    public static List<Field> getDataTypeField(final Class<?> objectClass) {
        return Arrays.stream(objectClass.getDeclaredFields())
                .filter(field -> Arrays.stream(field.getAnnotations())
                        .filter(annotation -> annotation.annotationType().isAnnotationPresent(DataType.class))
                        .findAny()
                        .isPresent())
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toList());
    }

    public static Optional<Annotation> getDataTypeAnnotation(final Field field) {
        return Arrays.stream(field.getAnnotations())
                .filter(annotation -> annotation.annotationType().isAnnotationPresent(DataType.class))
                .findFirst();
    }

    public static List<Field> getObjectTypeField(final Class<?> objectClass) {
        return Arrays.stream(objectClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ObjectType.class))
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toList());
    }

    public static Optional<Class<?>> getDecoderOutputType(final Decoder<?> decoder) {
        try {
            return Optional.ofNullable(decoder
                    .getClass().getMethod("decode", byte[].class, Endian.class, Annotation.class).getReturnType());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<Class<?>> getEncoderInputType(final Encoder<?> encoder) {
        // The last parameter is input value.
//        try {
            // Class<?>[] parameterTypes = encoder.getClass().getMethod("encode", byte[].class, Endian.class, Annotation.class, ).getParameterTypes();

            // return Optional.ofNullable(parameterTypes[parameterTypes.length - 1]);
            return Optional.empty();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//            return Optional.empty();
//        }
    }
}