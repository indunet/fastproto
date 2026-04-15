package org.indunet.fastproto.ros2.nav_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * nav_msgs/msg/OccupancyGrid
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OccupancyGrid {
    private Header header;
    private MapMetaData info;
    private byte[] data;
}
