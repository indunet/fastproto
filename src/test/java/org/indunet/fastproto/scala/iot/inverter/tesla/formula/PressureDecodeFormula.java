package org.indunet.fastproto.scala.iot.inverter.tesla.formula;

import java.util.function.Function;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class PressureDecodeFormula implements Function<Long, Double> {
    @Override
    public Double apply(Long value) {
        return value * 0.1;
    }
}