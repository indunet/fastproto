package org.indunet.fastproto;

import org.indunet.fastproto.assist.ObjectAssist;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FastProto {
    public FastProtoContext context = new FastProtoContext();

    public void     decode(final byte[] datagram, Object object) throws InvocationTargetException, IllegalAccessException {
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

    public void decode(Map<String, byte[]> datagramMap, Object object) throws InvocationTargetException, IllegalAccessException {
        Set<Object> objectSet = new HashSet<Object>() {
            {
                add(object);
            }
        };

        this.decode(datagramMap, objectSet);
    }

    public void decode(final Map<String, byte[]> datagramMap, Map<String ,Object> objectMap) {

    }

    public void decode(Map<String, byte[]> datagramMap, Set<Object> objectSet) throws InvocationTargetException, IllegalAccessException {
        for (Object object: objectSet) {
            if (!this.context.objectAssistMap.containsKey(object.getClass())) {
                this.context.objectAssistMap.put(object.getClass(), ObjectAssist.create(object.getClass()));
            }

            ObjectAssist objectAssist = this.context.objectAssistMap.get(object.getClass());
            objectAssist.decode(datagramMap, object);
        }
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
}
