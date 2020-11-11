package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DecoderFactory {
    Map<Class<? extends Annotation>, Decoder<?>> decoderMap = new HashMap<>();

    public DecoderFactory() {
        this.decoderMap.put(BinaryType.class, new BinaryDecoder());
        this.decoderMap.put(BooleanType.class, new BooleanDecoder());
        this.decoderMap.put(ByteType.class, new ByteDecoder());
        this.decoderMap.put(ShortType.class, new ShortDecoder());
        this.decoderMap.put(IntegerType.class, new IntegerDecoder());
        this.decoderMap.put(LongType.class, new LongDecoder());
        this.decoderMap.put(FloatType.class, new FloatDecoder());
        this.decoderMap.put(DoubleType.class, new DoubleDecoder());
    }

    Optional<Decoder<?>> create(Class<? extends Annotation> annotationClass) {
        return Optional.ofNullable(this.decoderMap.get(annotationClass));
    }

    public void addDecoder(Class<? extends Annotation> annotationClass, Decoder<?> decoder) {
        this.decoderMap.put(annotationClass, decoder);
    }
}
