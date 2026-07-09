# rosbag2 test fixtures

These files are copied from the official ROS 2 `rosbag2` repository test resources.

| Path | Source | Size |
|------|--------|------|
| `sqlite3/cdr_test/` | [rosbag2_tests/resources/sqlite3/cdr_test](https://github.com/ros2/rosbag2/tree/rolling/rosbag2_tests/resources/sqlite3/cdr_test) | ~28 KB |
| `mcap/cdr_test/` | [rosbag2_tests/resources/mcap/cdr_test](https://github.com/ros2/rosbag2/tree/rolling/rosbag2_tests/resources/mcap/cdr_test) | ~10 KB |

- **License:** Apache-2.0 (same as `ros2/rosbag2`)
- **Purpose:** integration tests for real `ros2 bag record` sqlite3 and MCAP storage
- **Topics:** `/test_topic` (`test_msgs/msg/BasicTypes`), `/array_topic` (`test_msgs/msg/Arrays`)
- **Messages:** 7 CDR payloads per bag

FastProto does not ship Java classes for `test_msgs`, so integration tests validate storage compatibility and raw payload access rather than decoded message objects.
