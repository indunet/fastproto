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
public class EncodeContext<T> {
    byte[] datagram;
    T value;

    TypeAssist typeAssist;

    protected EncodeContext() {

    }

    public static <T> EncodeContext<T> create(Class<T> clazz) {
        return new EncodeContext<T>();
    }

    public EncodeContext<T> setDatagram(byte[] datagram) {
        this.datagram = datagram;

        return this;
    }

    public EncodeContext<T> setValue(Object value) {
        this.value = (T) value;

        return this;
    }

    public EncodeContext<T> setTypeAssist(TypeAssist assist) {
        this.typeAssist = assist;

        return this;
    }

    public <T> T getDataType(Class<T> clazz) {
        return clazz.cast(this.typeAssist.getDataType());
    }
}
