package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Pose;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PoseStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PoseWithCovariance;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Quaternion;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Accel;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Twist;
import org.indunet.fastproto.ros2.geometry_msgs.msg.TwistWithCovariance;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Vector3;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Wrench;
import org.indunet.fastproto.ros2.nav_msgs.msg.MapMetaData;
import org.indunet.fastproto.ros2.nav_msgs.msg.Goals;
import org.indunet.fastproto.ros2.nav_msgs.msg.GridCells;
import org.indunet.fastproto.ros2.nav_msgs.msg.OccupancyGrid;
import org.indunet.fastproto.ros2.nav_msgs.msg.Odometry;
import org.indunet.fastproto.ros2.nav_msgs.msg.Path;
import org.indunet.fastproto.ros2.nav_msgs.msg.Trajectory;
import org.indunet.fastproto.ros2.nav_msgs.msg.TrajectoryPoint;
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

        assertEquals(odometry, Odometry.decode(odometry.encode()));
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

        assertEquals(path, Path.decode(path.encode()));
    }

    @Test
    void testGoalsRoundTrip() {
        Goals goals = Goals.builder()
                .header(Header.builder().stamp(Time.builder().sec(99).nanosec(100).build()).frameId("map").build())
                .goals(new PoseStamped[]{
                        PoseStamped.builder()
                                .header(Header.builder().stamp(Time.builder().sec(101).nanosec(102).build()).frameId("map").build())
                                .pose(Pose.builder().position(Point.builder().x(1.0).y(2.0).z(0.0).build()).orientation(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build()).build())
                                .build(),
                        PoseStamped.builder()
                                .header(Header.builder().stamp(Time.builder().sec(103).nanosec(104).build()).frameId("map").build())
                                .pose(Pose.builder().position(Point.builder().x(3.0).y(4.0).z(0.0).build()).orientation(Quaternion.builder().x(0.0).y(0.0).z(0.2).w(0.98).build()).build())
                                .build()
                })
                .build();

        assertEquals(goals, Goals.decode(goals.encode()));
    }

    @Test
    void testMapMetaDataRoundTrip() {
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

        assertEquals(mapMetaData, MapMetaData.decode(mapMetaData.encode()));
    }

    @Test
    void testOccupancyGridRoundTrip() {
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

        assertEquals(occupancyGrid, OccupancyGrid.decode(occupancyGrid.encode()));
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

        assertEquals(gridCells, GridCells.decode(gridCells.encode()));
    }

    @Test
    void testTrajectoryRoundTrip() {
        Trajectory trajectory = Trajectory.builder()
                .header(Header.builder().stamp(Time.builder().sec(105).nanosec(106).build()).frameId("map").build())
                .points(new TrajectoryPoint[]{
                        TrajectoryPoint.builder()
                                .header(Header.builder().stamp(Time.builder().sec(107).nanosec(108).build()).frameId("map").build())
                                .pose(Pose.builder().position(Point.builder().x(1.0).y(1.0).z(0.0).build()).orientation(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build()).build())
                                .velocity(Twist.builder().linear(Vector3.builder().x(0.5).y(0.0).z(0.0).build()).angular(Vector3.builder().x(0.0).y(0.0).z(0.1).build()).build())
                                .acceleration(Accel.builder().linear(Vector3.builder().x(0.1).y(0.0).z(0.0).build()).angular(Vector3.builder().x(0.0).y(0.0).z(0.01).build()).build())
                                .effort(Wrench.builder().force(Vector3.builder().x(1.0).y(0.0).z(0.0).build()).torque(Vector3.builder().x(0.0).y(0.0).z(0.2).build()).build())
                                .build(),
                        TrajectoryPoint.builder()
                                .header(Header.builder().stamp(Time.builder().sec(109).nanosec(110).build()).frameId("map").build())
                                .pose(Pose.builder().position(Point.builder().x(2.0).y(1.5).z(0.0).build()).orientation(Quaternion.builder().x(0.0).y(0.0).z(0.1).w(0.99).build()).build())
                                .velocity(Twist.builder().linear(Vector3.builder().x(0.4).y(0.1).z(0.0).build()).angular(Vector3.builder().x(0.0).y(0.0).z(0.08).build()).build())
                                .acceleration(Accel.builder().linear(Vector3.builder().x(0.05).y(0.01).z(0.0).build()).angular(Vector3.builder().x(0.0).y(0.0).z(0.005).build()).build())
                                .effort(Wrench.builder().force(Vector3.builder().x(0.8).y(0.1).z(0.0).build()).torque(Vector3.builder().x(0.0).y(0.0).z(0.15).build()).build())
                                .build()
                })
                .build();

        assertEquals(trajectory, Trajectory.decode(trajectory.encode()));
    }
}
