![fastproto](logo.png "fastproto")

[English](README.md) | 中文

# FastProto

[![Build Status](https://app.travis-ci.com/indunet/fastproto.svg?branch=master)](https://app.travis-ci.com/indunet/fastproto)
[![codecov](https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU)](https://codecov.io/gh/indunet/fastproto)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ed904d7aacd142f08b5cd50b16b1d74b)](https://www.codacy.com/gh/indunet/fastproto/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=indunet/fastproto&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://img.shields.io/maven-central/v/org.indunet/fastproto.svg?label=Maven%20Central)](https://search.maven.org/artifact/org.indunet/fastproto)
[![JetBrains Support](https://img.shields.io/badge/JetBrains-support-blue)](https://www.jetbrains.com/community/opensource)
[![License](https://img.shields.io/badge/license-Apache%202.0-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

FastProto 是一款轻量级的 Java 二进制协议库。只需使用注解描述数据结构，其余字节操作都由 FastProto 处理。

## *核心功能*

* **注解驱动：** 字段用注解标记，解析和封装一目了然。
* **类型丰富：** 支持 Java 原始类型、无符号类型、字符串、时间以及集合。
* **灵活地址：** 提供反向地址，适配变长协议。
* **字节顺序可选：** 大端或小端随心切换。
* **公式支持：** Lambda 或自定义类均可实现编解码公式。
* **校验和/CRC：** 使用 `@Checksum` 注解一次性定义起始地址、长度与存放地址，内置 CRC8（SMBus、MAXIM）、CRC16（MODBUS、CCITT）、CRC32/CRC32C、CRC64（ECMA/ISO）、LRC、XOR 等。
* **多种API：** 兼顾效率与易用性。

查看[更新日志](CHANGELOG-zh.md)获取版本历史，英文版请查阅[CHANGELOG](CHANGELOG.md)。

### *正在开发*

* 代码结构 & 性能优化
* 丰富文档（新增核心功能使用指南）
* 支持Kaitai结构导出与集成

### *文档*

- Annotation Mapping: [docs/annotation-mapping.md](docs/annotation-mapping.md)
- Byte & Bit Order: [docs/byte-and-bit-order.md](docs/byte-and-bit-order.md)
- Checksum/CRC: [docs/checksum.md](docs/checksum.md)
- Transformation Formulas: [docs/formulas.md](docs/formulas.md)
- Arrays & Strings: [docs/arrays-and-strings.md](docs/arrays-and-strings.md)
- Using APIs without Annotations: [docs/without-annotations.md](docs/without-annotations.md)
- FAQ: [docs/faq.md](docs/faq.md)

### *安装*

* Maven

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>3.12.0</version>
</dependency>
```

* Gradle

```gradle
implementation "org.indunet:fastproto:3.12.0"
```

## *1. 快速入门*

下面展示如何解析一条 20 字节的气象报文：

>   65 00 7F 69 3D 84 7A 01 00 00 55 00 F1 FF 0D 00 00 00 07 00

报文共包含八个信号，协议如下：

| 字节偏移 | 位偏移 |  数据类型(C/C++)   |  信号名称  | 单位 |  换算公式  |
|:-----------:|:----------:|:--------------:|:------:|:----:|:---------:|
| 0           |            | unsigned char  |  设备编号  |      |           |
| 1           |            |                |   预留   |      |           |
| 2-9         |            |      long      |  时间戳   |  ms  |           |
| 10-11       |            | unsigned short |   湿度   |  %RH |           |
| 12-13       |            |     short      |   温度   |  ℃  |            |
| 14-17       |            |  unsigned int  |   气压   |  Pa  | p * 0.1   |
| 18          | 0          |      bool      | 设备有效标识 |      |           |
| 18          | 3-7        |                |   预留   |      |           |
| 19          |            |                |   预留   |      |           |

### *1.1 解码和编码二进制数据*

气象站接收到报文后，需要把它解码成 Java 对象。按照协议定义 `Weather` 类，并在字段上标注字节偏移。

```java
import org.indunet.fastproto.annotation.*;

public class Weather {
    @UInt8Type(offset = 0)
    int id;

    @TimeType(offset = 2)
    Timestamp time;

    @UInt16Type(offset = 10)
    int humidity;

    @Int16Type(offset = 12)
    int temperature;

    @UInt32Type(offset = 14)
    long pressure;

    @BoolType(byteOffset = 18, bitOffset = 0)
    boolean deviceValid;
}
```

调用`FastProto::decode()`方法将二进制数据解码成Java数据对象`Weather`

```java
byte[] datagram = ...   // 检测设备发送的二进制报文
        
Weather weather = FastProto.decode(datagram, Weather.class);
```

调用 `FastProto::encode()` 将 `Weather` 对象写回字节数组。第二个参数是数据长度，留空时 FastProto 会自动推测。

```java
byte[] datagram = FastProto.encode(weather, 20);
```


### *1.2 变换公式*

压力字段需要做简单的换算。FastProto 提供 `@EncodingFormula` 和 `@DecodingFormula`，可直接用 Lambda 表达式完成转换。 查看 [变换公式文档](docs/formulas.md) 了解更多。

```java
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.EncodingFormula;

public class Weather {
    ...

    @UInt32Type(offset = 14)
    @DecodingFormula(lambda = "x -> x * 0.1")           // 解码后得到的pressure等于uint32 * 0.1
    @EncodingFormula(lambda = "x -> (long) (x * 10)")   // 写入二进制的数据等于强制转换为长整型的(pressure * 0.1)
    double pressure;
}
```


## *2. 注解*

### *2.1 基本数据类型注解*

FastProto支持Java基础数据类型，考虑到跨语言跨平台的数据交换，还引入了无符号类型。


|     注解      |                Java                |     C/C++      |  大小  |
|:-----------:|:----------------------------------:|:--------------:|:----:|
|  @BoolType  |          Boolean/boolean           |      bool      | 1 位  |    
|  @AsciiType |           Character/char           |      char      | 1 字节 |
|  @CharType  |           Character/char           |       --       | 2 字节 |
|  @Int8Type  |       Byte/byte/Integer/int        |      char      | 1 字节 |  
| @Int16Type  |      Short/short/Integer/int       |     short      | 2 字节 |  
| @Int32Type  |            Integer/int             |      int       | 4 字节 | 
| @Int64Type  |             Long/long              |      long      | 8 字节 |
| @UInt8Type  |            Integer/int             | unsigned char  | 1 字节 |   
| @UInt16Type |            Integer/int             | unsigned short | 2 字节 |   
| @UInt32Type |             Long/long              |  unsigned int  | 4 字节 |   
| @UInt64Type |             BigInteger             | unsigned long  | 8 字节 |
| @FloatType  |            Float/float             |     float      | 4 字节 |  
| @DoubleType |           Double/double            |     double     | 8 字节 |


### *2.2 复合数据类型注解*

|        注解         |                Java                |     C/C++      |  大小  |
|:-----------------:|:----------------------------------:|:--------------:|:----:|
|    @StringType    | String/ StringBuilder/StringBuffer |       --       | N 字节 |   
|     @TimeType     |  Timestamp/Date/Calendar/Instant   |      long      | 8 字节 |  
|     @EnumType     |                enum                |      enum      | 1 字节 |


### *2.3 数组数据类型注解*

|        注解        |                                       Java                                        |      C/C++       |
|:----------------:|:---------------------------------------------------------------------------------:|:----------------:|
|   @BinaryType    |                       Byte[]/byte[]/Collection&lt;Byte&gt;                        |      char[]      |
|  @BoolArrayType  |                    Boolean[]/boolean[]/Collection&lt;Boolean&gt;                     |      bool[]      |
| @AsciiArrayType  |                  Character[]/char[]/Collection&lt;Character&gt;                   |      char[]      |
|  @CharArrayType  |                  Character[]/char[]/Collection&lt;Character&gt;                   |        --        |
|  @Int8ArrayType  |  Byte[]/byte[]/Integer[]/int[]/Collection&lt;Byte&gt;/Collection&lt;Integer&gt;   |      char[]      |
| @Int16ArrayType  | Short[]/short[]/Integer[]/int[]/Collection&lt;Short&gt;/Collection&lt;Integer&gt; |     short[]      |
| @Int32ArrayType  |                     Integer[]/int[]/Collection&lt;Integer&gt;                     |      int[]       |
| @Int64ArrayType  |                       Long[]/long[]/Collection&lt;Long&gt;                        |      long[]      |
| @UInt8ArrayType  |                     Integer[]/int[]/Collection&lt;Integer&gt;                     | unsigned char[]  |
| @UInt16ArrayType |                     Integer[]/int[]/Collection&lt;Integer&gt;                     | unsigned short[] |
| @UInt32ArrayType |                       Long[]/long[]/Collection&lt;Long&gt;                        |  unsigned int[]  |
| @UInt64ArrayType |                     BigInteger[]/Collection&lt;BigInteger&gt;                     | unsigned long[]  |
| @FloatArrayType  |                      Float[]/float[]/Collection&lt;Float&gt;                      |     float[]      |
| @DoubleArrayType |                    Double[]/double[]/Collection&lt;Double&gt;                     |     double[]     |


### *2.4 辅助注解*

FastProto还提供了一些辅助注解，帮助用户进一步自定义二进制格式、解码和编码流程。

|        注解         |    作用域    |         描述         |
|:-----------------:|:---------:|:------------------:|
| @DefaultByteOrder |   Class   | 默认字节顺序，如无指定，使用小端  |
| @DefaultBitOrder  |   Class   | 默认位顺序，如无指定，使用LSB_0 |
|  @DecodingIgnore  |   Field   |     反序列化时忽略该字段     |
|  @EncodingIgnore  |   Field   |     序列化时忽略该字段      |
| @DecodingFormula  |   Field   |        解码公式        |
| @EncodingFormula  |   Field   |        编码公式        |


#### *2.4.1 字节顺序和位顺序*

FastProto默认使用小端，可以通过`@DefaultByteOrder`注解修改全局字节顺序，也可以通过数据类型注解中的`byteOrder`属性修改特定字段的字节顺序，后者优先级更高。 查看 [字节序与位序文档](docs/byte-and-bit-order.md) 了解更多。

同理，FastProto默认使用LSB_0，可以通过`@DefaultBitOrder`注解修改全局位顺序，也可以通过数据类型注解中的`bitOrder`属性修改特定字段的位顺序，后者优先级更高。

```java
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.DefaultByteOrder;

@DefaultByteOrder(ByteOrder.BIG)
@DefaultBitOrder(BitOrder.LSB_0)
public class Weather {
    @UInt16Type(offset = 10, byteOrder = ByteOrder.LITTLE)
    int humidity;

    @BoolType(byteOffset = 18, bitOffset = 0, bitOrder = BitOrder.MSB_0)
    boolean deviceValid;
}
```

#### *2.4.2 解码和编码公式*

用户可以通过两种方式自定义公式，形式较为简单的公式建议使用Lambda表达式，形式较为复杂的公式建议自定义公式类并实现`java.lang.function.Function`接口。

* *Lambda表达式*

```java
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.EncodingFormula;

public class Weather {
    ...

    @UInt32Type(offset = 14)
    @DecodingFormula(lambda = "x -> x * 0.1")
    @EncodingFormula(lambda = "x -> (long) (x * 10)")
    double pressure;
}

```

* *自定义公式类*

```java
import java.util.function.Function;

public class PressureDecodeFormula implements Function<Long, Double> {
    @Override
    public Double apply(Long value) {
        return value * 0.1;
    }
}
```

```java
import java.util.function.Function;

public class PressureEncodeFormula implements Function<Double, Long> {
    @Override
    public Long apply(Double value) {
        return (long) (value * 10);
    }
}
```

```java
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.EncodingFormula;

public class Weather {
    ...

    @UInt32Type(offset = 14)
    @DecodingFormula(PressureDecodeFormula.class)
    @EncodingFormula(PressureEncodeFormula.class)
    double pressure;
}
```

用户可以根据需要仅指定编码公式，或者仅指定解码公式，如果同时指定Lambda表达式和自定义公式类，后者有更高的优先级。


#### *2.4.3 自动类型*

如果字段被`@AutoType`修饰，那么FastProto会自动推测类型。

```java
import org.indunet.fastproto.annotation.AutoType;

public class Weather {
    @AutoType(offset = 10, byteOrder = ByteOrder.LITTLE)
    int humidity;   // 默认 Int32Type

    @AutoType(offset = 14)
    long pressure;  // 默认 Int64Type
}
```


#### *2.4.4 忽略字段*
在一些特殊情况下，如果你希望在解析时忽略某些字段，或在封装时忽略某些字段，可以使用 `@DecodingIgnore` 与 `@EncodingIgnore`。

```java
import org.indunet.fastproto.annotation.*;

public class Weather {
    @DecodingIgnore
    @Int16Type(offset = 10)
    int humidity;   // 解析时忽略

    @EncodingIgnore
    @Int32Type(offset = 14)
    long pressure;  // 封装时忽略
}
```


### *2.5 校验和/CRC*

使用 `@Checksum` 一次性定义“起始地址 + 长度 + 校验和存放地址”。FastProto 会在编码时自动写入校验和，在解码时自动校验并在不匹配时抛出异常。 查看 [校验和文档](docs/checksum.md) 了解更多。

- CRC16（小端）示例：计算区间 [0,5)，CRC 写入字节 5..6
```java
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.*;

public class Packet {
    @UInt8Type(offset = 0) int b1;
    @UInt8Type(offset = 1) int b2;
    @UInt8Type(offset = 2) int b3;
    @UInt8Type(offset = 3) int b4;
    @UInt8Type(offset = 4) int b5;

    // 只需一个注解即可：起始=0，长度=5，CRC16 小端写到 5..6
    @Checksum(start = 0, length = 5, offset = 5, type = Checksum.Type.CRC16, byteOrder = ByteOrder.LITTLE)
    int crc16;
}

// 编码：自动计算并写入 CRC
Packet p = new Packet();
p.b1 = 0x31; p.b2 = 0x32; p.b3 = 0x33; p.b4 = 0x34; p.b5 = 0x35;
byte[] bytes = FastProto.encode(p, 7);  // 5 字节数据 + 2 字节 CRC16

// 解码：自动校验 CRC，不匹配将抛出 DecodingException
Packet q = FastProto.decode(bytes, Packet.class);
```

- 也可不使用注解，直接调用工具方法计算校验和：
```java
import org.indunet.fastproto.annotation.Checksum;
import org.indunet.fastproto.checksum.ChecksumUtils;

byte[] bytes = new byte[]{0x31,0x32,0x33,0x34,0x35};
long crc = ChecksumUtils.calculate(bytes, /*start=*/0, /*length=*/5, Checksum.Type.CRC16);
// 或者：
int crc16 = ChecksumUtils.crc16(bytes) & 0xFFFF;  // 计算整个数组的 CRC16
```


## *3. Scala*
FastProto 支持 case class。但由于 Scala 与 Java 注解并非完全兼容，使用时请参考如下导入：

```scala
import org.indunet.fastproto.annotation.scala._
```


## *4. 不使用注解的编解码*
在某些场景下，开发者不想或不能用注解修饰数据对象，例如对象来源于第三方库无法修改源代码，或只是想以更直接的方式创建二进制数据块。FastProto 提供了简单 API 来满足上述需求。

### *4.1 解码二进制数据*

- 解码到数据对象
```java
byte[] bytes = ... // 待解码的二进制数据

public class DataObject {
    Boolean f1;
    Integer f2;
    Integer f3;
}

DataObject obj = FastProto.decode(bytes)
        .readBool("f1", 0, 0)       // 读取字节偏移 0、位偏移 0 的布尔值
        .readInt8("f2", 1)          // 读取字节偏移 1 的有符号 8 位整数
        .readInt16("f3", 2)         // 读取字节偏移 2 的有符号 16 位整数
        .mapTo(DataObject.class);    // 根据字段名映射到 Java 对象
```

- 不使用数据对象
```java
import org.indunet.fastproto.util.DecodeUtils;

byte[] bytes = ... // 待解码的二进制数据

boolean f1 = DecodeUtils.readBool(bytes, 0, 0); // 读取字节偏移 0、位偏移 0 的布尔值
int     f2 = DecodeUtils.readInt8(bytes, 1);    // 读取字节偏移 1 的有符号 8 位整数
int     f3 = DecodeUtils.readInt16(bytes, 2);   // 读取字节偏移 2 的有符号 16 位整数
```

### *4.2 创建二进制数据块*
```java
byte[] bytes = FastProto.create(16)         // 创建长度为 16 字节的二进制块
        .writeInt8(0, 1)                    // 在偏移 0 写入无符号 8 位整数 1
        .writeUInt16(2, 3, 4)               // 在偏移 2 连续写入两个无符号 16 位整数 3、4
        .writeUInt32(6, ByteOrder.BIG, 256) // 在偏移 6 按大端写入无符号 32 位整数 256
        .get();
```

```java
import org.indunet.fastproto.util.EncodeUtils;

byte[] bytes = new byte[16];

EncodeUtils.writeInt8(bytes, 0, 1);                     // 在偏移 0 写入无符号 8 位整数 1
EncodeUtils.writeUInt16(bytes, 2, 3, 4);                // 在偏移 2 连续写入两个无符号 16 位整数 3、4
EncodeUtils.writeUInt32(bytes, 6, ByteOrder.BIG, 256);  // 在偏移 6 按大端写入无符号 32 位整数 256
```


## *5. Benchmark*

- windows 11, i7 11th, 32gb
- openjdk 1.8.0_292
- 60 字节二进制数据、13 个字段的协议类

1. 注解 API

|      Benchmark      |    Mode  | Samples  | Score | Error  |   Units   |
|:-------------------:|:--------:|:--------:|:-----:|:------:|:---------:|
| `FastProto::decode` | throughput |   10    |  240  | ± 4.6  |  ops/ms   |
| `FastProto::encode` | throughput |   10    |  317  | ± 11.9 |  ops/ms   |

2. 非注解 API

|   Benchmark   |   Mode   | Samples | Score | Error |  Units  |
|:-------------:|:--------:|:-------:|:-----:|:-----:|:-------:|
| `decode`      | throughput |   10   | 1273  | ± 17  | ops/ms  |
| `create`      | throughput |   10   | 6911  | ± 162 | ops/ms  |


## *6. 构建要求*

- Java 1.8+
- Maven 3.5+


## *7. 贡献*

FastProto 获得 JetBrains 开源项目支持，为核心贡献者提供全产品包的免费 License。
如果你对本项目感兴趣并希望参与（开发/测试/文档），欢迎通过邮箱联系：<deng_ran@aliyun.com>


## *8. 许可*

FastProto 以 [Apache 2.0 许可](license) 发布。

```
Copyright 2019-2021 indunet.org

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at the following link.

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```