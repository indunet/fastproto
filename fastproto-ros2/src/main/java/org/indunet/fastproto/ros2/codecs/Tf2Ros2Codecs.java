package org.indunet.fastproto.ros2.codecs;

import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import org.indunet.fastproto.ros2.Ros2Codec;
import org.indunet.fastproto.ros2.tf2_msgs.msg.TFMessage;

public final class Tf2Ros2Codecs {
    public static final Ros2Codec<TFMessage> TF_MESSAGE = new Ros2Codec<TFMessage>() {
        @Override
        public void serialize(Ros2CdrWriter writer, TFMessage value) {
            Ros2CodecSupport.writeTransformStampedArray(writer, value.getTransforms());
        }

        @Override
        public TFMessage deserialize(Ros2CdrReader reader) {
            return TFMessage.builder()
                    .transforms(Ros2CodecSupport.readTransformStampedArray(reader))
                    .build();
        }
    };

    private Tf2Ros2Codecs() {
    }
}
