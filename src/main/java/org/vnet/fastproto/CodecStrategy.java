package org.vnet.fastproto;

import org.vnet.fastproto.decoder.Decoder;
import org.vnet.fastproto.encoder.Encoder;

import java.lang.annotation.Annotation;

public class CodecStrategy {
    protected Class<? extends Annotation> annotationClass;
    protected Decoder decoder;
    protected Encoder encoder;

    public CodecStrategy(Class<? extends Annotation> annotationClass, Decoder decoder) {
        new CodecStrategy(annotationClass, decoder, null);
    }

    public CodecStrategy(Class<? extends Annotation> annotationClass, Encoder encoder) {
        new CodecStrategy(annotationClass, null, encoder);
    }

    public CodecStrategy(Class<? extends Annotation> annotationClass, Decoder decoder, Encoder encoder) {
        this.annotationClass = annotationClass;
        this.decoder = decoder;
        this.encoder = encoder;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public Decoder getDecoder() {
        return decoder;
    }

    public void setDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    public Encoder getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder encoder) {
        this.encoder = encoder;
    }
}
