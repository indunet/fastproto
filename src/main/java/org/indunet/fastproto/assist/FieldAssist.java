package org.indunet.fastproto.assist;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.decoder.Decoder;
import org.indunet.fastproto.encoder.Encoder;
import org.indunet.fastproto.util.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class FieldAssist {
    Field field;
    Annotation dataTypeAnnotation;
    Decoder<?> decoder;
    Encoder<?> encoder;
    Endian endian;
    String datagramName;
    boolean decodeIgnore;
    boolean encodeIgnore;
    FormulaAssist decodeFormulaAssist;
    FormulaAssist encodeFormulaAssist;

    protected FieldAssist() {

    }

    public static FieldAssist create(Field field, String defaultDatagramName, Endian defaultEndian) {
        FieldAssist fieldAssist = new FieldAssist();

        field.setAccessible(true);
        fieldAssist.field = field;
        fieldAssist.datagramName = ReflectUtils.getDatagramName(field, defaultDatagramName);
        fieldAssist.endian = ReflectUtils.getEndian(field, defaultEndian);
        fieldAssist.decodeIgnore = ReflectUtils.getDecodeIgnore(field);
        fieldAssist.encodeIgnore = ReflectUtils.getEncodeIgnore(field);
        fieldAssist.decodeFormulaAssist = FormulaAssist.create(ReflectUtils.getDecodeFormula(field));
        fieldAssist.encodeFormulaAssist = FormulaAssist.create(ReflectUtils.getEncodeFormula(field));

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
        Object value = this.decoder.decode(this.dataTypeAnnotation, datagram, this.endian);

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
