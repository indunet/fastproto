package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.std_msgs.msg.Header;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/CameraInfo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CameraInfo {
    private Header header;
    private long height;
    private long width;
    private String distortionModel;
    private double[] d;
    private double[] k;
    private double[] r;
    private double[] p;
    private long binningX;
    private long binningY;
    private RegionOfInterest roi;
}
