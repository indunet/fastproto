package org.indunet.fastproto;

import org.indunet.fastproto.decoder.Decoder;
import org.indunet.fastproto.encoder.Encoder;

import java.lang.annotation.Annotation;

public class Codec {
    protected Class<? extends Annotation> annotationClass;
    protected Decoder decoder;
    protected Encoder encoder;

    public Codec(Class<? extends Annotation> annotationClass, Decoder decoder) {
        new Codec(annotationClass, decoder, null);
    }

    public Codec(Class<? extends Annotation> annotationClass, Encoder encoder) {
        new Codec(annotationClass, null, encoder);
    }

    public Codec(Class<? extends Annotation> annotationClass, Decoder decoder, Encoder encoder) {
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
