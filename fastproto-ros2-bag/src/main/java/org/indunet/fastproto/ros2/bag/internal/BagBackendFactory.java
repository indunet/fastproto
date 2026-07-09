package org.indunet.fastproto.ros2.bag.internal;

import java.io.IOException;
import java.nio.file.Path;

public final class BagBackendFactory {
    private BagBackendFactory() {
    }

    public static BagBackend open(Path path) throws IOException {
        ResolvedBagStorage storage = BagPathResolver.resolve(path);
        if (storage.getKind() == BagStorageKind.SQLITE3) {
            return new Sqlite3BagBackend(storage.getStorageFile());
        }
        if (storage.getKind() == BagStorageKind.MCAP) {
            return new McapBagBackend(storage.getStorageFile());
        }
        throw new IOException("Unsupported rosbag2 storage: " + storage.getKind());
    }
}
