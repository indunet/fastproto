package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Vector3;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/MagneticField
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MagneticField {
    private Header header;
    private Vector3 magneticField;
    private double[] magneticFieldCovariance;
}
