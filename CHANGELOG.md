# Changelog

All notable changes to this project will be documented in this file.

## [3.12.0] - 2025-08-11
### Added
- Single-annotation checksum: `@Checksum(start, length, offset, type, byteOrder)` with encode auto-write and decode auto-verify flows.
- Algorithm coverage: `CRC8_SMBUS`, `CRC8_MAXIM`, `XOR8 (BCC)`, `LRC8`, `CRC16_MODBUS`, `CRC16_CCITT`, `CRC32C`, `CRC64_ECMA-182`, `CRC64/ISO`.

### Changed
- `ChecksumUtils` now reuses singleton engines and throws `CodecException` for unsupported types.
- ChecksumFlow supports 8/16/32/64-bit checksum read/write and decode-time validation.
- Expanded documentation: annotation mapping, byte/bit order, checksum/CRC, formulas, arrays & strings, no-annotation APIs, FAQ.

### Breaking
- `@Checksum` parameters changed to `start`, `length`, `offset`. The checksum field no longer requires a `@UIntXType` solely to provide offset.

## [3.11.0] - 2025-06-08
### Added
- Added optional CRC checksum verification.
- Default to the host's native byte order.

### Fixed
- Fixed incorrect endianness when decoding doubles.

### Changed
- Expanded documentation with additional examples.

## [3.10.3] - 2024
### Fixed
- Fixed annotation processing issue when using `@DecodingIgnore` or `@EncodingIgnore` on Type fields.

## [3.10.2] - 2024
### Changed
- Split former `CodecUtils` into dedicated `DecodeUtils` and `EncodeUtils` helpers.
- Updated README with usage examples for the new utilities.

## [3.10.1] - 2024
### Fixed
- Fixed invalid values produced by the calendar codec.

## [3.9.2] - 2023
### Changed
- Refactored codec implementation for lower allocation and faster parsing.

## [3.9.1] - 2023
### Added
- Enhanced support for Scala annotations for better interop.

## [3.9.0] - 2023
### Added
- Added `AsciiType` and `CharType` data types for text processing.
- `Decoder.mapTo` now accepts mapping functions for custom conversion.

## [3.8.3] - 2022-12-23
### Changed
- Simplified `Decoder` API with new convenience methods.

## [3.8.2] - 2022-12-20
### Changed
- Expanded manual with more examples and diagrams.

## [3.8.1] - 2022-11-01
### Added
- Added docs for `@EncodingIgnore` and `@DecodingIgnore` annotations.

## [3.8.0] - 2022-10-27
### Added
- Introduced codec extension points and tuned throughput.

## [3.7.1] - 2022-10-24
### Added
- Documented automatic type detection feature.

## [3.7.0] - 2022-10-18
### Changed
- Various documentation updates for API clarity.

## [3.6.2] - 2022-10-08
### Added
- Added test coverage for list types in codec mapper.

## [3.6.1] - 2022-10-07
### Added
- Now handles wrapper classes like `Integer` and `Long`.

## [3.6.0] - 2022-10-03
### Added
- Added built-in codecs for array-type fields.

## [3.5.1] - 2022-10-02
### Changed
- Merged cleanup and optimizations from the development branch.

## [3.5.0] - 2022-09-30
### Added
- Overhauled formula engine for complex expressions.

## [3.4.1] - 2022-09-28
### Fixed
- Fixed incorrect offset when parsing time types.

## [3.4.0] - 2022-09-28
### Changed
- Improved reference graph algorithm for faster lookups.

## [3.3.1] - 2022-09-23
### Changed
- More reliable reference resolving in nested structures.

## [3.3.0] - 2022-07-20
### Changed
- Renamed primary API classes to use the FastProto prefix.

## [3.2.1] - 2022-06-13
### Changed
- Minor project configuration updates.

## [3.2.0] - 2022-06-10
### Added
- Renamed annotation types for clarity.

## [3.1.3] - 2023-08-12
### Fixed
- Fixed bug where ignore annotations were not applied.

## [3.1.2] - 2021-11-30
### Changed
- Merged assorted improvements from development branch.

## [3.1.1] - 2021-11-27
### Changed
- Addressed issue #1127 with stability fixes.

## [3.1.0] - 2021-11-16
### Added
- Improved reference graph to reduce memory consumption.

## [3.0.0] - 2021-11-09
### Changed
- Major release including API changes and new features.

## [2.4.3] - 2021-11-03
### Fixed
- Fixed issue where decode or encode ignore flags were lost.

## [2.4.2] - 2021-11-01
### Changed
- Updated docs to reflect latest API changes.

## [2.4.1] - 2021-08-10
### Changed
- Adjusted build scripts for better compatibility.

## [2.4.0] - 2021-08-10
### Added
- Added support for constant-length fields.

## [2.2.0] - 2021-07-21
### Added
- Added mechanism to detect and handle circular references.

## [2.1.0] - 2021-07-13
### Added
- Added additional tests for enum serialization.

## [2.0.0] - 2021-07-07
### Added
- Introduced Chinese README for localization.

## [1.6.2] - 2021-06-22
### Changed
- Refreshed benchmark documentation.

## [1.5.2] - 2021-06-15
### Added
- Introduced protocol versioning.

## [1.4.1] - 2021-06-09
### Added
- Added automatic type inference feature.

## [1.3.0] - 2021-06-07
### Changed
- Merged updates from master branch.

## [1.2.3] - 2021-06-02
### Added
- Added full API documentation.

## [1.1.2] - 2021-05-31
### Changed
- Improved datagram boundary checks.

