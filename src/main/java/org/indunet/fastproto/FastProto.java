package org.indunet.fastproto;

import org.indunet.fastproto.decoder.Decoder;
import org.indunet.fastproto.formula.Formula;
import org.indunet.fastproto.util.FieldInfo;
import org.indunet.fastproto.util.MethodInfo;
import org.indunet.fastproto.util.ObjectInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FastProto {
    public FastProtoContext context = new FastProtoContext();

    public void decode(final byte[] datagram, Object object) {
        Map<String, byte[]> datagramMap = new HashMap<String, byte[]>() {
            {
                put("default", datagram);
            }
        };

        Set<Object> objectSet = new HashSet<Object>() {
            {
                add(object);
            }
        };

        this.decode(datagramMap, objectSet);
    }

    public void decode(Map<String, byte[]> datagramMap, Object object) {
        Set<Object> objectSet = new HashSet<Object>() {
            {
                add(object);
            }
        };

        this.decode(datagramMap, objectSet);
    }

    public void decode(Map<String, byte[]> datagramMap, Set<Object> objectSet) {

    }

    private void decode(FastProtoContext context, Object object, Map<String, byte[]> datagramMap, Set<Object> objectSet) {

    }

    public void encode(final Object object, byte[] datagram) {
        Map<String, byte[]> datagramMap = new HashMap<String, byte[]>() {
            {
                put("default", datagram);
            }
        };

        Set<Object> objectSet = new HashSet<Object>() {
            {
                add(object);
            }
        };

        this.encode(objectSet, datagramMap);
    }

    public void encode(Set<Object> objectSet, byte[] datagram) {
        Map<String, byte[]> datagramMap = new HashMap<String, byte[]>() {
            {
                put("default", datagram);
            }
        };

        this.encode(objectSet, datagramMap);
    }

    public void encode(Set<Object> objectSet, Map<String, byte[]> datagramMap) {

    }

    public void encode(FastProtoContext context, Object object, Set<Object> objectSet, Map<String, byte[]> datagramMap) {

    }

    protected void decode(Object object, ObjectInfo objectInfo) throws InvocationTargetException, IllegalAccessException {
        // Before decode.
        for (MethodInfo methofInfo: objectInfo.getMethodInfoList()) {
            methofInfo.getMethod().invoke(object);
        }

        // Decode field
        for (FieldInfo fieldInfo: objectInfo.getFieldInfoList()) {
            Decoder<?> decoder = this.context.getCodecStrategy(fieldInfo.dataTypeAnno.annotationType()).decoder;

            // Object value = decoder.decode(datagram, endian, Annotation);
            Object value = 10;

            if (fieldInfo.getDecodeFormulaName() == null) {

            } else {
                Formula<?, ?> formula = this.context.getFormula(fieldInfo.getDecodeFormulaName());

                // formula.transform(value);

                fieldInfo.getDecodeFormula().getMethod().invoke(value);
            }

            fieldInfo.getField().set(object, value);
        }

        // Decode object
        for (ObjectInfo childObjectInfo: objectInfo.getObjectInfoList()) {
            // recursion.
        }

        // After decode.
        for (MethodInfo methodInfo: objectInfo.getMethodInfoList()) {

        }
    }
}
