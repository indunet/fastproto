package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/BatteryState
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatteryState {
    public static final int POWER_SUPPLY_STATUS_UNKNOWN = 0;
    public static final int POWER_SUPPLY_STATUS_CHARGING = 1;
    public static final int POWER_SUPPLY_STATUS_DISCHARGING = 2;
    public static final int POWER_SUPPLY_STATUS_NOT_CHARGING = 3;
    public static final int POWER_SUPPLY_STATUS_FULL = 4;

    public static final int POWER_SUPPLY_HEALTH_UNKNOWN = 0;
    public static final int POWER_SUPPLY_HEALTH_GOOD = 1;
    public static final int POWER_SUPPLY_HEALTH_OVERHEAT = 2;
    public static final int POWER_SUPPLY_HEALTH_DEAD = 3;
    public static final int POWER_SUPPLY_HEALTH_OVERVOLTAGE = 4;
    public static final int POWER_SUPPLY_HEALTH_UNSPEC_FAILURE = 5;
    public static final int POWER_SUPPLY_HEALTH_COLD = 6;
    public static final int POWER_SUPPLY_HEALTH_WATCHDOG_TIMER_EXPIRE = 7;
    public static final int POWER_SUPPLY_HEALTH_SAFETY_TIMER_EXPIRE = 8;

    public static final int POWER_SUPPLY_TECHNOLOGY_UNKNOWN = 0;
    public static final int POWER_SUPPLY_TECHNOLOGY_NIMH = 1;
    public static final int POWER_SUPPLY_TECHNOLOGY_LION = 2;
    public static final int POWER_SUPPLY_TECHNOLOGY_LIPO = 3;
    public static final int POWER_SUPPLY_TECHNOLOGY_LIFE = 4;
    public static final int POWER_SUPPLY_TECHNOLOGY_NICD = 5;
    public static final int POWER_SUPPLY_TECHNOLOGY_LIMN = 6;

    private Header header;
    private float voltage;
    private float temperature;
    private float current;
    private float charge;
    private float capacity;
    private float designCapacity;
    private float percentage;
    private int powerSupplyStatus;
    private int powerSupplyHealth;
    private int powerSupplyTechnology;
    private boolean present;
    private float[] cellVoltage;
    private float[] cellTemperature;
    private String location;
    private String serialNumber;
}
