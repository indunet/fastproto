# 更新日志

本文件用于记录本项目的所有重要变更。

## [4.1.0] - 2026-01-27
### 新增
- **fastproto 聚合模块**：新增 `fastproto-bundle` 模块（artifactId 为 `fastproto`），聚合 core 和 processor，用户只需引用一个依赖即可获得完整功能
- Bundle 模块集成测试，验证聚合后功能正常

### 移除
- 移除 `CodecProcessor` 和 `@GenerateCodec` 注解（功能价值有限）
- 移除运行时动态编译功能（`FormulaBuilder` 及相关类），lambda 公式现在完全依赖编译时代码生成

### 变更
- 项目结构调整为三模块：
  - `fastproto-core` - 核心库
  - `fastproto-processor` - 注解处理器
  - `fastproto` (bundle) - 聚合模块，推荐用户使用
- 更新文档，简化依赖引用说明

### 升级指南
从 4.0.0 升级的用户，现在只需引用一个依赖：

**Maven:**
```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>4.1.0</version>
</dependency>
```

## [4.0.0] - 2026-01-26
### 重大变更
- **多模块重构**：项目拆分为两个模块：
  - `fastproto` - 核心库，包含所有运行时功能（artifactId 保持向后兼容）
  - `fastproto-processor` - 注解处理器，用于编译时公式类生成
- 动态 lambda 编译默认禁用，以提升 Android/JRE 兼容性

### 新增
- `fastproto-processor` 模块，支持 SPI 自动发现注解处理器
- `FormulaProcessor` - 在编译时为 `@DecodingFormula(lambda="...")` 和 `@EncodingFormula(lambda="...")` 生成 `Function` 实现类
- `CodecProcessor` - 为 `@GenerateCodec` 注解的类生成编解码包装器
- `FormulaRegistry` - 运行时查找编译时生成的公式类
- 通过注解处理器实现完整的 Android 和 Java 11+ JRE 兼容性

### 变更
- Lambda 公式现在使用编译时代码生成，而非运行时动态编译
- 使用 lambda 公式功能需要添加 `fastproto-processor` 依赖
- 更新文档以反映新的多模块结构

### 升级指南
从 3.x 升级的用户：

**Maven:**
```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>4.0.0</version>
</dependency>
<!-- 使用 lambda 公式时添加 -->
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto-processor</artifactId>
    <version>4.0.0</version>
    <scope>provided</scope>
</dependency>
```

**Gradle:**
```groovy
implementation 'org.indunet:fastproto:4.0.0'
// 使用 lambda 公式时添加
annotationProcessor 'org.indunet:fastproto-processor:4.0.0'
```

## [3.12.3] - 2025-11-23
### 新增
- 新增 `@BcdType` 注解与 `BcdCodec`，支持定长 packed BCD 整数（支持大小端），映射到 `int` / `Integer` 类型。
- 为 BCD 类型补充编解码单元测试与 FastProto 集成测试，覆盖大端/小端及异常场景。
- 新增 `@Expect` / `@Expects` 固定断言注解，可在指定偏移校验/写入常量值，支持在解码阶段自动校验，在编码阶段自动填充固定字节。

### 变更
- README 与 Quick Start：增加“传统位运算解析 vs FastProto 注解”的对比示例，并补充设计理念说明。
- 文档站点（`docs/index.html`、`docs/annotation-mapping.md` 等）：补充 BCD 类型的映射与特性描述，完善核心功能文档。
- 核心解析流程：扩展 `lengthRef` / 动态长度支持，将长度绑定到上文字段，并在 `Resolver`/`CodecFlow` 中统一透传到字符串、数组与结构体数组的编码解码流程中。
- 核心解析流程：扩展 `offsetRef` / 动态偏移支持，可引用前置字段的数值作为偏移，在 `Resolver` 中绑定偏移供应器，并通过 `CodecFlow` 包装注解，使 offset 在编解码阶段按运行时值生效。

## [3.12.2] - 2025-08-26
### 新增
- `@AutoType` 新增 `lengthRef`，支持变长字符串/数组/集合的自动类型推断。
- 校验和增强：支持基于区间的范围计算。
- 文档：新增 `docs/variable-length.md`（可变长度与结构体数组），新增 Netty/Kafka 集成文档 `docs/netty-integration.md`、`docs/kafka-integration.md`。
- 可变长度支持：通过 `lengthRef` 引用计数字段，支持变长字符串/数组与结构体数组。

### 变更
- 代理转发：`ProtocolType.proxy(AutoType, ...)` 透传 `lengthRef` 至目标注解（`StringType`、各 `*ArrayType`、`StructArrayType`）。
- 文档与示例：帮助页侧边栏加入“Variable Length & Struct Arrays”入口；安装示例版本更新为 `3.12.2`。

### 修复
- DateCodecTest：将 Instant/LocalDateTime 按毫秒对齐，提升测试稳定性。

### 移除
- 移除 Scala 相关支持。

## [3.12.1] - 2025-08-19
### 修复
- ByteBuffer：在非固定缓冲区写入超出当前容量时自动扩容，避免数组越界异常。
- ByteBufferIOStream.align：对齐参数改为必须是正的 2 的幂，非法值抛出 IllegalArgumentException。

### 变更
- 文档站点：重设计 `docs/index.html` 主页（概览、特性、基础用法），帮助页默认落点改为 Quick Start，特性区块增加小图标。
- 文档内版本号统一更新为 3.12.1。

### 新增
- 新增 Android 兼容性指南 `docs/android.md` 并在相关页面添加链接。

