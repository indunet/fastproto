package org.indunet.fastproto.decoder;

import lombok.Builder;
import lombok.Data;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.TypeAssist;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Deng Ran
 * @version 1.0
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
