package org.indunet.fastproto;

import org.indunet.fastproto.assist.ObjectAssist;
import org.indunet.fastproto.decoder.Decoder;
import org.indunet.fastproto.decoder.DecoderFactory;
import org.indunet.fastproto.encoder.Encoder;
import org.indunet.fastproto.encoder.EncoderFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FastProtoContext {
    DecoderFactory decoderFactory = new DecoderFactory();
    EncoderFactory encoderFactory = new EncoderFactory();
    Map<Class<?>, ObjectAssist> objectAssistMap = new HashMap<>();

    public DecoderFactory getDecoderFactory() {
        return decoderFactory;
    }

    public EncoderFactory getEncoderFactory() {
        return encoderFactory;
    }

    public Optional<ObjectAssist> getObjectAssist(final Class<?> objectClass) {
        return Optional.ofNullable(this.objectAssistMap.get(objectClass));
    }
}
