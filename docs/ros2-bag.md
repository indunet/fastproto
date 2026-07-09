## ROS2 Bag Reading

The `fastproto-ros2-bag` module reads **rosbag2** recordings in Java without installing ROS 2. It lists topics, iterates messages in timestamp order, and decodes CDR payloads when a matching class exists in `fastproto-ros2-msg`.

### Dependency

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto-ros2-bag</artifactId>
    <version>4.2.0</version>
</dependency>
```

`fastproto-ros2-bag` depends on `fastproto-ros2-msg` transitively.

### Open a bag

A bag can be:

- A directory containing `metadata.yaml` plus storage file(s)
- A single `.db3` (sqlite3) file
- A single `.mcap` file

```java
import org.indunet.fastproto.ros2.bag.Ros2BagReader;
import org.indunet.fastproto.ros2.bag.Ros2BagMessage;
import org.indunet.fastproto.ros2.bag.Ros2BagTopic;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

Path bag = Paths.get("/path/to/my_bag"); // directory, .db3, or .mcap

try (Ros2BagReader reader = Ros2BagReader.open(bag)) {
    List<Ros2BagTopic> topics = reader.topics();
    for (Ros2BagTopic topic : topics) {
        System.out.println(topic.getName() + " -> " + topic.getType());
    }

    reader.forEachMessage(message -> {
        if (message.isDecoded()) {
            System.out.println(message.getTopic() + ": " + message.getDecodedMessage());
        } else {
            System.out.println(message.getTopic() + ": raw " + message.getPayload().length + " bytes");
        }
    });
}
```

### Filter and group

```java
List<Ros2BagMessage> headers = reader.readMessages("/imu/data");

Map<String, List<Ros2BagMessage>> byTopic = reader.readMessagesByTopic();
```

### Supported storage formats

| Format | `metadata.yaml` `storage_identifier` | Notes |
|--------|--------------------------------------|-------|
| sqlite3 | `sqlite3` | Default legacy rosbag2 backend (`.db3`) |
| MCAP | `mcap` | Modern rosbag2 default (`.mcap`) |

### Unknown message types

When `Ros2BagReader` encounters a ROS 2 type that has no Java class in `fastproto-ros2-msg`, `isDecoded()` returns `false` and the CDR bytes are available via `getPayload()`.

### Not supported

- Writing or converting bags
- DDS / live ROS 2 graph participation
- MCAP chunks compressed with zstd or lz4 (uncompressed bags work)
- Automatic code generation from `.msg` files

### Related

- [ROS2 Messages](ros2-messages.md) — CDR encode/decode for standard message types.
