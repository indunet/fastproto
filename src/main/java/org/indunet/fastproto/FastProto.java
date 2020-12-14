package org.indunet.fastproto;

import org.indunet.fastproto.assist.ObjectAssist;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The type Fast proto.
 */
public class FastProto {
    public FastProtoContext context = new FastProtoContext();

    /**
     * Decode.
     *
     * @param datagram the datagram
     * @param object   the object
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     */
    public void decode(final byte[] datagram, Object object) throws InvocationTargetException, IllegalAccessException {
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

    /**
     * Decode.
     *
     * @param datagramMap the datagram map
     * @param object      the object
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     */
    public void decode(Map<String, byte[]> datagramMap, Object object) throws InvocationTargetException, IllegalAccessException {
        Set<Object> objectSet = new HashSet<Object>() {
            {
                add(object);
            }
        };

        this.decode(datagramMap, objectSet);
    }

    public void decode(final Map<String, byte[]> datagramMap, Map<Object ,Object> objectMap) {

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
