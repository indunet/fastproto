package org.indunet.fastproto.ros2.bag.internal;

import org.indunet.fastproto.ros2.bag.Ros2BagMessage;
import org.indunet.fastproto.ros2.bag.Ros2BagTopic;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public interface BagBackend extends Closeable {
    List<Ros2BagTopic> topics();

    void forEachMessage(String topic, Consumer<Ros2BagMessage> consumer) throws IOException;
}
