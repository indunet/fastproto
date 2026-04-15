package org.indunet.fastproto.ros2.builtin_interfaces.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.Int32Type;
import org.indunet.fastproto.annotation.UInt32Type;

/**
 * builtin_interfaces/msg/Time
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Time {
    public static final int SIZE = 8;

    @Int32Type(offset = 0)
    private int sec;

    @UInt32Type(offset = 4)
    private long nanosec;
}
