package org.indunet.fastproto.assist;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.Decoder;
import org.indunet.fastproto.encoder.EncodeContext;
import org.indunet.fastproto.encoder.Encoder;
import org.indunet.fastproto.formula.Formula;
import org.indunet.fastproto.util.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class FieldAssist {
    Field field;
    Class<?> fieldType;
    Annotation dataTypeAnnotation;
    Decoder<?> decoder;
    Class<?> decoderGeneric;
    Encoder encoder;
    Endian endian;
    String datagramName;
    boolean decodeIgnore;
    boolean encodeIgnore;

    FormulaAssist decodeFormulaAssist;
    FormulaAssist encodeFormulaAssist;

    // Context.
    DecodeContext decodeContext;
    EncodeContext encodeContext;

    protected FieldAssist() {

    }

    public static FieldAssist create(Field field, String defaultDatagramName, Endian defaultEndian) {
        FieldAssist fieldAssist = new FieldAssist();

        field.setAccessible(true);
        fieldAssist.field = field;
        fieldAssist.fieldType = field.getType();
        fieldAssist.datagramName = ReflectUtils.getDatagramName(field).orElse(defaultDatagramName);
        fieldAssist.dataTypeAnnotation = ReflectUtils.getDataTypeAnnotation(field);
        fieldAssist.endian = ReflectUtils.getEndian(field).orElse(defaultEndian);
        fieldAssist.decodeIgnore = ReflectUtils.getDecodeIgnore(field);
        fieldAssist.encodeIgnore = ReflectUtils.getEncodeIgnore(field);

        ReflectUtils.getDecodeFormula(field)
                .ifPresent(formula -> fieldAssist.decodeFormulaAssist = FormulaAssist.create(formula));
        ReflectUtils.getEncodeFormula(field)
                .ifPresent(formula -> fieldAssist.encodeFormulaAssist = FormulaAssist.create(formula));

        fieldAssist.decodeContext = new DecodeContext.Builder()
                .setFieldType(fieldAssist.fieldType)
                .setDataTypeAnnotation(fieldAssist.dataTypeAnnotation)
                .setEndian(fieldAssist.endian)
                .setFormulaInputType(fieldAssist.decodeFormulaAssist.inputType)
                .setFormulaOutputType(fieldAssist.decodeFormulaAssist.outputType)
                .build();

        fieldAssist.encodeContext = new EncodeContext.Builder()
                .setFieldType(fieldAssist.fieldType)
                .setDataTypeAnnotation(fieldAssist.dataTypeAnnotation)
                .setEndian(fieldAssist.endian)
                .setFormulaInputType(fieldAssist.encodeFormulaAssist.inputType)
                .setFormulaOutputType(fieldAssist.encodeFormulaAssist.outputType)
                .build();

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

    public FormulaAssist getDecodeFormulaInfo() {
        return decodeFormulaAssist;
    }

    public FormulaAssist getEncodeFormulaInfo() {
        return encodeFormulaAssist;
    }

    public void decode(Map<String, byte[]> datagramMap, Object object) throws IllegalAccessException, InvocationTargetException {
        byte[] datagram = datagramMap.get(this.datagramName);

        Object value = this.decoder.decode(this.decodeContext);

        if (this.decodeFormulaAssist != null) {
            value = this.decodeFormulaAssist.invokeTransform(value);
        }

        this.field.set(object, value);
    }

    public void encode(Object object, Map<String, byte[]> datagramMap) throws InvocationTargetException, IllegalAccessException {
        byte[] datagram = datagramMap.get(this.datagramName);

        if (this.encodeFormulaAssist != null) {
            Object value = this.encodeFormulaAssist.invokeTransform(field.get(object));

            // TODO
            // this.encoder.encode(datagram, this.endian, this.dataTypeAnnotation, value);
        } else {
            // this.encoder.encode(datagram, this.endian, this.dataTypeAnnotation, field.get(object));
        }
    }
}
