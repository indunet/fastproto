package org.indunet.fastproto.assist;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.decoder.Decoder;
import org.indunet.fastproto.decoder.DecoderFactory;
import org.indunet.fastproto.encoder.Encoder;
import org.indunet.fastproto.encoder.EncoderFactory;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.util.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

public class FieldAssist {
    Field field;
    Class<?> fieldType;
    Annotation dataTypeAnnotation;
    boolean primaryKey;
    Optional<Object> primaryKeyValue;

    Optional<Decoder<?>> decoder;
    Optional<Class<?>> decoderOutputType = Optional.empty();
    Optional<Encoder> encoder;
    Optional<Class<?>> encoderInputType = Optional.empty();

    Endian endian;
    String datagramName;
    boolean decodeIgnore;
    boolean encodeIgnore;

    Optional<FormulaAssist> decodeFormulaAssist = Optional.empty();
    Optional<FormulaAssist> encodeFormulaAssist = Optional.empty();

    protected FieldAssist() {

    }

    public static FieldAssist create(Field field, String defaultDatagramName, Endian defaultEndian) {
        FieldAssist fieldAssist = new FieldAssist();

        field.setAccessible(true);
        fieldAssist.field = field;
        fieldAssist.fieldType = field.getType();
        fieldAssist.datagramName = ReflectUtils.getDatagramName(field).orElse(defaultDatagramName);
        fieldAssist.dataTypeAnnotation = ReflectUtils.getDataTypeAnnotation(field).get();
        fieldAssist.endian = ReflectUtils.getEndian(field).orElse(defaultEndian);
        fieldAssist.decodeIgnore = ReflectUtils.isDecodeIgnore(field);
        fieldAssist.encodeIgnore = ReflectUtils.isEncodeIgnore(field);

        fieldAssist.decoder = DecoderFactory.create(fieldAssist.dataTypeAnnotation.annotationType());
        fieldAssist.decoder.ifPresent(d -> {
            fieldAssist.decoderOutputType = ReflectUtils.getDecoderOutputType(d);
        });
        fieldAssist.encoder = EncoderFactory.create(fieldAssist.dataTypeAnnotation.annotationType());
        fieldAssist.encoder.ifPresent(d -> {
            fieldAssist.encoderInputType = ReflectUtils.getEncoderInputType(d);
        });


//        ReflectUtils.getDecodeFormula(field)
//                .ifPresent(formula -> fieldAssist.decodeFormulaAssist = FormulaAssist.create(formula));
//        ReflectUtils.getEncodeFormula(field)
//                .ifPresent(formula -> fieldAssist.encodeFormulaAssist = FormulaAssist.create(formula));

        fieldAssist.decoder
                .map(e -> e.validate(new Decoder.ValidationContext.Builder()
                        .setFieldType(fieldAssist.fieldType)
                        .setDataTypeAnnotation(fieldAssist.dataTypeAnnotation)
                        .setEndian(fieldAssist.endian)
                        .setFormulaInputType(Optional.ofNullable(fieldAssist.decodeFormulaAssist.get().inputType))
                        .setFormulaOutputType(Optional.ofNullable(fieldAssist.decodeFormulaAssist.get().outputType))
                        .build()))
                .filter(Boolean::booleanValue)
                .orElseThrow(() -> new DecodeException(""));

        fieldAssist.encoder
                .map(e -> e.validate(new Encoder.ValidationContext.Builder()
                        .setFieldType(fieldAssist.fieldType)
                        .setDataTypeAnnotation(fieldAssist.dataTypeAnnotation)
                        .setEndian(fieldAssist.endian)
                        .setFormulaInputType(fieldAssist.encodeFormulaAssist.get().inputType)
                        .setFormulaOutputType(fieldAssist.encodeFormulaAssist.get().outputType)
                        .build()))
                .filter(Boolean::booleanValue)
                .orElseThrow(() -> new EncodeException(""));

        return fieldAssist;
    }

    public Field getField() {
        return field;
    }

    public Annotation getDataTypeAnnotation() {
        return dataTypeAnnotation;
    }

    public Endian getEndian() {
        return endian;
    }

    public String getDatagramName() {
        return datagramName;
    }

    public boolean isDecodeIgnore() {
        return decodeIgnore;
    }

    public boolean isEncodeIgnore() {
        return encodeIgnore;
    }

    public Optional<FormulaAssist> getDecodeFormulaAssist() {
        return decodeFormulaAssist;
    }

    public Optional<FormulaAssist> getEncodeFormulaAssist() {
        return encodeFormulaAssist;
    }

    public void decode(Map<String, byte[]> datagramMap, Object object) throws IllegalAccessException, InvocationTargetException {
        byte[] datagram = datagramMap.get(this.datagramName);
        Object value = null;

        if (this.decodeFormulaAssist.isPresent()) {
            value = decodeFormulaAssist.get().invokeTransform(
                        this.decoder.get().decode(datagram, endian, dataTypeAnnotation),
                            decodeFormulaAssist.get().getOutputType());
        } else {
            value = this.decoder.get().decode(datagram, endian, dataTypeAnnotation);
        }

        this.field.set(object, value);
    }

    public void encode(Object object, Map<String, byte[]> datagramMap) {
        byte[] datagram = datagramMap.get(this.datagramName);

        if (this.encodeFormulaAssist.isPresent()) {
            this.encoder.get().encode(
                    datagram,
                    endian,
                    dataTypeAnnotation,
                    this.encoder.get().cast(object));
        } else {
            this.encoder.get().encode(new byte[100], endian, dataTypeAnnotation, 100);
        }
    }
}
