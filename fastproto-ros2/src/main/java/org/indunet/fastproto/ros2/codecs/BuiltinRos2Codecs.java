package org.indunet.fastproto.ros2.codecs;

import org.indunet.fastproto.ros2.Ros2Codec;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Duration;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.internal.FixedSizeRos2Codec;

public final class BuiltinRos2Codecs {
    public static final Ros2Codec<Time> TIME = new FixedSizeRos2Codec<>(Time.class, 4, Time.SIZE);
    public static final Ros2Codec<Duration> DURATION = new FixedSizeRos2Codec<>(Duration.class, 4, Duration.SIZE);

    private BuiltinRos2Codecs() {
    }
}
