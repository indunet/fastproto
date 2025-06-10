# 更多示例

这里包含两个示例，展示了 FastProto 的更多特性。

- **Weather 示例**：`Weather` 对象演示了字节序、位序和公式转换等用法，
  详见 `src/test/java/org/indunet/fastproto/domain/Weather.java` 与 `examples/WeatherExample.java`。
- **Phone 示例**：`Phone` 对象结合枚举、位字段、`@AutoType` 等注解，
  数据结构位于 `src/test/java/org/indunet/fastproto/domain/color/Phone.java`，
  调用代码见 `examples/PhoneExample.java`。

示例中既展示基于注解的快速编解码，也给出了链式 API 及工具类的手动解析方式。
