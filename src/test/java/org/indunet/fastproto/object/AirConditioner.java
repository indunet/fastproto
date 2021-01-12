package org.indunet.fastproto.object;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.function.LinearFormula;

public class AirConditioner {
    protected static final int OUTDOOR_TEMPERATURE_BYTE_OFFSET = 0;
    protected static final int INDOOR_TEMPERATURE_BYTE_OFFSET = 2;

    @ObjectType
    Compressor compressor = new Compressor();
    @ObjectType
    Valve valve = new Valve();

    @EndianMode(Endian.Big)
    @IntegerType(byteOffset = OUTDOOR_TEMPERATURE_BYTE_OFFSET)
    @DecodeFormula(LinearFormula.class)
    double outdoorTemperature;

    @DoubleType(byteOffset = INDOOR_TEMPERATURE_BYTE_OFFSET)
    double indoorTemperature;

    public class DatagramBuilder {
        byte[] datagram;

        public DatagramBuilder(byte[] datagram) {
            this.datagram = datagram;
        }

        public DatagramBuilder setOutdoorTemperature(double temperature) {
            return this;
        }

        public DatagramBuilder setIndoorTemperature(double temperature) {
            return this;
        }

        public byte[] build() {
            return this.datagram;
        }
    }
}
