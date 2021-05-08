package org.indunet.fastproto.encoder;

import org.indunet.fastproto.annotation.type.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EncoderFactory {
    static Map<Class<? extends Annotation>, Encoder> encoderMap = new HashMap<>();

    static {
        encoderMap.put(BinaryType.class, new BinaryEncoder());
        encoderMap.put(BooleanType.class, new BooleanEncoder());
        encoderMap.put(ByteType.class, new ByteEncoder());
        encoderMap.put(ShortType.class, new ShortEncoder());
        encoderMap.put(IntegerType.class, new IntegerEncoder());
        encoderMap.put(LongType.class, new LongEncoder());
        encoderMap.put(FloatType.class, new FloatEncoder());
        encoderMap.put(DoubleType.class, new DoubleEncoder());
    }

    public static Optional<Encoder> create(Class<? extends Annotation> annotationClass) {
        return Optional.ofNullable(encoderMap.get(annotationClass));
    }

    public static void addDecoder(Class<? extends Annotation> annotationClass, Encoder encoder) {
        encoderMap.put(annotationClass, encoder);
    }
}
