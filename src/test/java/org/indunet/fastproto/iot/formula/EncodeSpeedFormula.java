package org.indunet.fastproto.iot.formula;

import java.util.function.Function;

/**
 * @author Chance
 * @since 1.0.0
 */
public class EncodeSpeedFormula implements Function<Float, Integer> {
    @Override
    public Integer apply(Float value) {
        return (int) (value * 10);
    }
}
