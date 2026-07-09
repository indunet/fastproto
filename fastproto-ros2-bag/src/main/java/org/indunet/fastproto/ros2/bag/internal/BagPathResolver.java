package org.indunet.fastproto.ros2.bag.internal;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

final class BagPathResolver {
    private BagPathResolver() {
    }

    static ResolvedBagStorage resolve(Path path) throws IOException {
        if (Files.isRegularFile(path)) {
            return resolveFile(path);
        }

        if (!Files.isDirectory(path)) {
            throw new IOException("rosbag2 path does not exist: " + path);
        }

        Path metadata = path.resolve("metadata.yaml");
        if (Files.exists(metadata)) {
            ResolvedBagStorage fromMetadata = storageFromMetadata(path, metadata);
            if (fromMetadata != null) {
                if (!Files.isRegularFile(fromMetadata.getStorageFile())) {
                    throw new IOException("rosbag2 metadata references missing "
                            + storageLabel(fromMetadata.getKind()) + " file: "
                            + fromMetadata.getStorageFile());
                }
                return fromMetadata;
            }
        }

        Path sqlite = firstMatch(path, "*.db3");
        if (sqlite != null) {
            return new ResolvedBagStorage(BagStorageKind.SQLITE3, sqlite);
        }

        Path mcap = firstMatch(path, "*.mcap");
        if (mcap != null) {
            return new ResolvedBagStorage(BagStorageKind.MCAP, mcap);
        }

        throw new IOException("No rosbag2 .db3 or .mcap storage file found under: " + path);
    }

    private static ResolvedBagStorage resolveFile(Path path) throws IOException {
        String fileName = path.getFileName().toString().toLowerCase();
        if (fileName.endsWith(".db3")) {
            return new ResolvedBagStorage(BagStorageKind.SQLITE3, path);
        }
        if (fileName.endsWith(".mcap")) {
            return new ResolvedBagStorage(BagStorageKind.MCAP, path);
        }
        throw new IOException("Unsupported rosbag2 storage file: " + path);
    }

    @SuppressWarnings("unchecked")
    private static ResolvedBagStorage storageFromMetadata(Path bagDirectory, Path metadata) throws IOException {
        Yaml yaml = new Yaml();
        Object loaded;
        try (InputStream inputStream = Files.newInputStream(metadata)) {
            loaded = yaml.load(inputStream);
        }

        if (!(loaded instanceof Map)) {
            return null;
        }

        Object rosbag2BagfileInformation = ((Map<String, Object>) loaded).get("rosbag2_bagfile_information");
        if (!(rosbag2BagfileInformation instanceof Map)) {
            return null;
        }

        Map<String, Object> information = (Map<String, Object>) rosbag2BagfileInformation;
        Object storageIdentifier = information.get("storage_identifier");
        BagStorageKind kind = null;
        if (storageIdentifier != null) {
            String identifier = storageIdentifier.toString();
            if ("sqlite3".equals(identifier)) {
                kind = BagStorageKind.SQLITE3;
            } else if ("mcap".equals(identifier)) {
                kind = BagStorageKind.MCAP;
            } else {
                throw new IOException("Unsupported rosbag2 storage identifier: " + identifier);
            }
        }

        Object relativeFilePaths = information.get("relative_file_paths");
        if (!(relativeFilePaths instanceof List)) {
            return null;
        }

        for (Object relativeFilePath : (List<Object>) relativeFilePaths) {
            if (relativeFilePath == null) {
                continue;
            }

            String relativePath = relativeFilePath.toString();
            BagStorageKind resolvedKind = kind;
            if (resolvedKind == null) {
                if (relativePath.endsWith(".db3")) {
                    resolvedKind = BagStorageKind.SQLITE3;
                } else if (relativePath.endsWith(".mcap")) {
                    resolvedKind = BagStorageKind.MCAP;
                } else {
                    continue;
                }
            } else if (resolvedKind == BagStorageKind.SQLITE3 && !relativePath.endsWith(".db3")) {
                continue;
            } else if (resolvedKind == BagStorageKind.MCAP && !relativePath.endsWith(".mcap")) {
                continue;
            }

            return new ResolvedBagStorage(resolvedKind, bagDirectory.resolve(relativePath));
        }

        return null;
    }

    private static Path firstMatch(Path directory, String glob) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, glob)) {
            for (Path candidate : stream) {
                return candidate;
            }
        }
        return null;
    }

    private static String storageLabel(BagStorageKind kind) {
        if (kind == BagStorageKind.MCAP) {
            return "MCAP";
        }
        return "SQLite";
    }
}
