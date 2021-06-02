package org.indunet.fastproto.iot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.type.*;

import java.sql.Timestamp;

/**
 * @author Deng Ran
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherMetrics {
    @ShortType(0)
    short id;

    @TimestampType(2)
    Timestamp time;

    @IntegerType(10)
    int humidity;

    @FloatType(14)
    float temperature;

    @DoubleType(18)
    double pressure;
}