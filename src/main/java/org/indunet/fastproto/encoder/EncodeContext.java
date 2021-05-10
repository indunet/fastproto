package org.indunet.fastproto.encoder;

import lombok.Builder;
import lombok.Data;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Endian;

import java.lang.annotation.Annotation;

/**
 * @author Deng Ran
 * @version 1.0
 */
@Data
@Builder
public class EncodeContext {
    byte[] datagram;
    EndianPolicy endianPolicy;
    Annotation dataType;
    Object value;

    public <T> T getDataType(Class<T> clazz) {
        return clazz.cast(this.dataType);
    }

    public <T> T getValue(Class<T> clazz) {
        return clazz.cast(this.value);
    }
}
