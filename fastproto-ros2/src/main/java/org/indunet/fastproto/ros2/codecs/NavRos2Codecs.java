package org.indunet.fastproto.ros2.codecs;

import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import org.indunet.fastproto.ros2.Ros2Codec;
import org.indunet.fastproto.ros2.nav_msgs.msg.MapMetaData;
import org.indunet.fastproto.ros2.nav_msgs.msg.GridCells;
import org.indunet.fastproto.ros2.nav_msgs.msg.OccupancyGrid;
import org.indunet.fastproto.ros2.nav_msgs.msg.Odometry;
import org.indunet.fastproto.ros2.nav_msgs.msg.Path;

public final class NavRos2Codecs {
    public static final Ros2Codec<Odometry> ODOMETRY = new Ros2Codec<Odometry>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Odometry value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeString(value.getChildFrameId());
            GeometryRos2Codecs.POSE_WITH_COVARIANCE.serialize(writer, value.getPose());
            GeometryRos2Codecs.TWIST_WITH_COVARIANCE.serialize(writer, value.getTwist());
        }

        @Override
        public Odometry deserialize(Ros2CdrReader reader) {
            return Odometry.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .childFrameId(reader.readString())
                    .pose(GeometryRos2Codecs.POSE_WITH_COVARIANCE.deserialize(reader))
                    .twist(GeometryRos2Codecs.TWIST_WITH_COVARIANCE.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<MapMetaData> MAP_META_DATA = new Ros2Codec<MapMetaData>() {
        @Override
        public void serialize(Ros2CdrWriter writer, MapMetaData value) {
            BuiltinRos2Codecs.TIME.serialize(writer, value.getMapLoadTime());
            writer.writeFloat(value.getResolution());
            writer.writeUInt32(value.getWidth());
            writer.writeUInt32(value.getHeight());
            GeometryRos2Codecs.POSE.serialize(writer, value.getOrigin());
        }

        @Override
        public MapMetaData deserialize(Ros2CdrReader reader) {
            return MapMetaData.builder()
                    .mapLoadTime(BuiltinRos2Codecs.TIME.deserialize(reader))
                    .resolution(reader.readFloat())
                    .width(reader.readUInt32())
                    .height(reader.readUInt32())
                    .origin(GeometryRos2Codecs.POSE.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<Path> PATH = new Ros2Codec<Path>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Path value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            Ros2CodecSupport.writePoseStampedArray(writer, value.getPoses());
        }

        @Override
        public Path deserialize(Ros2CdrReader reader) {
            return Path.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .poses(Ros2CodecSupport.readPoseStampedArray(reader))
                    .build();
        }
    };

    public static final Ros2Codec<OccupancyGrid> OCCUPANCY_GRID = new Ros2Codec<OccupancyGrid>() {
        @Override
        public void serialize(Ros2CdrWriter writer, OccupancyGrid value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            MAP_META_DATA.serialize(writer, value.getInfo());
            writer.writeByteSequence(value.getData());
        }

        @Override
        public OccupancyGrid deserialize(Ros2CdrReader reader) {
            return OccupancyGrid.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .info(MAP_META_DATA.deserialize(reader))
                    .data(reader.readByteSequence())
                    .build();
        }
    };

    public static final Ros2Codec<GridCells> GRID_CELLS = new Ros2Codec<GridCells>() {
        @Override
        public void serialize(Ros2CdrWriter writer, GridCells value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeFloat(value.getCellWidth());
            writer.writeFloat(value.getCellHeight());
            Ros2CodecSupport.writePointArray(writer, value.getCells());
        }

        @Override
        public GridCells deserialize(Ros2CdrReader reader) {
            return GridCells.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .cellWidth(reader.readFloat())
                    .cellHeight(reader.readFloat())
                    .cells(Ros2CodecSupport.readPointArray(reader))
                    .build();
        }
    };

    private NavRos2Codecs() {
    }
}
