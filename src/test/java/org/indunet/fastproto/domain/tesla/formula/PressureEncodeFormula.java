package org.indunet.fastproto.domain.tesla.formula;

import java.util.function.Function;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class PressureEncodeFormula implements Function<Long, Integer> {
    @Override
    public Integer apply(Long value) {
        return (int) (value * 10);
    }
}
