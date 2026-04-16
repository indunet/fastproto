package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
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

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeFloat(this.getVoltage());
                    writer.writeFloat(this.getTemperature());
                    writer.writeFloat(this.getCurrent());
                    writer.writeFloat(this.getCharge());
                    writer.writeFloat(this.getCapacity());
                    writer.writeFloat(this.getDesignCapacity());
                    writer.writeFloat(this.getPercentage());
                    writer.writeUInt8(this.getPowerSupplyStatus());
                    writer.writeUInt8(this.getPowerSupplyHealth());
                    writer.writeUInt8(this.getPowerSupplyTechnology());
                    writer.writeBool(this.isPresent());
                    writer.writeFloatSequence(Ros2CodecSupport.safeFloatArray(this.getCellVoltage()));
                    writer.writeFloatSequence(Ros2CodecSupport.safeFloatArray(this.getCellTemperature()));
                    writer.writeString(this.getLocation() == null ? "" : this.getLocation());
                    writer.writeString(this.getSerialNumber() == null ? "" : this.getSerialNumber());
    }

    public static BatteryState readFrom(Ros2CdrReader reader) {
                    return BatteryState.builder()
                            .header(Header.readFrom(reader))
                            .voltage(reader.readFloat())
                            .temperature(reader.readFloat())
                            .current(reader.readFloat())
                            .charge(reader.readFloat())
                            .capacity(reader.readFloat())
                            .designCapacity(reader.readFloat())
                            .percentage(reader.readFloat())
                            .powerSupplyStatus(reader.readUInt8())
                            .powerSupplyHealth(reader.readUInt8())
                            .powerSupplyTechnology(reader.readUInt8())
                            .present(reader.readBool())
                            .cellVoltage(reader.readFloatSequence())
                            .cellTemperature(reader.readFloatSequence())
                            .location(reader.readString())
                            .serialNumber(reader.readString())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, BatteryState::writeTo);
    }

    public static BatteryState decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, BatteryState::readFrom);
    }
}
