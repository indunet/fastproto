package org.indunet.fastproto.formula;

import java.util.function.Function;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class VoltageFormula implements Function<Byte, Double> {
    @Override
    public Double apply(Byte value) {
        return value * 0.1;
    }
}
