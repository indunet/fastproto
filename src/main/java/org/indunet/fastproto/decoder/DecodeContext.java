package org.indunet.fastproto.decoder;

import lombok.Builder;
import lombok.Data;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.TypeAssist;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
@Data
@Builder
public class DecodeContext {
    Object object;
    byte[] datagram;
    TypeAssist typeAssist;

    public EndianPolicy getEndianPolicy() {
        return this.typeAssist.getEndianPolicy();
    }

    public <T> T getDataType(Class<T> clazz) {
        return clazz.cast(this.typeAssist.getDataType());
    }

    public <T> T getObject(Class<T> clazz) {
        return clazz.cast(this.object);
    }
}
