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

public class ReflectUtils {
    public static Endian getEndian(final Object object) {
        if (object.getClass().isAnnotationPresent(EndianMode.class)) {
            return object.getClass().getAnnotation(EndianMode.class).value();
        } else {
            return null;
        }
    }

    public static Endian getEndian(final Object object, Endian defaultValue) {
        Endian endian = getEndian(object);

        return endian != null ? endian : defaultValue;
    }

    public static Endian getEndian(final Field field) {
        if (field.isAnnotationPresent(EndianMode.class)) {
            return field.getAnnotation(EndianMode.class).value();
        } else {
            return null;
        }
    }

    public static Endian getEndian(final Field field, Endian defaultValue) {
        Endian endian = getEndian(field);

        return endian != null ? endian : defaultValue;
    }

    public static String getDatagramName(final Object object) {
        if (object.getClass().isAnnotationPresent(Datagram.class)) {
            return object.getClass().getAnnotation(Datagram.class).value();
        } else {
            return null;
        }
    }

    public static String getDatagramName(final Object object, String defaultValue) {
        String datagramName = getDatagramName(object);

        return datagramName != null ? datagramName : defaultValue;
    }

    public static String getDatagramName(final Field field) {
        if (field.isAnnotationPresent(Datagram.class)) {
            return field.getAnnotation(Datagram.class).value();
        } else {
            return null;
        }
    }

    public static String getDatagramName(final Field field, String defaultValue) {
        String datagramName = getDatagramName(field);

        return datagramName != null ? datagramName : defaultValue;
    }

    public static String getDecodeFormula(final Field field) {
        if (!field.isAnnotationPresent(DecodeFormula.class)) {
            return null;
        }

        return field.getAnnotation(DecodeFormula.class).value();
    }

    public static String getEncodeFormula(final Field field) {
        if (field.isAnnotationPresent(EncodeFormula.class) == false) {
            return null;
        }

        return field.getAnnotation(EncodeFormula.class).value();
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

    public static Annotation getMethodAnnotation(Method method) {
        for (Annotation annotation: method.getAnnotations()) {
            if (annotation instanceof BeforeDecode
                    || annotation instanceof AfterDecode
                    || annotation instanceof BeforeEncode
                    || annotation instanceof AfterEncode) {
                return annotation;
            }
        }

        return null;
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
