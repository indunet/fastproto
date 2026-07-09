package org.indunet.fastproto.ros2.bag.internal;

import java.nio.file.Path;

final class ResolvedBagStorage {
    private final BagStorageKind kind;
    private final Path storageFile;

    ResolvedBagStorage(BagStorageKind kind, Path storageFile) {
        this.kind = kind;
        this.storageFile = storageFile;
    }

    BagStorageKind getKind() {
        return kind;
    }

    Path getStorageFile() {
        return storageFile;
    }
}
