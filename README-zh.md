![fastproto](logo.png "fastproto")

[English](README.md) | 中文

# *FastProto*

[![Build Status](https://travis-ci.com/indunet/fastproto.svg?branch=master)](https://travis-ci.com/indunet/fastproto)
[![codecov](https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU)](https://codecov.io/gh/indunet/fastproto)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ed904d7aacd142f08b5cd50b16b1d74b)](https://www.codacy.com/gh/indunet/fastproto/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=indunet/fastproto&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

FastProto是一款Java编写的协议化二进制序列化和反序列化工具，它允许用户通过注释自定义协议，实现二进制数据的解码和编码。
FastProto采用一种全新的方式解决了Java跨语言和跨平台的数据交换问题 ，尤其适用于物联网（IoT）领域。

## *功能*

*   协议化二进制序列化和反序列化
    *   支持无符号类型
    *   支持反向寻址 
    *   自定义数据开端
    *   自定义[编码公式 & 解码公式][formula]   
*   支持数据[压缩 & 解压缩(gzip, deflate)][compression]  
*   支持[协议版本校验][protocol-version]
*   支持[数据完整性校验][checksum]
*   支持数据对称加密 & 解密     
*   内置[Kafka serializer & deserializer][kafka]
*   内置Netty解码器 & 编码器

## *Under Developing*

*   性能优化
*   循环引用
*   任意类型数组

## *Compared with ProtoBuf*

仅对Java而言，可以说两者采用不同的方式解决了相同的问题。
相比之下，FastProto更加适用于物联网（IoT）领域，以下场景推荐使用FastProto：

*   因带宽或者流量的限制，要求更小体积的序列化结果
*   嵌入式设备采用C/C++编程，不便处理JSON/XML格式的数据
*   因兼容性问题而无法使用ProtoBuf
*   嵌入式设备采用非传统的编程方式，如梯形图、功能图和ST等，数据只能通过二进制格式交换
*   通过网关采集现场总线数据(CAN/MVB/RS-485)，以二进制格式转发

## *Maven*

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>2.0.0</version>
</dependency>
```

## *快速入门*

想象这样一个应用场景，一台气象监测设备实时采集气象数据，并以二进制格式发送数据到气象站，数据报文固定长度20字节:

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

气象站接收到数据后，希望将其转换成Java数据对象，以便后续的业务功能开发，所以按照协议定义Java数据对象，并使用FastProto数据类型注解修饰相应字段。
需要注意，任意数据类型注解的`value`字段对应信号的字节偏移量。

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

通过`FastProto::parseFrom()`方法即可将二进制数据反序列化成Java数据对象。

```java
byte[] datagram = ...   // Datagram sent by monitoring device.

Weather weather = FastProto.parseFrom(datagram, Weather.class);
```

同理，也可通过`FastProto::toByteArray()`方法将Java数据对象序列成二进制数据。
该方法的第二个参数是数据报文长度，如果用户不指定，那么FastProto会自动推测长度。

```java
byte[] datagram = FastProto.toByteArray(weather, 20);
```

到这里还没有结束，需要注意，压力信号对应一个换算公式，通常需要用户将序列化后的结果乘以0.1。
为了帮助用户减少中间步骤，FastProto通过编码公式和解码公式实现上述过程。

自定义解码公式需要实现`java.lang.function.Function`接口，然后通过数据类型注解的`afterDecode`字段指定解码公式。

```java
public class PressureDecodeFormula implements Function<Long, Double> {
    @Override
    public Double apply(Long value) {
        return value * 0.1;
    }
}
```

```java
@UInteger32Type(value = 14, afterDecode = DecodeSpeedFormula.class)
double pressure;
```

同理，编码公式也需要实现`java.lang.function.Function`接口，然后通过数据类型注解的`beforeEncode`字段指定编码公式。[更多][formula]

```java
public class PressureEncodeFormula implements Function<Double, Long> {
    @Override
    public Long apply(Double value) {
        return (long) (value * 10);
    }
}
```

```java
@UInteger32Type(value = 14, afterDecode = PressureDecodeFormula.class, beforeEncode = PressureEncodeFormula.class)
double pressure;
```

## *注解*

FastProto支持Java基础数据类型、Timestamp、String和字节数组，以上类型均可由`@AutoType`代替。
考虑到跨语言跨平台的数据交换，FastProto还引入了无符号类型。


| 注解      | Java               | C/C++          | 大小        |   AutoType |
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

FastProto还提供了一些辅助注解，帮助用户进一步自定义二进制格式、解码和编码流程。

| 注解    | 作用域        | 描述                           |
|:-------------:|:------------:|:-------------------------------------:|
| `@Endian`       | Class & Field | 数据开端，默认小开端 |
| `@DecodeIgnore` | Field        | 反序列化时忽略该字段       |
| `@EncodeIgnore` | Field        | 序列化时忽略该字段       |
| `@EnableCompress` | Class        | 启动压缩和解压缩  |
| `@EnableProtocolVersion` | Class     |  启动协议版本校验  |
| `@EnableChecksum`      |  Class      |  启动数据完整性校验              |
| `@EnableCrypto`      |  Class      |    启动加密和解密             |

## *基准测试*

*   macOS, m1 8 cores, 16gb
*   openjdk 1.8.0_292
*   二进制数据固定大小128字节，嵌套数据对象共包含48个不同类型的字段

|Benchmark |    模式  | 样本数量  |  评分  |   误差   |   单位   |
|:--------:|:--------:|:--------:|:-------:|:---------:|:---------:|
| `FastProto::parseFrom` |  吞吐量   |   10  |   115.2 | ± 1.6    |  次/毫秒   |
| `FastProto::toByteArray` | 吞吐量  |   10  |   285.7 | ± 1.5    |  次/毫秒   |

## *Build Requirements*

*   Java 1.8+  
*   Maven 3.5+    

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