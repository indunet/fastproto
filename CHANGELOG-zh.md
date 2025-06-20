# 更新日志

该文件记录项目的主要更新内容。

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

