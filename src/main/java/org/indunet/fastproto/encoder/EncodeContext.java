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
 * @version 1.0
 */
@Getter
@Builder
public class EncodeContext<T> {
    byte[] datagram;
    T value;

    TypeAssist typeAssist;

    protected EncodeContext() {

    }

    public <T> T getDataType(Class<T> clazz) {
        return clazz.cast(this.typeAssist.getDataType());
    }
}
