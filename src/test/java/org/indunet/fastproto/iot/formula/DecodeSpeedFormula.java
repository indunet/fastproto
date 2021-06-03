package org.indunet.fastproto.iot.formula;

import java.util.function.Function;

/**
 * @author Deng Ran
 * @since 1.2.4
 */
public class DecodeSpeedFormula implements Function<Integer, Float> {
    @Override
    public Float apply(Integer value) {
        return value * 0.1f;
    }
}
