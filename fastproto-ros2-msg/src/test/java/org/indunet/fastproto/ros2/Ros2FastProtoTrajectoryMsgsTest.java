package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ros2.builtin_interfaces.msg.Duration;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Quaternion;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Transform;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Twist;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Vector3;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;
import org.indunet.fastproto.ros2.trajectory_msgs.msg.JointTrajectory;
import org.indunet.fastproto.ros2.trajectory_msgs.msg.JointTrajectoryPoint;
import org.indunet.fastproto.ros2.trajectory_msgs.msg.MultiDOFJointTrajectory;
import org.indunet.fastproto.ros2.trajectory_msgs.msg.MultiDOFJointTrajectoryPoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Ros2FastProtoTrajectoryMsgsTest {
    @Test
    void testJointTrajectoryRoundTrip() {
        JointTrajectory jointTrajectory = JointTrajectory.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(21).nanosec(22).build())
                        .frameId("arm_base")
                        .build())
                .jointNames(new String[]{"joint_1", "joint_2", "joint_3"})
                .points(new JointTrajectoryPoint[]{
                        JointTrajectoryPoint.builder()
                                .positions(new double[]{0.1, 0.2, 0.3})
                                .velocities(new double[]{0.4, 0.5, 0.6})
                                .accelerations(new double[]{0.7, 0.8, 0.9})
                                .effort(new double[]{1.0, 1.1, 1.2})
                                .timeFromStart(Duration.builder().sec(1).nanosec(200_000_000L).build())
                                .build()
                })
                .build();

        assertEquals(jointTrajectory, JointTrajectory.decode(jointTrajectory.encode()));
    }

    @Test
    void testMultiDofJointTrajectoryRoundTrip() {
        MultiDOFJointTrajectory multiDofJointTrajectory = MultiDOFJointTrajectory.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(31).nanosec(32).build())
                        .frameId("map")
                        .build())
                .jointNames(new String[]{"base_link"})
                .points(new MultiDOFJointTrajectoryPoint[]{
                        MultiDOFJointTrajectoryPoint.builder()
                                .transforms(new Transform[]{
                                        Transform.builder()
                                                .translation(Vector3.builder().x(1.0).y(2.0).z(0.0).build())
                                                .rotation(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build())
                                                .build()
                                })
                                .velocities(new Twist[]{
                                        Twist.builder()
                                                .linear(Vector3.builder().x(0.5).y(0.0).z(0.0).build())
                                                .angular(Vector3.builder().x(0.0).y(0.0).z(0.2).build())
                                                .build()
                                })
                                .accelerations(new Twist[]{
                                        Twist.builder()
                                                .linear(Vector3.builder().x(0.1).y(0.0).z(0.0).build())
                                                .angular(Vector3.builder().x(0.0).y(0.0).z(0.05).build())
                                                .build()
                                })
                                .timeFromStart(Duration.builder().sec(2).nanosec(0).build())
                                .build()
                })
                .build();

        assertEquals(multiDofJointTrajectory, MultiDOFJointTrajectory.decode(multiDofJointTrajectory.encode()));
    }
}
