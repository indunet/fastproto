package org.indunet.fastproto.formula;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FormulaFactory {
    protected static Map<Class<? extends Formula>, Formula<?, ?>> formulaMap = new ConcurrentHashMap();

    public static Formula<?, ?> create(Class<? extends Formula> formulaClass) throws IllegalAccessException, InstantiationException {
        if (formulaMap.containsKey(formulaClass)) {
            return formulaMap.get(formulaClass);
        } else {
            Formula<?, ?> formula = formulaClass.newInstance();

            formulaMap.put(formulaClass, formula);
            return formula;
        }
    }
}
