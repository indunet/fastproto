package org.indunet.fastproto.ros2.nav_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * nav_msgs/msg/GridCells
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GridCells {
    private Header header;
    private float cellWidth;
    private float cellHeight;
    private Point[] cells;
}
