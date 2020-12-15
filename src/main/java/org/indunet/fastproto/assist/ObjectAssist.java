package org.indunet.fastproto.assist;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.AfterDecode;
import org.indunet.fastproto.annotation.BeforeDecode;
import org.indunet.fastproto.util.ReflectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Deng Ran
 * @version 1.0
 * @see FieldAssist,FormulaAssist,MethodAssist
 * @since 2020/10/01
 */
public class ObjectAssist {
    Class objectClass;
    Optional<Field> nestedObjectField = Optional.empty();

    Endian endian;
    String datagramName;

    List<FieldAssist> fieldAssistList = new ArrayList<>();
    List<MethodAssist> methodAssistList = new ArrayList<>();

    List<ObjectAssist> objectAssistList = new ArrayList<>();

    protected ObjectAssist() {

    }

    public void addFieldInfo(FieldAssist fieldAssist) {
        this.fieldAssistList.add(fieldAssist);
    }

    public void addMethodInfo(MethodAssist methodAssist) {
        this.methodAssistList.add(methodAssist);
    }

    public void addObjectInfo(ObjectAssist objectAssist) {
        this.objectAssistList.add(objectAssist);
    }

    public Class getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(Class objectClass) {
        this.objectClass = objectClass;
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

    public List<FieldAssist> getFieldInfoList() {
        return fieldAssistList;
    }

    public List<MethodAssist> getMethodInfoList() {
        return methodAssistList;
    }

    public List<ObjectAssist> getObjectInfoList() {
        return objectAssistList;
    }

    public static ObjectAssist create(Class<?> objectClass) {
        ObjectAssist objectAssist = new ObjectAssist();

        // Object.
        objectAssist.setObjectClass(objectClass);

        String datagramName = ReflectUtils.getDatagramName(objectClass).orElse("default");
        objectAssist.setDatagramName(datagramName);

        Endian endian = ReflectUtils.getEndian(objectClass).orElse(Endian.Little);
        objectAssist.setEndian(endian);

        List<Field> list = ReflectUtils.getDataTypeField(objectClass);

        // Field.
        ReflectUtils.getDataTypeField(objectClass).stream()
                .map(field -> FieldAssist.create(field, datagramName, endian))
                .forEach(assist -> objectAssist.addFieldInfo(assist));

        // Method.
        ReflectUtils.getBeforeAfterCodecMethod(objectClass).stream()
                .forEach(method -> {
                    MethodAssist methodAssist = MethodAssist.create(method);
                    objectAssist.addMethodInfo(methodAssist);
                });

        // Object type.
        ReflectUtils.getObjectTypeField(objectClass).stream()
                .forEach(field -> {
                    ObjectAssist assist = create(field.getType());

                    assist.nestedObjectField = Optional.of(field);
                    objectAssist.addObjectInfo(assist);
                });

        return objectAssist;
    }

    public void decode(Map<String, byte[]> datagramMap, Object object) {
        // Before decode method.
        this.methodAssistList.stream()
                .filter(assist -> assist.annotation instanceof BeforeDecode)
                .forEach(assist -> assist.invoke(object));

        // Field.
        this.fieldAssistList.stream()
                .filter(assist -> assist.decodeIgnore == false)
                .forEach(assist -> assist.decode(datagramMap, object));

        // Nested object.
        this.objectAssistList.stream()
                .forEach(assist -> assist.decode(datagramMap, assist.getNestedObject(object)));

        // After decode method.
        this.methodAssistList.stream()
                .filter(assist -> assist.annotation instanceof AfterDecode)
                .forEach(assist -> assist.invoke(object));
    }

    protected Object getNestedObject(Object object) {
        Object value = null;

        try {
            value = this.nestedObjectField.get().get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return value;
    }
}