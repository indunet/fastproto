package org.indunet.fastproto.ros2.bag;

/**
 * Topic metadata from a rosbag2 storage file.
 */
public final class Ros2BagTopic {
    private final int id;
    private final String name;
    private final String type;
    private final String serializationFormat;
    private final String offeredQosProfiles;

    public Ros2BagTopic(int id, String name, String type, String serializationFormat, String offeredQosProfiles) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.serializationFormat = serializationFormat;
        this.offeredQosProfiles = offeredQosProfiles;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSerializationFormat() {
        return serializationFormat;
    }

    public String getOfferedQosProfiles() {
        return offeredQosProfiles;
    }
}
