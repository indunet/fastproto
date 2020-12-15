package org.indunet.fastproto.object;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.DecodeFormula;
import org.indunet.fastproto.annotation.EndianMode;
import org.indunet.fastproto.annotation.IntegerType;
import org.indunet.fastproto.annotation.ObjectType;
import org.indunet.fastproto.function.LinearFormula;

public class AirConditioner {
    @ObjectType
    Compressor compressor = new Compressor();
    @ObjectType
    Valve valve = new Valve();

    @EndianMode(Endian.Big)
    @IntegerType(byteOffset = 0)
    @DecodeFormula(LinearFormula.class)
    double temperature;
}
