# Changelog

All notable changes to this project will be documented in this file.

## [3.11.0] - 2025-06-08
### Added
- CRC checksum support.
- Native byte order is used by default.
### Fixed
- Resolved endianness bug when reading doubles.
### Changed
- Documentation improvements.

## [3.10.3] - 2024
### Fixed
- Issue with `@DecodingIgnore` and `@EncodingIgnore` on Type fields.

## [3.10.2] - 2024
### Changed
- Replaced `CodecUtils` with `DecodeUtils` and `EncodeUtils`.
- Updated README documentation.

## [3.10.1] - 2024
### Fixed
- Calendar codec error.

## [3.9.2] - 2023
### Changed
- Refactored codec implementation for better performance.

## [3.9.1] - 2023
### Added
- Updated Scala annotation support.

## [3.9.0] - 2023
### Added
- `AsciiType` and `CharType` data types.
- Improved `Decoder.mapTo` functionality.

## [3.8.3] - 2022-12-23
### Changed
- Updated `Decoder` API for simplified usage.

## [3.8.2] - 2022-12-20
### Changed
- Improved manual documentation.

## [3.8.1] - 2022-11-01
### Added
- Documentation for ignore annotations.

## [3.8.0] - 2022-10-27
### Added
- New codec features and performance tweaks.

## [3.7.1] - 2022-10-24
### Added
- Documentation describing auto type support.

## [3.7.0] - 2022-10-18
### Changed
- Updated README and general documentation.

## [3.6.2] - 2022-10-08
### Added
- Enhanced codec mapper with list type tests.

## [3.6.1] - 2022-10-07
### Added
- Support for wrapper classes of primitives.

## [3.6.0] - 2022-10-03
### Added
- Array type codecs.

## [3.5.1] - 2022-10-02
### Changed
- Various improvements merged from development branch.

## [3.5.0] - 2022-09-30
### Added
- Updated encoding and decoding formulas.

## [3.4.1] - 2022-09-28
### Fixed
- Time type handling bug.

## [3.4.0] - 2022-09-28
### Changed
- Optimized reference graph performance.

## [3.3.1] - 2022-09-23
### Changed
- Improved reference resolving.

## [3.3.0] - 2022-07-20
### Changed
- Renamed FastProto API.

## [3.2.1] - 2022-06-13
### Changed
- Updated POM and README.

## [3.2.0] - 2022-06-10
### Added
- Renamed type annotations.

## [3.1.3] - 2023-08-12
### Fixed
- Ignore annotation bug.

## [3.1.2] - 2021-11-30
### Changed
- Development branch improvements.

## [3.1.1] - 2021-11-27
### Changed
- Resolved issue #1127.

## [3.1.0] - 2021-11-16
### Added
- Optimized reference graph.

## [3.0.0] - 2021-11-09
### Changed
- Major release with numerous improvements.

## [2.4.3] - 2021-11-03
### Fixed
- Decode/encode ignore issue.

## [2.4.2] - 2021-11-01
### Changed
- Updated documentation.

## [2.4.1] - 2021-08-10
### Changed
- Build script adjustments.

## [2.4.0] - 2021-08-10
### Added
- Fixed-length field support.

## [2.2.0] - 2021-07-21
### Added
- Circular reference handling.

## [2.1.0] - 2021-07-13
### Added
- Enum test cases.

## [2.0.0] - 2021-07-07
### Added
- Added Chinese README.

## [1.6.2] - 2021-06-22
### Changed
- Updated benchmark docs.

## [1.5.2] - 2021-06-15
### Added
- Protocol version support.

## [1.4.1] - 2021-06-09
### Added
- Auto type feature.

## [1.3.0] - 2021-06-07
### Changed
- Synced with master branch.

## [1.2.3] - 2021-06-02
### Added
- API documentation.

## [1.1.2] - 2021-05-31
### Changed
- Datagram boundary check update.

