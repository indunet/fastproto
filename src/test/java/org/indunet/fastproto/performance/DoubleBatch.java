package org.indunet.fastproto.performance;

import org.indunet.fastproto.annotation.type.DoubleType;

/**
 * @author Deng Ran
 * @since 1.4.0
 */
public class DoubleBatch {
    protected static final int BYTE_OFFET = 64;

    @DoubleType(value = BYTE_OFFET)
    Double d01;

    @DoubleType(value = BYTE_OFFET + 8)
    Double d02;

    @DoubleType(value = BYTE_OFFET + 8 * 2)
    Double d03;

    @DoubleType(value = BYTE_OFFET + 8 * 3)
    Double d04;

    @DoubleType(value = BYTE_OFFET + 8 * 4)
    Double d05;

    @DoubleType(value = BYTE_OFFET + 8 * 5)
    Double d06;

    @DoubleType(value = BYTE_OFFET + 8 * 6)
    Double d07;

    @DoubleType(value = BYTE_OFFET + 8 * 7)
    Double d08;
}
