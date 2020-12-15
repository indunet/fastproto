package org.indunet.fastproto;

import org.indunet.fastproto.assist.ObjectAssist;

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

        this.decode(datagramMap, object);
    }

    public void decode(Map<String, byte[]> datagramMap, Object object) {
        if (!this.context.objectAssistMap.containsKey(object.getClass())) {
            this.context.objectAssistMap.put(object.getClass(), ObjectAssist.create(object.getClass()));
        }

        ObjectAssist objectAssist = this.context.objectAssistMap.get(object.getClass());
        objectAssist.decode(datagramMap, object);
    }

    public void encode(final Object object, byte[] datagram) {
        Map<String, byte[]> datagramMap = new HashMap<String, byte[]>() {
            {
                put("default", datagram);
            }
        };

        this.encode(object, datagramMap);
    }

    public void encode(final Object object, Map<String, byte[]> datagramMap) {
        if (!this.context.objectAssistMap.containsKey(object.getClass())) {
            this.context.objectAssistMap.put(object.getClass(), ObjectAssist.create(object.getClass()));
        }

        ObjectAssist objectAssist = this.context.objectAssistMap.get(object.getClass());

        // TODO
        // objectAssist.(datagramMap, object);
    }
}
