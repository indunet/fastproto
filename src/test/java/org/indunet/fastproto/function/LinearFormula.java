package org.indunet.fastproto.function;

import org.indunet.fastproto.formula.Formula;

/**
 * @author
 * @version 1.0
 */
public class LinearFormula implements Formula<Integer, Double> {
    @Override
    public Double transform(Integer input) {
        return input * 0.1;
    }
}
