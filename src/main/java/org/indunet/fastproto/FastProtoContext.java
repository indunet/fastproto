package org.indunet.fastproto;

import org.indunet.fastproto.decoder.Decoder;
import org.indunet.fastproto.encoder.Encoder;
import org.indunet.fastproto.formula.Formula;
import org.indunet.fastproto.assist.ObjectAssist;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class FastProtoContext {
    Map<String, Formula> formulaMap = new HashMap<>();
    Map<Class<?>, ObjectAssist> objectInfoMap = new HashMap<>();
    Map<Class<? extends Annotation>, Codec> codecMap = CodecFactory.create();

    public boolean containsFormula(String formulaName) {
        return this.formulaMap.containsKey(formulaName);
    }

    public Formula getFormula(String formulaName) {
        return this.formulaMap.getOrDefault(formulaName, null);
    }

    public boolean containsCodecStrategy(Class<? extends Annotation> annotationClass) {
        return this.codecMap.containsKey(annotationClass);
    }

    public Codec getCodecStrategy(Class<? extends Annotation> annotationClass) {
        return this.codecMap.getOrDefault(annotationClass, null);
    }

    public void setDecoder(Class<? extends Annotation> annotationClass, Decoder decoder) {
        if (this.codecMap.containsKey(annotationClass)) {
            this.codecMap.get(annotationClass).setDecoder(decoder);
        } else {
            Codec strategy = new Codec(annotationClass, decoder);
            this.codecMap.put(annotationClass, strategy);
        }
    }

    public void setEncoder(Class<? extends Annotation> annotationClass, Encoder encoder) {
        if (this.codecMap.containsKey(annotationClass)) {
            this.codecMap.get(annotationClass).setEncoder(encoder);
        } else {
            Codec strategy = new Codec(annotationClass, encoder);
            this.codecMap.put(annotationClass, strategy);
        }
    }

    public boolean containsObjectInfo(Class<?> clazz) {
        return this.objectInfoMap.containsKey(clazz);
    }

    public ObjectAssist getObjectInfo(Class<?> clazz) {
        if (this.objectInfoMap.containsKey(clazz)) {
            return this.objectInfoMap.get(clazz);
        } else {
            ObjectAssist objectAssist = ObjectAssist.create(clazz);
            this.objectInfoMap.put(clazz, objectAssist);

            return objectAssist;
        }
    }
}
