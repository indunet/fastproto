package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.type.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DecoderFactory {
    static Map<Class<? extends Annotation>, Decoder<?>> decoderMap = new HashMap<>();

    static {
        decoderMap.put(BinaryType.class, new BinaryDecoder());
        decoderMap.put(BooleanType.class, new BooleanDecoder());
        decoderMap.put(ByteType.class, new ByteDecoder());
        decoderMap.put(ShortType.class, new ShortDecoder());
        decoderMap.put(IntegerType.class, new IntegerDecoder());
        decoderMap.put(LongType.class, new LongDecoder());
        decoderMap.put(FloatType.class, new FloatDecoder());
        decoderMap.put(DoubleType.class, new DoubleDecoder());
    }

    public static Optional<Decoder<?>> create(Class<? extends Annotation> annotationClass) {
        return Optional.ofNullable(decoderMap.get(annotationClass));
    }

    public static void addDecoder(Class<? extends Annotation> annotationClass, Decoder<?> decoder) {
        decoderMap.put(annotationClass, decoder);
    }
}
