package org.indunet.fastproto.encoder;

import org.indunet.fastproto.annotation.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EncoderFactory {
    Map<Class<? extends Annotation>, Encoder> encoderMap = new HashMap<>();

    public EncoderFactory() {
        this.encoderMap.put(BinaryType.class, new BinaryEncoder());
        this.encoderMap.put(BooleanType.class, new BooleanEncoder());
        this.encoderMap.put(ByteType.class, new ByteEncoder());
        this.encoderMap.put(ShortType.class, new ShortEncoder());
        this.encoderMap.put(IntegerType.class, new IntegerEncoder());
        this.encoderMap.put(LongType.class, new LongEncoder());
        this.encoderMap.put(FloatType.class, new FloatEncoder());
        this.encoderMap.put(DoubleType.class, new DoubleEncoder());
    }

    Optional<Encoder<?>> create(Class<? extends Annotation> annotationClass) {
        return Optional.ofNullable(this.encoderMap.get(annotationClass));
    }

    public void addDecoder(Class<? extends Annotation> annotationClass, Encoder<?> encoder) {
        this.encoderMap.put(annotationClass, encoder);
    }
}
