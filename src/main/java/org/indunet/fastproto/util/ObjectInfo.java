package org.indunet.fastproto.util;

import org.indunet.fastproto.Endian;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ObjectInfo {
    Class clazz;
    Endian endian;
    String datagramName;

    List<FieldInfo> fieldInfoList = new ArrayList<>();
    List<MethodInfo> methodInfoList = new ArrayList<>();
    List<ObjectInfo> objectInfoList = new ArrayList<>();

    protected ObjectInfo() {

    }

    public void addFieldInfo(FieldInfo fieldInfo) {
        this.fieldInfoList.add(fieldInfo);
    }

    public void addMethodInfo(MethodInfo methodInfo) {
        this.methodInfoList.add(methodInfo);
    }

    public void addObjectInfo(ObjectInfo objectInfo) {
        this.objectInfoList.add(objectInfo);
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
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

    public List<FieldInfo> getFieldInfoList() {
        return fieldInfoList;
    }

    public List<MethodInfo> getMethodInfoList() {
        return methodInfoList;
    }

    public List<ObjectInfo> getObjectInfoList() {
        return objectInfoList;
    }

    public static ObjectInfo create(Object object) {
        ObjectInfo objectInfo = new ObjectInfo();

        // Object.
        objectInfo.setClazz(object.getClass());
        objectInfo.setDatagramName(ReflectUtils.getDatagramName(object, "default"));
        objectInfo.setEndian(ReflectUtils.getEndian(object, Endian.Little));

        // Field.
        for (Field field : ReflectUtils.getDataTypeField(object)) {
            FieldInfo fieldInfo = new FieldInfo();

            fieldInfo.setField(field);
            fieldInfo.setDatagramName(ReflectUtils.getDatagramName(field, objectInfo.datagramName));
            fieldInfo.setEndian(ReflectUtils.getEndian(field, objectInfo.endian));
            fieldInfo.setDecodeIgnore(ReflectUtils.getDecodeIgnore(field));
            fieldInfo.setEncodeIgnore(ReflectUtils.getEncodeIgnore(field));
            fieldInfo.setDecodeFormulaName(ReflectUtils.getDecodeFormulaName(field));
            fieldInfo.setEncodeFormulaName(ReflectUtils.getEncodeFormulaName(field));

            objectInfo.addFieldInfo(fieldInfo);
        }

        // Method.
        for (Method method : ReflectUtils.getMethod(object)) {
            MethodInfo methodInfo = new MethodInfo();

            methodInfo.setAnnotation(ReflectUtils.getMethodAnnotation(method));
            methodInfo.setMethod(method);
            methodInfo.setParameters(method.getParameters());

            objectInfo.addMethodInfo(methodInfo);
        }

        // Object.
        for (Field field : ReflectUtils.getObjectType(object)) {
            try {
                field.setAccessible(true);
                objectInfo.addObjectInfo(create(field.get(object)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return objectInfo;
    }
}
