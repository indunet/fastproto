package org.indunet.fastproto.encoder;

import lombok.Builder;
import lombok.Data;
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
    Endian endian;
    Annotation dataType;
}