## [3.12.0] - 2025-08-11
### 新增
- 单注解校验和：`@Checksum(start, length, offset, type, byteOrder)`，编码自动写入、解码自动校验。
- 算法覆盖更广：`CRC8_SMBUS`、`CRC8_MAXIM`、`XOR8 (BCC)`、`LRC8`、`CRC16_MODBUS`、`CRC16_CCITT`、`CRC32C`、`CRC64_ECMA-182`、`CRC64/ISO`。

### 变更
- `ChecksumUtils` 复用单例引擎，新增不支持类型时报错 `CodecException`。
- ChecksumFlow 支持 8/16/32/64 位校验值的读写与解码校验。
- 文档扩展：注解映射、字节序/位序、校验和/CRC、公式、数组与字符串、无注解 API、FAQ。

### 不兼容变更
- `@Checksum` 参数改为 `start`、`length`、`offset`。不再需要通过 `@UIntXType` 仅用于提供偏移。

## [3.11.0] - 2025-06-08
### 新增
- 新增可选的CRC校验机制。
- 默认字节序改为与主机一致。

### 修复
- 修复读取double时字节序处理不正确的问题。

### 变更
- 补充更多文档示例。

## [3.10.3] - 2024
### 修复
- 修复在Type字段上使用 `@DecodingIgnore` 和 `@EncodingIgnore` 的处理问题。

## [3.10.2] - 2024
### 变更
- 将原 `CodecUtils` 拆分为 `DecodeUtils` 和 `EncodeUtils`。
- 更新README，新增示例。

## [3.10.1] - 2024
### 修复
- 修复Calendar编解码返回错误值的问题。

## [3.9.2] - 2023
### 变更
- 重构编解码层，减少分配并提升速度。

## [3.9.1] - 2023
### 新增
- 增强Scala注解处理能力，提升跨语言兼容性。

## [3.9.0] - 2023
### 新增
- 新增 `AsciiType` 与 `CharType` 数据类型用于文本处理。
- `Decoder.mapTo` 现在可接受自定义映射函数。

## [3.8.3] - 2022-12-23
### 变更
- 简化 `Decoder` API，新增便捷方法。

## [3.8.2] - 2022-12-20
### 变更
- 扩充手册案例和图示。

## [3.8.1] - 2022-11-01
### 新增
- 补充 `@EncodingIgnore` 与 `@DecodingIgnore` 注解的说明。

## [3.8.0] - 2022-10-27
### 新增
- 新增扩展点并优化整体吞吐。

## [3.7.1] - 2022-10-24
### 新增
- 补充自动类型识别功能的文档。

## [3.7.0] - 2022-10-18
### 变更
- 多处文档更新，阐明API使用方式。

## [3.6.2] - 2022-10-08
### 新增
- 在编码映射器中加入列表类型测试覆盖。

## [3.6.1] - 2022-10-07
### 新增
- 现已支持 `Integer`、`Long` 等包装类型。

## [3.6.0] - 2022-10-03
### 新增
- 新增数组字段的内置编解码器。

## [3.5.1] - 2022-10-02
### 变更
- 合并开发分支的清理和优化。

## [3.5.0] - 2022-09-30
### 新增
- 重写公式系统，支持复杂表达式。

## [3.4.1] - 2022-09-28
### 修复
- 修复解析时间类型时偏移量错误。

## [3.4.0] - 2022-09-28
### 变更
- 改进引用图算法，加快查找速度。

## [3.3.1] - 2022-09-23
### 变更
- 在嵌套结构中更可靠地解析引用。

## [3.3.0] - 2022-07-20
### 变更
- 主要API类统一更名为FastProto前缀。

## [3.2.1] - 2022-06-13
### 变更
- 更新项目配置与README。

## [3.2.0] - 2022-06-10
### 新增
- 重命名注解类以提高可读性。

## [3.1.3] - 2023-08-12
### 修复
- 修复忽略注解在部分字段上失效的问题。

## [3.1.2] - 2021-11-30
### 变更
- 合并开发分支中的多项改进。

## [3.1.1] - 2021-11-27
### 变更
- 修复 #1127，并提升稳定性。

## [3.1.0] - 2021-11-16
### 新增
- 改进引用图结构，降低内存占用。

## [3.0.0] - 2021-11-09
### 变更
- 重大版本发布，带来诸多新功能与API变更。

## [2.4.3] - 2021-11-03
### 修复
- 修复解码或编码忽略标记失效的问题。

## [2.4.2] - 2021-11-01
### 变更
- 更新文档以反映最新API。

## [2.4.1] - 2021-08-10
### 变更
- 调整构建脚本以提升兼容性。

## [2.4.0] - 2021-08-10
### 新增
- 新增对固定长度字段的支持。

## [2.2.0] - 2021-07-21
### 新增
- 增加循环引用检测与处理机制。

## [2.1.0] - 2021-07-13
### 新增
- 补充枚举序列化相关测试。

## [2.0.0] - 2021-07-07
### 新增
- 新增中文版README。

## [1.6.2] - 2021-06-22
### 变更
- 刷新基准测试相关文档。

## [1.5.2] - 2021-06-15
### 新增
- 引入协议版本控制功能。

## [1.4.1] - 2021-06-09
### 新增
- 新增自动类型推断特性。

## [1.3.0] - 2021-06-07
### 变更
- 与主分支同步，包含一些修复。

## [1.2.3] - 2021-06-02
### 新增
- 新增完整的API文档。

## [1.1.2] - 2021-05-31
### 变更
- 改进数据报边界检查。

