package org.indunet.fastproto.domain;

import lombok.ToString;
import org.indunet.fastproto.annotation.DecodeFormula;
import org.indunet.fastproto.annotation.type.ByteType;
import org.indunet.fastproto.annotation.type.IntegerType;
import org.indunet.fastproto.annotation.type.ShortType;
import org.indunet.fastproto.formula.VoltageFormula;

@ToString
public class Battery {
    @ShortType(byteOffset = 8)
    public short current;

    @ByteType(byteOffset = 9)
    @DecodeFormula(VoltageFormula.class)
    public double voltage;

    @IntegerType(byteOffset = 4)
    public int temperature;
}
