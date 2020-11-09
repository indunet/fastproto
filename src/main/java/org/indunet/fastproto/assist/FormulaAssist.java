package org.indunet.fastproto.assist;

import org.indunet.fastproto.formula.Formula;
import org.indunet.fastproto.util.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FormulaAssist {
    Formula<?, ?> formula;
    Method method;
    Class<?> inputType;
    Class<?> outputType;

    protected FormulaAssist() {

    }

    public static FormulaAssist create(final String className) {
        if (className == null) {
            return null;
        }

        try {
            Class<?> clazz = Class.forName(className);
            Object formula = clazz.newInstance();

            if (formula instanceof Object == false) {
                return null;
            }

            FormulaAssist formulaAssist = new FormulaAssist();
            formulaAssist.formula = (Formula<?, ?>) formula;
            formulaAssist.method = ReflectUtils.getFormulaMethod(clazz);
            formulaAssist.method.setAccessible(true);
            formulaAssist.inputType = ReflectUtils.getFormulaGeneric(clazz)[0];
            formulaAssist.inputType = ReflectUtils.getFormulaGeneric(clazz)[1];

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Formula<?, ?> getFormula() {
        return formula;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getInputType() {
        return inputType;
    }

    public Class<?> getOutputType() {
        return outputType;
    }

    public Object invokeTransform(Object object) throws InvocationTargetException, IllegalAccessException {
        return this.method.invoke(formula, object);
    }
}
