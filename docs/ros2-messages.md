## ROS2 Messages

FastProto ships optional Maven modules for **offline ROS 2 message interoperability** in Java. This is **not** a ROS 2 runtime: there is no DDS, no nodes, and no live pub/sub. Use it when you already have CDR-encoded ROS 2 payloads (for example from a recorded bag) and want plain Java objects.

### Dependency

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto-ros2-msg</artifactId>
    <version>4.2.0</version>
</dependency>
```

The main `fastproto` bundle does **not** include ROS 2 modules. Add `fastproto-ros2-msg` explicitly when you need it.

### Encode and decode

Message classes are hand-written FastProto-style POJOs with `encode()` / `decode(byte[])` helpers:

```java
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

Header header = Header.builder()
        .stamp(Time.builder().sec(12).nanosec(34L).build())
        .frameId("map")
        .build();

byte[] cdr = header.encode();
Header decoded = Header.decode(cdr);
```

### Supported message packages

| Package | Examples |
|---------|----------|
| `builtin_interfaces` | `Time`, `Duration` |
| `std_msgs` | `Header`, `String`, `Bool`, multi-array types |
| `geometry_msgs` | `Pose`, `Twist`, `Quaternion`, stamped variants |
| `sensor_msgs` | `Imu`, `Image`, `LaserScan`, `PointCloud2` |
| `nav_msgs` | `Odometry`, `OccupancyGrid`, `Path` |
| `visualization_msgs` | `Marker`, `MarkerArray` |
| `trajectory_msgs` | `JointTrajectory` |
| `shape_msgs` | `SolidPrimitive`, `Mesh` |
| `diagnostic_msgs` | `DiagnosticArray` |
| `tf2_msgs` | `TFMessage` |

### Limitations

- **Little-endian CDR only** (the common ROS 2 default).
- **Standard messages only** — workspace-specific `.msg` types are not generated automatically.
- For unknown types, keep the raw `byte[]` payload and decode manually or extend the library with your own POJOs.

### Related

- [ROS2 Bag reading](ros2-bag.md) — open sqlite3 or MCAP recordings and decode known message types.
