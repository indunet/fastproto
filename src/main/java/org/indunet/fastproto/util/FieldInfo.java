package org.indunet.fastproto.util;

import org.indunet.fastproto.Endian;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldInfo {
    Field field;
    Annotation dataTypeAnno;
    Endian endian;
    String datagramName;
    boolean decodeIgnore;
    boolean encodeIgnore;
    String decodeFormulaName;
    MethodInfo decodeFormula;
    MethodInfo encodeFormula;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Annotation getDataTypeAnno() {
        return dataTypeAnno;
    }

    public void setDataTypeAnno(Annotation dataTypeAnno) {
        this.dataTypeAnno = dataTypeAnno;
    }

    public Endian getEndian() {
        return endian;
    }

    public void setEndian(Endian endian) {
        this.endian = endian;
    }

    public String getDatagramName() {
        return datagramName;
    }

    public void setDatagramName(String datagramName) {
        this.datagramName = datagramName;
    }

    public boolean isDecodeIgnore() {
        return decodeIgnore;
    }

    public void setDecodeIgnore(boolean decodeIgnore) {
        this.decodeIgnore = decodeIgnore;
    }

    public boolean isEncodeIgnore() {
        return encodeIgnore;
    }

    public void setEncodeIgnore(boolean encodeIgnore) {
        this.encodeIgnore = encodeIgnore;
    }

    public String getDecodeFormulaName() {
        return decodeFormulaName;
    }

    public void setDecodeFormulaName(String decodeFormulaName) {
        this.decodeFormulaName = decodeFormulaName;
    }

    public MethodInfo getDecodeFormula() {
        return decodeFormula;
    }

    public void setDecodeFormula(MethodInfo decodeFormula) {
        this.decodeFormula = decodeFormula;
    }

    public MethodInfo getEncodeFormula() {
        return encodeFormula;
    }

    public void setEncodeFormula(MethodInfo encodeFormula) {
        this.encodeFormula = encodeFormula;
    }
}
