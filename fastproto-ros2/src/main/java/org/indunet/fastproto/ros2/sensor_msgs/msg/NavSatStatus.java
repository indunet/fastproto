package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/NavSatStatus
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NavSatStatus {
    public static final int STATUS_NO_FIX = -1;
    public static final int STATUS_FIX = 0;
    public static final int STATUS_SBAS_FIX = 1;
    public static final int STATUS_GBAS_FIX = 2;

    public static final int SERVICE_GPS = 1;
    public static final int SERVICE_GLONASS = 2;
    public static final int SERVICE_COMPASS = 4;
    public static final int SERVICE_GALILEO = 8;

    private int status;
    private int service;
}
