package org.indunet.fastproto.encoder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.indunet.fastproto.EndianPolicy;

import java.lang.annotation.Annotation;

/**
 * @author Deng Ran
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
public class EncodeContext<T> {
    Object object;
    byte[] datagram;
    T value;

    EndianPolicy endianPolicy;
    Annotation dataType;

    protected EncodeContext() {

    }

    public static <T> EncodeContext<T> create(Class<T> clazz) {
        return new EncodeContext<T>();
    }

    public <T> T getDataType(Class<T> clazz) {
        return clazz.cast(this.dataType);
    }
}
