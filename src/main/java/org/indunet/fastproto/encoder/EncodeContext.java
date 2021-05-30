package org.indunet.fastproto.encoder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.TypeAssist;

import java.lang.annotation.Annotation;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
@Data
@Builder
public class EncodeContext {
    byte[] datagram;
    TypeAssist typeAssist;
    Object value;

    public <T> T getDataType(Class<T> clazz) {
        return clazz.cast(this.typeAssist.getDataType());
    }

    public <T> T getValue(Class<T> clazz) {
        return (T) value;
    }

    public EndianPolicy getEndianPolicy() {
        return this.typeAssist.getEndianPolicy();
    }
}
