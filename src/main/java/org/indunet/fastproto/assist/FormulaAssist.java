package org.indunet.fastproto.assist;

import org.indunet.fastproto.formula.Formula;
import org.indunet.fastproto.formula.FormulaFactory;
import org.indunet.fastproto.util.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class FormulaAssist {
    Formula<?, ?> formula;
    Method method;
    Class<?> inputType;
    Class<?> outputType;

    protected FormulaAssist() {

    }

    public static Optional<FormulaAssist> create(final Class<? extends Formula<?, ?>> formulaClass) {
        FormulaAssist formulaAssist = new FormulaAssist();

        try {
            formulaAssist.formula = FormulaFactory.create(formulaClass);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        formulaAssist.method = ReflectUtils.getFormulaMethod(formulaClass).get();

        formulaAssist.inputType = ReflectUtils.getFormulaInputType(formulaClass).get();
        formulaAssist.inputType = ReflectUtils.getFormulaOutputType(formulaClass).get();

        return Optional.ofNullable(formulaAssist);
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

    public <T> T invokeTransform(Object object, Class<T> outputType) {
        Object value = null;

        try {
            value = this.method.invoke(formula, object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return (T) value;
    }
}
