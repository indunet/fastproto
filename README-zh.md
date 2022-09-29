![fastproto](logo.png "fastproto")

[English](README.md) | 中文

# *Fast Protocol*

[![Build Status](https://app.travis-ci.com/indunet/fastproto.svg?branch=master)](https://app.travis-ci.com/indunet/fastproto)
[![codecov](https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU)](https://codecov.io/gh/indunet/fastproto)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ed904d7aacd142f08b5cd50b16b1d74b)](https://www.codacy.com/gh/indunet/fastproto/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=indunet/fastproto&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/)
[![JetBrain Support](https://img.shields.io/badge/JetBrain-support-blue)](https://www.jetbrains.com/community/opensource)
[![License](https://img.shields.io/badge/license-Apache%202.0-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![GitHub](https://img.shields.io/badge/repo-github-blue)](https://github.com/indunet/fastproto)

FastProto是一款采用Java编写的二进制序列化和反序列化工具，能够通过注解自定义协议，解决了跨语言和跨平台的数据交换问题，特别适用于物联网（IoT）领域。

## *功能*

*   二进制序列化和反序列化
    * 通过注解自定义协议  
    * 支持基本数据类型、无符号类型、字符串类型和时间类型等
    * 支持反向寻址，适用于非固定长度二进制数据
    * 自定义开端字节顺序
    * 自定义[编码公式 & 解码公式][formula]   
*   支持数据[压缩 & 解压缩(gzip, deflate)][compression]  
*   支持[协议版本校验][protocol-version]
*   支持[数据完整性校验][checksum]
*   支持数据对称加密 & 解密

## *Under Developing*

*  代码结构 & 性能优化
*  添加测试用例，增加单元测试覆盖率

## *Compared with ProtoBuf*

虽然ProtoBuf和FastProto都可以用于解决跨语言和跨平台的数据交换问题，但两者采用的方式完全不同：

*   ProtoBuf通过编写schema自定义协议，而FastProto通过注解自定义协议；
*   仅有数据交换双方均使用ProtoBuf才能实现数据交换，而FastProto采用自定义开放协议，任意一方使用均可；
*   FastProto性能更加优越，自定义协议粒度更加精细

以下场景更加推荐使用FastProto：

* 二进制格式 & 开放协议
* 性能要求苛刻，不能容忍通用数据格式（JSON/XML）带来的性能损失；
* 数据源包含大量二进制内容，如通过现场总线（CAN/MVB/RS-485）采集的数据，并不适用于文本格式

## *Maven*

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>3.4.1</version>
</dependency>
```

## *快速入门*

有这样一个应用场景，一台气象监测设备实时采集气象数据，并以二进制格式发送数据到气象站，数据报文20字节固定长度:

>   65 00 7F 69 3D 84 7A 01 00 00 55 00 F1 FF 0D 00 00 00 07 00

数据报文包含8种不同类型的信号，具体协议如下：

| 字节偏移 | 位偏移 | 数据类型(C/C++)   | 信号名称       | 单位 |  换算公式  |
|:-----------:|:----------:|:--------------:|:-----------------:|:----:|:---------:|
| 0           |            | unsigned char  | 设备编号         |      |           |
| 1           |            |                | 预留          |      |           |
| 2-9         |            | long long      | 时间戳              |  ms  |           |
| 10-11       |            | unsigned short | 湿度          |  %RH |           |
| 12-13       |            | short          | 温度       |  ℃  |            |
| 14-17       |            | unsigned int   | 气压          |  Pa  | p * 0.1   |
| 18          | 0          | bool           | 温度有效标识 |      |           |
| 18          | 1          | bool           | 湿度有效标识    |      |           |
| 18          | 2          | bool           | 气压有效标识    |      |           |
| 18          | 3-7        |                | 预留          |      |           |
| 19          |            |                | 预留          |      |           |

1. **序列化 & 反序列化**

气象站接收到数据后，需要将其反序列化成Java数据对象，以便后续的业务功能开发。
首先，按照协议定义Java数据对象`Weather`，然后使用FastProto数据类型注解修饰各个属性，通过注解的offset属性指定信号的字节偏移量。

```java
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
    boolean temperatureValid;

    @BoolType(byteOffset = 18, bitOffset = 1)
    boolean humidityValid;

    @BoolType(byteOffset = 18, bitOffset = 2)
    boolean pressureValid;
}
```

调用`FastProto::parse()`方法将二进制数据反序列化成Java数据对象`Weather`

```java
// datagram sent by monitoring device.
byte[] datagram = ...   
        
Weather weather = FastProto.parse(datagram, Weather.class);
```

调用`FastProto::toBytes()`方法将Java数据对象`Weather`序列成二进制数据,方法的第二个参数是字节数组长度，如果用户不指定，那么FastProto会自动推测。

```java
byte[] datagram = FastProto.toBytes(weather, 20);
```

2. **编码公式 & 解码公式**

也许你已经注意到压力信号对应一个换算公式，通常需要用户自行将序列化后的结果乘以0.1，这是物联网数据交换时极其常见的操作。
为了帮助用户减少中间步骤，FastProto引入的编码公式和解码公式。

自定义解码公式需要实现`java.lang.function.Function`接口，然后通过数据类型注解的`decodingFormula`属性指定解码公式。

```java
public class PressureDecodeFormula implements Function<Long, Double> {
    @Override
    public Double apply(Long value) {
        return value * 0.1;
    }
}
```

```java
public class Weather {
    ...
    
    @UInt32Type(offset = 14, decodingFormula = DecodeSpeedFormula.class)
    double pressure;
}
```

同理，编码公式也需要实现`java.lang.function.Function`接口，然后通过数据类型注解的`encodingFormula`属性指定编码公式。[更多][formula]

```java
public class PressureEncodeFormula implements Function<Double, Long> {
    @Override
    public Long apply(Double value) {
        return (long) (value * 10);
    }
}
```

```java
public class Weather {
    ...

    @UInt32Type(offset = 14, decodingFormula = PressureDecodeFormula.class, encodingFormula = PressureEncodeFormula.class)
    double pressure;
}
```

3. **其他功能**

FastProto支持数据压缩、协议版本验证、数据完整性校验和数据对称加密，各项功能均可由注解开启。

```java
@EnableCrypto(value = CryptoPolicy.AES_ECB_PKCS5PADDING, key = "330926")
@EnableProtocolVersion(value = 78, version = 17)
@EnableCompress(value = CompressPolicy.DEFLATE, level = 2)
@EnableChecksum(offset = -4, start = 0, length = -5, checkPolicy = CheckPolicy.CRC32, endianPolicy = EndianPolicy.BIG)
public class Weather {
    ...
}
```

## *核心注解*

FastProto支持Java基础数据类型、Timestamp、String和字节数组，以上类型均可由`@AutoType`代替。
考虑到跨语言跨平台的数据交换，FastProto还引入了无符号类型。[更多][types]


|     注解      |                 Java                  | C/C++          |    大小    |
|:-----------:|:-------------------------------------:|:--------------:|:--------:|
|  @BoolType  |           Boolean / boolean           | bool           |   1 位    |    
| @CharType`  |           Character / char            | --             |   2 字节   |   
| @Int32Type  |             Integer / int             | int            |   4 字节   | 
| @Int64Type  |              Long / long              | long long      |   8 字节   |   
| @FloatType  |             Float / float             | float          |   4 字节   |  
| @DoubleType |            Double / double            | double         |   8 字节   |  
|  @Int8Type  |      Byte / byte / Integer / int      | char           |   1 字节   |  
| @Int16Type  |     Short / short / Integer / int     | short          |   2 字节   |  
| @UInt8Type  |             Integer / int             | unsigned char  |   1 字节   |   
| @UInt16Type |             Integer / int             | unsigned short |   2 字节   |   
| @UInt32Type |              Long / long              | unsigned long  |   4 字节   |   
| @UInt64Type |              BigInteger               | unsigned long long |   8 字节   |  
| @BinaryType |                byte[]                 | char[]         |   N 字节   |  
| @StringType | String / StringBuilder / StringBuffer | --             |   N 字节   |   
|  @TimeType  |      Timestamp / Date / Calendar      | --             | 4 / 8 字节 |  
|  @EnumType  |                 enum                  | enum             |   1 字节   |

FastProto还提供了一些辅助注解，帮助用户进一步自定义二进制格式、解码和编码流程。

|                      注解                      | 作用域        | 描述                           |
|:--------------------------------------------:|:------------:|:-------------------------------------:|
|                   @Endian                    | Class & Field | 数据开端，默认小开端 |
|               @DecodingIgnore                | Field        | 反序列化时忽略该字段       |
|               @EncodingIgnore                | Field        | 序列化时忽略该字段       |
|               @EnableCompress                | Class        | 启动压缩和解压缩  |
|           @EnableProtocolVersions            | Class     |  启动协议版本校验  |
|            @EnableProtocolVersion            | Class     |  启动协议版本校验  |
|               @EnableChecksum                |  Class      |  启动数据完整性校验              |
|                @EnableCrypto                 |  Class      |    启动加密和解密             |
|              @EnableFixedLength              |  Class      |    启动固定报文长度             |

## *基准测试*

*   macOS, m1 8 cores, 16gb
*   openjdk 1.8.0_292
*   二进制数据固定大小128字节，嵌套数据对象共包含48个不同类型的字段

|Benchmark |    模式  | 样本数量  |  评分  |   误差   |   单位   |
|:--------:|:--------:|:--------:|:-------:|:---------:|:---------:|
| `FastProto::parse` |  吞吐量   |   10  |   291.2 | ± 1.6    |  次/毫秒   |
| `FastProto::toBytes` | 吞吐量  |   10  |   285.7 | ± 1.5    |  次/毫秒   |

## *Build Requirements*

*   Java 1.8+  
*   Maven 3.5+    

## *欢迎加入*

FastProto取得了etBrain开源计划的支持，可提供核心开发人员免费的全家桶许可证。
如果你对该项目感兴趣，并希望加入承担部分工作（开发/测试/文档），请通过邮件<deng_ran@foxmail.com>联系我。

GitHub仓库: [github.com/indunet/fastproto](https://github.com/indunet/fastproto)

## *License*

FastProto is released under the [Apache 2.0 license](license).

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

[formula]: https://github.com/indunet/fastproto/wiki/Conversion-Formula
[kafka]: https://github.com/indunet/fastproto/wiki/Work-with-Kafka
[checksum]: https://github.com/indunet/fastproto/wiki/Data-Integrity-Check
[protocol-version]: https://github.com/indunet/fastproto/wiki/Protocol-Version
[compression]: https://github.com/indunet/fastproto/wiki/Compression
[formula]: https://github.com/indunet/fastproto/wiki/Formula-zh
[types]: https://github.com/indunet/fastproto/wiki/Data-Type-Annotations-zh
