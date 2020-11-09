package org.indunet.fastproto.assist;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.AfterDecode;
import org.indunet.fastproto.annotation.BeforeDecode;
import org.indunet.fastproto.util.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectAssist {
    Class objectClass;
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

        String datagramName = ReflectUtils.getDatagramName(objectClass, "default");
        objectAssist.setDatagramName(datagramName);

        Endian endian = ReflectUtils.getEndian(objectClass, Endian.Little);
        objectAssist.setEndian(endian);

        // Field.
        for (Field field : ReflectUtils.getDataTypeField(objectClass)) {
            FieldAssist fieldAssist = FieldAssist.create(field, datagramName, endian);
            objectAssist.addFieldInfo(fieldAssist);
        }

        // Method.
        for (Method method : ReflectUtils.getMethod(objectClass)) {
            MethodAssist methodAssist = MethodAssist.create(method);
            objectAssist.addMethodInfo(methodAssist);
        }

        // Object in object.
        for (Field field : ReflectUtils.getObjectType(objectClass)) {
            objectAssist.addObjectInfo(create(field.getType()));
        }

        return objectAssist;
    }

    public static ObjectAssist create(Object object) {
        return create(object.getClass());
    }

    public void decode(Map<String, byte[]> datagramMap, Object object) throws InvocationTargetException, IllegalAccessException {
        // Before Decode.
        for (MethodAssist methodAssist : this.methodAssistList) {
            if (methodAssist.annotation instanceof BeforeDecode) {
                methodAssist.invokeMethod(object);
            }
        }

        // Field.
        for (FieldAssist fieldAssist : this.fieldAssistList) {
            if (fieldAssist.decodeIgnore == false) {
                fieldAssist.decode(datagramMap, object);
            }
        }

        // Object.
        for (ObjectAssist objectAssist : this.objectAssistList) {
            this.decode(datagramMap, object);
        }

        // After Decode.
        for (MethodAssist methodAssist : this.methodAssistList) {
            if (methodAssist.annotation instanceof AfterDecode) {
                methodAssist.invokeMethod(object);
            }
        }
    }
}
