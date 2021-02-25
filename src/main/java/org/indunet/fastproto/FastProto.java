package org.indunet.fastproto;

import org.indunet.fastproto.assist.ObjectAssist;

import java.util.HashMap;
import java.util.Map;

public class FastProto {
    public FastProtoContext context = new FastProtoContext();

    public void decode(final byte[] datagram, Object object) {
        if (this.context.objectAssistMap.containsKey(object.getClass())) {
            ObjectAssist objectAssist = this.context.objectAssistMap.get(object.getClass());
            objectAssist.decode(datagram, object);
        } else {
            ObjectAssist objectAssist = ObjectAssist.create(object.getClass());
            objectAssist.decode(datagram, object);
        }
    }

    public void encode(final Object object, byte[] datagram) {
        ObjectAssist objectAssist = this.context.objectAssistMap.get(object.getClass());
        // objectAssist.encode(object, datagram);
    }
}
