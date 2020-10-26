package org.vnet.fastproto;

import org.vnet.fastproto.flow.FlowFactory;
import org.vnet.fastproto.flow.codec.AbstractCodecFlow;

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
        FastProtoSession session = new FastProtoSession(datagramMap, objectSet);

        for (Object object : objectSet) {
            session.setCurrentObject(object);

            if (this.context.containsFlowStrategy(object.getClass()) == false) {
                FlowFactory.createAnalyseFlow().handle(context, object);
            }

            CodecFlowStrategy flowStrategy = this.context.getFlowStrategy(object.getClass());
            AbstractCodecFlow decodeFlow = flowStrategy.getDecodeFlow();
            decodeFlow.handle(context, session);
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
        FastProtoSession session = new FastProtoSession(datagramMap, objectSet);

        for (Object object : objectSet) {
            if (this.context.containsFlowStrategy(object.getClass()) == false) {
                FlowFactory.createAnalyseFlow().handle(context, object);
            }

            CodecFlowStrategy flowStrategy = this.context.getFlowStrategy(object.getClass());
            AbstractCodecFlow encodeFlow = flowStrategy.getEncodeFlow();
            encodeFlow.handle(context, session);
        }
    }
}
