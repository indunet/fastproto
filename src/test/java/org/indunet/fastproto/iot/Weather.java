package org.indunet.fastproto.iot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.EnableCompress;
import org.indunet.fastproto.annotation.type.*;
import org.indunet.fastproto.compress.CompressPolicy;

import java.sql.Timestamp;

/**
 * @author Deng Ran
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EnableCompress(value = CompressPolicy.DEFLATE, level = 2)
public class Weather {
    @UInteger8Type(0)
    int id;

    @TimestampType(2)
    Timestamp time;

    @UInteger16Type(10)
    int humidity;

    @Integer16Type(12)
    int temperature;

    @UInteger32Type(14)
    long pressure;

    @BooleanType(value = 18, bitOffset = 0)
    boolean temperatureValid;

    @BooleanType(value = 18, bitOffset = 1)
    boolean humidityValid;

    @BooleanType(value = 18, bitOffset = 2)
    boolean pressureValid;
}