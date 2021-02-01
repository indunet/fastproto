package org.indunet.fastproto;

import org.indunet.fastproto.assist.ObjectAssist;

import java.util.HashMap;
import java.util.Map;

public class FastProto {
    public FastProtoContext context = new FastProtoContext();

    public void decode(final byte[] datagram, Object object) {
        ObjectAssist objectAssist = this.context.objectAssistMap.get(object.getClass());
        objectAssist.decode(datagram, object);
    }

    public void encode(final Object object, byte[] datagram) {
        ObjectAssist objectAssist = this.context.objectAssistMap.get(object.getClass());
        objectAssist.encode(object, datagram);
    }
}
