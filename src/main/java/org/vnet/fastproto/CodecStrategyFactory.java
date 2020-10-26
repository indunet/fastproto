package org.vnet.fastproto;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class CodecStrategyFactory {
    public static Map<Class<? extends Annotation>, CodecStrategy> create() {
        Map<Class<? extends Annotation>, CodecStrategy> strategyMap = new HashMap<>();

        return strategyMap;
    }
}
