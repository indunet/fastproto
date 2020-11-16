package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;

import java.lang.annotation.Annotation;
import java.util.Optional;

public interface Decoder<T> {
    default boolean validate(ValidationContext context) {
        return true;
    }

    default T cast(Object object) {
        return (T) object;
    }

    T decode(final byte[] datagram, Endian endian, Annotation dataTypeAnnotation);

    public class ValidationContext {
        Class<?> fieldType;
        Annotation dataTypeAnnotation;
        Endian endian;

        Optional<Class<?>> formulaInputType;
        Optional<Class<?>> formulaOutputType;

        protected ValidationContext() {

        }

        public static class Builder {
            ValidationContext context = new ValidationContext();

            public Builder setDataTypeAnnotation(Annotation dataTypeAnnotation) {
                context.dataTypeAnnotation = dataTypeAnnotation;

                return this;
            }

            public Builder setEndian(Endian endian) {
                context.endian = endian;

                return this;
            }

            public Builder setFieldType(Class<?> fieldType) {
                context.fieldType = fieldType;

                return this;
            }

            public Builder setFormulaInputType(Optional<Class<?>> inputType) {
                context.formulaInputType = inputType;

                return this;
            }

            public Builder setFormulaOutputType(Optional<Class<?>> outputType) {
                context.formulaOutputType = outputType;

                return this;
            }

            public ValidationContext build() {
                return this.context;
            }
        }

        public <T> T getDataTypeAnnotation(Class<T> annotationClass) {
            return (T) dataTypeAnnotation;
        }

        public Endian getEndian() {
            return endian;
        }

        public Class<?> getFieldType() {
            return fieldType;
        }

        public Optional<Class<?>> getFormulaInputType() {
            return this.formulaInputType;
        }

        public Optional<Class<?>> getFormulaOutputType() {
            return this.formulaOutputType;
        }

        public boolean hasFormula() {
            return this.formulaInputType.isPresent()
                    && this.formulaOutputType.isPresent();
        }
    }
}
