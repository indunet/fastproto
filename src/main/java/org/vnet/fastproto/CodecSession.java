package org.vnet.fastproto;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

public class CodecSession {
    public byte[] datagram;
    public Endian endian;
    public Class<? extends Annotation> dateType;
    public Set<Object> args;
    public Class<?> targetType;
}
