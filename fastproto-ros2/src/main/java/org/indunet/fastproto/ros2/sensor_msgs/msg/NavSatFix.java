package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/NavSatFix
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NavSatFix {
    public static final int COVARIANCE_TYPE_UNKNOWN = 0;
    public static final int COVARIANCE_TYPE_APPROXIMATED = 1;
    public static final int COVARIANCE_TYPE_DIAGONAL_KNOWN = 2;
    public static final int COVARIANCE_TYPE_KNOWN = 3;

    private Header header;
    private NavSatStatus status;
    private double latitude;
    private double longitude;
    private double altitude;
    private double[] positionCovariance;
    private int positionCovarianceType;
}
