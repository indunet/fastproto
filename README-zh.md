![fastproto](logo.png "fastproto")

[English](README.md) | 中文

# *FastProto*

[![Build Status](https://travis-ci.com/indunet/fastproto.svg?branch=master)](https://travis-ci.com/indunet/fastproto)
[![codecov](https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU)](https://codecov.io/gh/indunet/fastproto)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ed904d7aacd142f08b5cd50b16b1d74b)](https://www.codacy.com/gh/indunet/fastproto/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=indunet/fastproto&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

FastProto是一款二进制协议化的序列化和反序列化工具，采用Java语言编写，它允许用户通过注释自定义二进制数据格式。
FastProto以一种全新的方式解决了Java跨语言和跨平台的数据交换问题 ，特别适用于物联网（IoT）领域。

## *功能*

*   二进制协议化序列化和反序列化
    *   通过注解自定义二进制格式
    *   支持无符号类型(uint8/uint16/uint32/uint64)
    *   支持反向寻址 
    *   自定义数据大小开端
    *   自定义[编码公式 & 解码公式][formula]   
*   支持数据[压缩 & 解压缩(gzip, deflate)][compression]  
*   支持[协议版本校验][protocol-version]
*   支持[数据完整性校验][checksum]
*   支持数据对称加密 & 解密     
*   内置[Kafka serializer & deserializer][kafka]
*   内置Netty解码器 & 编码器

## *Under Developing*

*   优化设计
*   增加单元测试覆盖率

## Compared with ProtoBuf

FastProto和ProtoBuf采用不同的方式解决了相同的问题，与之相比，FastProto更加适用于物联网领域，以下场景推荐使用FastProto:

*   因带宽或者流量的限制，需要更小的序列化结果
*   嵌入式设备采用C/C++编程，不便处理JSON/XML格式的数据，同时也不愿采用ProtoBuf
*   嵌入式设备采用非传统的编程方式，如梯形图、功能图和ST等，数据只能通过二进制格式交换
*   通过网关采集现场总线数据，如CAN/MVB/RS-485等

## *Maven*

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>2.0.0</version>
</dependency>
```

## *Quick Start*

想象这样一个应用场景，一台气象监测设备实时采集气象数据，并以二进制格式发送到气象站服务器，数据报文固定长度20字节。

>   65 00 7F 69 3D 84 7A 01 00 00 55 00 F1 FF 0D 00 00 00 07 00

数据报文包含8个不同类型的信号，具体协议（格式）如下：

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

首先，定义Java数据对象，并用FastProto注解依次标注各个字段。

```java
public class Weather {
    @UInteger8Type(0)
    int id;

    @TimestampType(2)
    Timestamp time;

    @UInteger16Type(10)
    int humidity;

    @Integer16Type(12)
    int temperature;

    @UInteger32Type(14)
    long pressure;

    @BooleanType(value = 18, bitOffset = 0)
    boolean temperatureValid;

    @BooleanType(value = 18, bitOffset = 1)
    boolean humidityValid;

    @BooleanType(value = 18, bitOffset = 2)
    boolean pressureValid;
}
```

通过`FastProto::parseFrom()`方法将数据报文反序列化成Java数据对象。

```java
byte[] datagram = ...   // Datagram sent by monitoring device.

Weather weather = FastProto.parseFrom(datagram, Weather.class);
```

通过`FastProto::toByteArray()`方法将Java数据对象序列成二进制数据，该方法的第二个参数是数据报文长度，如不指定，那么FastProto会自动推测长度。

```java
byte[] datagram = FastProto.toByteArray(weather, 20);
```

也许你已经注意到信号协议表中压力信号对应着一个换算公式，需要将压力值乘以0.1，这在物联网中非常常见。
为了帮助开发者减少中间步骤，FastProto支持自定义数据换算公式。

用户自定义的解码公式必须实现`java.lang.function.Function`接口。

```java
public class PressureDecodeFormula implements Function<Long, Double> {
    @Override
    public Double apply(Long value) {
        return value * 0.1;
    }
}
```

修改压力字段的注解和数据类型。

```java
@UInteger32Type(value = 14, afterDecode = DecodeSpeedFormula.class)
double pressure;
```

如果需要序列化操作，那么需要指定一个编码公式，同样必须实现`java.lang.function.Function`接口。

```java
public class PressureEncodeFormula implements Function<Double, Long> {
    @Override
    public Long apply(Double value) {
        return (long) (value * 10);
    }
}
```

修改压力字段的注解。

```java
@UInteger32Type(value = 14, afterDecode = PressureDecodeFormula.class, beforeEncode = PressureEncodeFormula.class)
double pressure;
```

## *Annotations*

除了Java基础数据类型及其包装类，FastProto还支持Timestamp、String和字节数组，上述类型均可通过`@AutoType`替代。
为了兼容跨语言数据交换，FastProto还引入了无符号类型。


| Annotation      | Java               | C/C++          | Size        |   AutoType |
|:---------------:|:------------------:|:--------------:|:-----------:|:-----------:|
| `@BooleanType`    | Boolean / boolean  | bool           | 1 位       |  √ |    
| `@CharacterType`  | Character / char   | --             | 2 字节     |  √  |    
| `@ByteType`       | Byte / byte        | char           | 1 字节      |  √  |    
| `@ShortType`      | Short / short      | short          | 2 字节     |  √  |    
| `@IntegerType`    | Integer / int      | int            | 4 字节     |  √ |    
| `@LongType`       | Long / long        | long long      | 8 字节     |  √ |    
| `@FloatType`      | Float / float      | float          | 4 字节     |  √  |    
| `@DoubleType`     | Double / double    | double         | 8 字节     |  √ |    
| `@Integer8Type`   | Integer / int      | char           | 1 字节      |  ×  |    
| `@Integer16Type`  | Integer / int      | short          | 2 字节     |  × |    
| `@UInteger8Type`  | Integer / int      | unsigned char  | 1 字节      |  ×  |    
| `@UInteger16Type` | Integer / int      | unsigned short | 2 字节     |  × |    
| `@UInteger32Type` | Long / long        | unsigned long  | 4 字节     |  × |    
| `@UInteger64Type` | BigInteger        | unsigned long long | 8 字节  |  √ |    
| `@BinaryType`     | byte[]             | char[]         | N 字节     |  √  |    
| `@StringType`     | java.lang.String   | --             | N 字节     |  √ |    
| `@TimestampType`  | java.sql.Timestamp | --             | 4 / 8 字节 |  √  |    

除了数据类型注解，FastProto还提供辅助注解帮助用户更深层次地自定义二进制数据格式。


| Annotation    | Scope        | Description                           |
|:-------------:|:------------:|:-------------------------------------:|
| `@Endian`       | Class & Field | Endianness, default as little endian. |
| `@DecodeIgnore` | Field        | Ignore the field when decoding.       |
| `@EncodeIgnore` | Field        | Ignore the field when encoding.       |
| `@EnableCompress` | Class        | Enable compress & decompress.  |
| `@EnableProtocolVersion` | Class     |  Enable protocol version verification.  |
| `@EnableChecksum`      |  Class      |  Enable checksum verification.              |
| `@EnableCrypto`      |  Class      |    Enable encrypt & decrypt.             |

## *Benchmark*

*   macOS, m1 8 cores, 16gb
*   openjdk 1.8.0_292
*   datagram of 128 bytes and nested protocol class of 48 fields 

|Benchmark |    Mode  | Samples  |  Score  |   Error   |   Units   |
|:--------:|:--------:|:--------:|:-------:|:---------:|:---------:|
| `FastProto::parseFrom` |  throughput   |   10  |   115.2 | ± 1.6    |  ops/ms   |
| `FastProto::toByteArray` | throughput  |   10  |   285.7 | ± 1.5    |  ops/ms   |

## *Build Requirements*

*   Java 1.8+  
*   Maven 3.5+    

## *License*

FastProto is released under the [Apache 2.0 license](license).

```
Copyright 2019-2021 indunet

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