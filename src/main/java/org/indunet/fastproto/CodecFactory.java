package org.indunet.fastproto;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class CodecFactory {
    public static Map<Class<? extends Annotation>, Codec> create() {
        Map<Class<? extends Annotation>, Codec> strategyMap = new HashMap<>();

        return strategyMap;
    }
}
