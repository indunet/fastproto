package org.indunet.fastproto;

import org.indunet.fastproto.decoder.BooleanDecoder;
import org.indunet.fastproto.decoder.Decoders;
import org.indunet.fastproto.decoder.TypeDecoder;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class FastProto {
    protected ConcurrentHashMap<Class<?>, TypeAssist> assists = new ConcurrentHashMap<>();

    // TODO, decode method.
    public <T> T decode(byte[] datagram, Class<T> clazz) {
        Objects.requireNonNull(datagram);
        Objects.requireNonNull(clazz);


//        TypeAssist.create(clazz)
//                .toDecodeContexts(datagram).stream()
//                .peek(c -> {
//                    try {
//                        Class<? extends TypeDecoder> cc = null;
//
//
//                    } catch (InstantiationException e) {
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                });

        return null;
    }

    // TODO, encode method.
    public void encode(Object object, byte[] datagram) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(datagram);
    }
}
