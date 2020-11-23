package org.indunet.fastproto.assist;

import org.indunet.fastproto.util.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodAssist {
    Method method;
    Annotation annotation;
    Parameter[] parameters;

    protected MethodAssist() {

    }

    public static MethodAssist create(Method method) {
        MethodAssist methodAssist = new MethodAssist();

        method.setAccessible(true);
        methodAssist.method = method;
        methodAssist.annotation = ReflectUtils.getBeforeAfterCodecAnnotation(method);
        methodAssist.parameters = method.getParameters();

        return methodAssist;
    }

    public Method getMethod() {
        return method;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Object invokeMethod(Object object, Object... parameters) {
        try {
            return this.method.invoke(object, parameters);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
