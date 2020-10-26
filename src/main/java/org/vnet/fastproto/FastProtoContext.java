package org.vnet.fastproto;

import org.vnet.fastproto.decoder.Decoder;
import org.vnet.fastproto.encoder.Encoder;
import org.vnet.fastproto.formula.Formula;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class FastProtoContext {
    Map<String, Formula> formulaMap = new HashMap<>();
    Map<Class, CodecFlowStrategy> flowStrategyMap = new HashMap<>();
    Map<Class<? extends Annotation>, CodecStrategy> codecStrategyMap = CodecStrategyFactory.create();

    public boolean containsFormula(String formulaName) {
        return this.formulaMap.containsKey(formulaName);
    }

    public Formula getFormula(String formulaName) {
        return this.formulaMap.getOrDefault(formulaName, null);
    }

    public boolean containsCodecStrategy(Class<? extends Annotation> annotationClass) {
        return this.codecStrategyMap.containsKey(annotationClass);
    }

    public CodecStrategy getCodecStrategy(Class<? extends Annotation> annotationClass) {
        return this.codecStrategyMap.getOrDefault(annotationClass, null);
    }

    public void setDecoder(Class<? extends Annotation> annotationClass, Decoder decoder) {
        if (this.codecStrategyMap.containsKey(annotationClass)) {
            this.codecStrategyMap.get(annotationClass).setDecoder(decoder);
        } else {
            CodecStrategy strategy = new CodecStrategy(annotationClass, decoder);
            this.codecStrategyMap.put(annotationClass, strategy);
        }
    }

    public void setEncoder(Class<? extends Annotation> annotationClass, Encoder encoder) {
        if (this.codecStrategyMap.containsKey(annotationClass)) {
            this.codecStrategyMap.get(annotationClass).setEncoder(encoder);
        } else {
            CodecStrategy strategy = new CodecStrategy(annotationClass, encoder);
            this.codecStrategyMap.put(annotationClass, strategy);
        }
    }

    public boolean containsFlowStrategy(Class<?> clazz) {
        return this.flowStrategyMap.containsKey(clazz);
    }

    public CodecFlowStrategy getFlowStrategy(Class<?> clazz) {
        return this.flowStrategyMap.getOrDefault(clazz, null);
    }
}
