package org.indunet.fastproto.domain;

import lombok.Data;
import lombok.ToString;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.annotation.type.DoubleType;

@Data
@ToString
public class AirConditioner {
    protected static final int OUTDOOR_TEMPERATURE_BYTE_OFFSET = 10;
    protected static final int INDOOR_TEMPERATURE_BYTE_OFFSET = 20;

    public Compressor compressor = new Compressor();
    public Valve valve = new Valve();

    @Endian(EndianPolicy.BIG)
    @DoubleType(value = OUTDOOR_TEMPERATURE_BYTE_OFFSET)
    public double outdoorTemperature;

    @DoubleType(value = INDOOR_TEMPERATURE_BYTE_OFFSET)
    public double indoorTemperature;

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
