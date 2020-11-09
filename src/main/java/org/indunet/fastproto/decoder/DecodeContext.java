package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;

import java.lang.annotation.Annotation;

public class DecodeContext {
    Annotation dataTypeAnnotation;
    Endian endian;
    byte[] datagram;

    Class<?> fieldType;
    Class<?> formulaInputType;
    Class<?> formulaOutputType;

    protected DecodeContext() {

    }

    public static class Builder {
        DecodeContext context = new DecodeContext();

        public Builder setDataTypeAnnotation(Annotation dataTypeAnnotation) {
            context.dataTypeAnnotation = dataTypeAnnotation;

            return this;
        }

        public Builder setEndian(Endian endian) {
            context.endian = endian;

            return this;
        }

        public Builder setDatagram(byte[] datagram) {
            context.datagram = datagram;

            return this;
        }

        public Builder setFieldType(Class<?> fieldType) {
            context.fieldType = fieldType;

            return this;
        }

        public Builder setFormulaInputType(Class<?> inputType) {
            context.formulaInputType = inputType;

            return this;
        }

        public Builder setFormulaOutputType(Class<?> outputType) {
            context.formulaOutputType = outputType;

            return this;
        }

        public DecodeContext build() {
            return this.context;
        }
    }

    public <T> T getDataTypeAnnotation(Class<T> annotationClass) {
        return (T) dataTypeAnnotation;
    }

    public Endian getEndian() {
        return endian;
    }

    public byte[] getDatagram() {
        return datagram;
    }

    public void setDatagram(byte[] datagram) {
        this.datagram = datagram;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public Class<?> getFormulaInputType() {
        return formulaInputType;
    }

    public Class<?> getFormulaOutputType() {
        return formulaOutputType;
    }

    public boolean hasFormula() {
        return this.formulaInputType != null;
    }
}
