package org.indunet.fastproto.decoder;

import lombok.Builder;
import lombok.Data;
import org.indunet.fastproto.EndianPolicy;

import java.lang.annotation.Annotation;
import java.util.function.Function;

/**
 * @author Deng Ran
 * @version 1.0
 */
@Data
@Builder
public class DecodeContext {
    byte[] datagram;
    EndianPolicy endian;
    Annotation dateType;
    Function formula;
}
