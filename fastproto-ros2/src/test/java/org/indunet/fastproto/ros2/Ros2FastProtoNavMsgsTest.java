package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Pose;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PoseStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PoseWithCovariance;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Quaternion;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Twist;
import org.indunet.fastproto.ros2.geometry_msgs.msg.TwistWithCovariance;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Vector3;
import org.indunet.fastproto.ros2.nav_msgs.msg.MapMetaData;
import org.indunet.fastproto.ros2.nav_msgs.msg.GridCells;
import org.indunet.fastproto.ros2.nav_msgs.msg.OccupancyGrid;
import org.indunet.fastproto.ros2.nav_msgs.msg.Odometry;
import org.indunet.fastproto.ros2.nav_msgs.msg.Path;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Ros2FastProtoNavMsgsTest extends Ros2FastProtoTestSupport {
    @Test
    void testOdometryRoundTrip() {
        Odometry odometry = Odometry.builder()
                .header(Header.builder().stamp(Time.builder().sec(51).nanosec(52).build()).frameId("odom").build())
                .childFrameId("base_link")
                .pose(PoseWithCovariance.builder()
                        .pose(Pose.builder()
                                .position(Point.builder().x(1.0).y(2.0).z(0.0).build())
                                .orientation(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build())
                                .build())
                        .covariance(fixedCovariance(0.001))
                        .build())
                .twist(TwistWithCovariance.builder()
                        .twist(Twist.builder()
                                .linear(Vector3.builder().x(0.5).y(0.0).z(0.0).build())
                                .angular(Vector3.builder().x(0.0).y(0.0).z(0.1).build())
                                .build())
                        .covariance(fixedCovariance(0.002))
                        .build())
                .build();

        assertEquals(odometry, Ros2FastProto.decode(Ros2FastProto.encode(odometry, Ros2Codecs.ODOMETRY), Ros2Codecs.ODOMETRY));
    }

    @Test
    void testPathRoundTrip() {
        Pose[] poses = new Pose[]{
                Pose.builder().position(Point.builder().x(1.0).y(1.0).z(0.0).build()).orientation(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build()).build(),
                Pose.builder().position(Point.builder().x(2.0).y(2.0).z(0.0).build()).orientation(Quaternion.builder().x(0.0).y(0.0).z(0.1).w(0.99).build()).build()
        };
        Path path = Path.builder()
                .header(Header.builder().stamp(Time.builder().sec(93).nanosec(94).build()).frameId("map").build())
                .poses(new PoseStamped[]{
                        PoseStamped.builder().header(Header.builder().stamp(Time.builder().sec(95).nanosec(96).build()).frameId("map").build()).pose(poses[0]).build(),
                        PoseStamped.builder().header(Header.builder().stamp(Time.builder().sec(97).nanosec(98).build()).frameId("map").build()).pose(poses[1]).build()
                })
                .build();

        assertEquals(path, Ros2FastProto.decode(Ros2FastProto.encode(path, Ros2Codecs.PATH), Ros2Codecs.PATH));
    }

    @Test
    void testMapMetaDataAndOccupancyGridRoundTrip() {
        MapMetaData mapMetaData = MapMetaData.builder()
                .mapLoadTime(Time.builder().sec(99).nanosec(100).build())
                .resolution(0.05f)
                .width(128)
                .height(64)
                .origin(Pose.builder()
                        .position(Point.builder().x(-10.0).y(-5.0).z(0.0).build())
                        .orientation(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build())
                        .build())
                .build();
        OccupancyGrid occupancyGrid = OccupancyGrid.builder()
                .header(Header.builder().stamp(Time.builder().sec(101).nanosec(102).build()).frameId("map").build())
                .info(mapMetaData)
                .data(new byte[]{0, 100, -1, 50, 25, 0, -1, 100})
                .build();

        assertEquals(mapMetaData, Ros2FastProto.decode(Ros2FastProto.encode(mapMetaData, Ros2Codecs.MAP_META_DATA), Ros2Codecs.MAP_META_DATA));
        assertEquals(occupancyGrid, Ros2FastProto.decode(Ros2FastProto.encode(occupancyGrid, Ros2Codecs.OCCUPANCY_GRID), Ros2Codecs.OCCUPANCY_GRID));
    }

    @Test
    void testGridCellsRoundTrip() {
        GridCells gridCells = GridCells.builder()
                .header(Header.builder().stamp(Time.builder().sec(103).nanosec(104).build()).frameId("map").build())
                .cellWidth(0.5f)
                .cellHeight(0.5f)
                .cells(new Point[]{
                        Point.builder().x(1.0).y(2.0).z(0.0).build(),
                        Point.builder().x(1.5).y(2.5).z(0.0).build(),
                        Point.builder().x(2.0).y(3.0).z(0.0).build()
                })
                .build();

        assertEquals(gridCells, Ros2FastProto.decode(Ros2FastProto.encode(gridCells, Ros2Codecs.GRID_CELLS), Ros2Codecs.GRID_CELLS));
    }
}
