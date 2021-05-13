package org.indunet.fastproto;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class FastProto {
    protected ConcurrentHashMap<Class<?>, ClassAssist> assists = new ConcurrentHashMap<>();

    // TODO, decode method.
    public <T> T decode(byte[] datagram, Class<T> clazz) {
        Objects.requireNonNull(datagram);
        Objects.requireNonNull(clazz);

        return null;
    }

    // TODO, encode method.
    public void encode(Object object, byte[] datagram) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(datagram);
    }
}
