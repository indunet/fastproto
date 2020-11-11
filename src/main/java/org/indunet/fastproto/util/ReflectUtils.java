package org.indunet.fastproto.util;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.formula.Formula;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ReflectUtils {
    public static Optional<Endian> getEndian(final Class<?> objectClass) {
        if (objectClass.isAnnotationPresent(EndianMode.class)) {
            return Optional.of(objectClass.getAnnotation(EndianMode.class).value());
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Endian> getEndian(final Field field) {
        if (field.isAnnotationPresent(EndianMode.class)) {
            return Optional.of(field.getAnnotation(EndianMode.class).value());
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Field> getPrimaryKeyField(final Class<?> objectClass) {
        return Arrays.stream(objectClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(PrimaryKey.class))
                .findFirst();
    }

    public static Optional<String> getPrimaryKeyValue(final Object object, final Field primaryKeyField) {
        try {
            return Optional.ofNullable(primaryKeyField.get(object).toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Optional<String> getDatagramName(final Class<?> objectClass) {
        if (objectClass.isAnnotationPresent(Datagram.class)) {
            return Optional.of(objectClass.getAnnotation(Datagram.class).value());
        } else {
            return Optional.empty();
        }
    }

    public static Optional<String> getDatagramName(final Field field) {
        if (field.isAnnotationPresent(Datagram.class)) {
            return Optional.of(field.getAnnotation(Datagram.class).value());
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Class<? extends Formula>> getDecodeFormula(final Field field) {
        if (!field.isAnnotationPresent(DecodeFormula.class)) {
            return Optional.of(field.getAnnotation(DecodeFormula.class).value());
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Class<? extends Formula>> getEncodeFormula(final Field field) {
        if (!field.isAnnotationPresent(EncodeFormula.class)) {
            return Optional.of(field.getAnnotation(EncodeFormula.class).value());
        } else {
            return Optional.empty();
        }
    }

    public static List<Method> getMethod(final Object object) {
        List<Method> list = new ArrayList<>();

        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeDecode.class)
                    || method.isAnnotationPresent(AfterDecode.class)
                    || method.isAnnotationPresent(BeforeEncode.class)
                    || method.isAnnotationPresent(AfterEncode.class)) {
                list.add(method);
            }
        }

        return list;
    }

    public static Optional<Annotation> getMethodAnnotation(Method method) {
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation instanceof BeforeDecode
                    || annotation instanceof AfterDecode
                    || annotation instanceof BeforeEncode
                    || annotation instanceof AfterEncode) {
                return Optional.of(annotation);
            }
        }

        return Optional.empty();
    }

    public static Class[] getFormulaGeneric(final Object formula) {
        if (formula instanceof Formula == false) {
            return null;
        }

        Type[] types = formula.getClass().getGenericInterfaces();

        for (Type type: types) {
            if (type instanceof ParameterizedType  == false) {
                continue;
            } else {
                ParameterizedType parameterizedType = (ParameterizedType) type;

                return Arrays.stream(parameterizedType.getActualTypeArguments()).toArray(Class[]::new);
            }
        }

        return null;
    }

    public static boolean getDecodeIgnore(final Field field) {
        return field.isAnnotationPresent(DecodeIgnore.class);
    }

    public static boolean getEncodeIgnore(final Field field) {
        return field.isAnnotationPresent(EncodeIgnore.class);
    }

    public static List<Field> getDataTypeField(final Object object) {
        List<Field> list = new ArrayList<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation.annotationType().isAnnotationPresent(DataType.class)) {
                    list.add(field);
                }
            }
        }

        return list;
    }

    public static Annotation getDataTypeAnnotation(Field field) {
        for (Annotation annotation : field.getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(DataType.class)) {
                return annotation;
            }
        }

        return null;
    }

    public static List<Field> getObjectType(final Object object) {
        List<Field> list = new ArrayList<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ObjectType.class)) {
                list.add(field);
            }
        }

        return list;
    }

    public static Method getFormulaMethod(final Class<?> clazz) throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod("transform");
        method.setAccessible(true);

        return method;
    }
}
