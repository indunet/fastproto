![fastproto](logo.png "fastproto")

[English](README.md) | 中文

# *Fast Protocol*

[![Build Status](https://app.travis-ci.com/indunet/fastproto.svg?branch=master)](https://app.travis-ci.com/indunet/fastproto)
[![codecov](https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU)](https://codecov.io/gh/indunet/fastproto)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ed904d7aacd142f08b5cd50b16b1d74b)](https://www.codacy.com/gh/indunet/fastproto/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=indunet/fastproto&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.indunet/fastproto/)
[![JetBrain Support](https://img.shields.io/badge/JetBrain-support-blue)](https://www.jetbrains.com/community/opensource)
[![License](https://img.shields.io/badge/license-Apache%202.0-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

FastProto是一款Java编写的二进制数据处理工具，开发者可以通过注解来标记二进制数据中的字段信息（数据类型、字节偏移、大小开端等），然后调用简单的API即可实现解析和封包二进制数据。
它简化了二进制数据处理的流程，开发者并不需要编写复杂的代码。

## *功能*

* 通过注解来标记字段信息，快速地解析和封包二进制数据
* 支持Java基本数据类型、无符号类型、字符串类型、时间类型、数组类型和集合类型等
* 支持反向寻址，适用于非固定长度二进制数据，例如-1表示二进制数据的末尾
* 支持自定义字节顺序（大小开端）
* 自定义编码公式 & 解码公式，支持Lambda表达式

### *正在开发*

* 不依赖注解的开发
* 代码结构 & 性能优化

### *Maven*

```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>3.8.3</version>
</dependency>
```

## *1. 快速入门*

有这样一个应用场景，一台气象监测设备实时采集气象数据，并以二进制格式发送数据到气象站，数据报文固定长度20字节，具体如下:

>   65 00 7F 69 3D 84 7A 01 00 00 55 00 F1 FF 0D 00 00 00 07 00

数据报文包含8种不同类型的信号，具体协议如下：

| 字节偏移 | 位偏移 |  数据类型(C/C++)   | 信号名称       | 单位 |  换算公式  |
|:-----------:|:----------:|:--------------:|:-----------------:|:----:|:---------:|
| 0           |            | unsigned char  | 设备编号         |      |           |
| 1           |            |                | 预留          |      |           |
| 2-9         |            |      long      | 时间戳              |  ms  |           |
| 10-11       |            | unsigned short | 湿度          |  %RH |           |
| 12-13       |            |     short      | 温度       |  ℃  |            |
| 14-17       |            |  unsigned int  | 气压          |  Pa  | p * 0.1   |
| 18          | 0          |      bool      | 温度有效标识 |      |           |
| 18          | 1          |      bool      | 湿度有效标识    |      |           |
| 18          | 2          |      bool      | 气压有效标识    |      |           |
| 18          | 3-7        |                | 预留          |      |           |
| 19          |            |                | 预留          |      |           |

### *1.1 解析和封包二进制数据*

气象站接收到数据后，需要将其解析成Java数据对象，以便后续的业务功能开发。
首先，按照协议定义Java数据对象`Weather`，然后使用FastProto注解修饰各个字段，注解的offset属性信号的字节偏移量（地址）。

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
    boolean temperatureValid;

    @BoolType(byteOffset = 18, bitOffset = 1)
    boolean humidityValid;

    @BoolType(byteOffset = 18, bitOffset = 2)
    boolean pressureValid;
}
```

调用`FastProto::parse()`方法将二进制数据解析成Java数据对象`Weather`

```java
// datagram sent by monitoring device.
byte[] datagram = ...   
        
Weather weather = FastProto.parse(datagram, Weather.class);
```

调用`FastProto::toBytes()`方法将Java数据对象`Weather`封包成二进制数据,其中方法的第二个参数是字节数组长度，如果用户不指定，那么FastProto会自动推测。

```java
byte[] datagram = FastProto.toBytes(weather, 20);
```


### *1.2 变换公式*

也许你已经注意到压力信号对应一个换算公式，通常需要用户自行将序列化后的结果乘以0.1，这是物联网数据交换时极其常见的操作。
为了帮助用户减少中间步骤，FastProto引入了编码公式注解`@EncodingFormula`和解码公式注解`@DecodingFormula`，上述简单的公式变换可以通过Lambda表达式实现。

```java
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.EncodingFormula;

public class Weather {
    ...

    @UInt32Type(offset = 14)
    @DecodingFormula(lambda = "x -> x * 0.1")           // 解析后得到的pressure等于uint32 * 0.1
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
|  @AsciiTye  |           Character/char           |      char      | 1 字节 |   
|  @CharTye   |           Character/char           |       --       | 2 字节 |   
| @Int32Type  |            Integer/int             |      int       | 4 字节 | 
| @Int64Type  |             Long/long              |      long      | 8 字节 |   
| @FloatType  |            Float/float             |     float      | 4 字节 |  
| @DoubleType |           Double/double            |     double     | 8 字节 |  
|  @Int8Type  |       Byte/byte/Integer/int        |      char      | 1 字节 |  
| @Int16Type  |      Short/short/Integer/int       |     short      | 2 字节 |  
| @UInt8Type  |            Integer/int             | unsigned char  | 1 字节 |   
| @UInt16Type |            Integer/int             | unsigned short | 2 字节 |   
| @UInt32Type |             Long/long              |  unsigned int  | 4 字节 |   
| @UInt64Type |             BigInteger             | unsigned long  | 8 字节 |  


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

|        注解        |    作用域    |     描述     |
|:----------------:|:---------:|:----------:|
|  @DefaultEndian  |   Class   | 数据开端，默认小开端 |
| @DecodingIgnore  |   Field   | 反序列化时忽略该字段 |
| @EncodingIgnore  |   Field   | 序列化时忽略该字段  |
|   @FixedLength   |   Class   |  启动固定报文长度  |
| @DecodingFormula |   Field   |    解码公式    |
| @EncodingFormula |   Field   |    编码公式    |


#### *2.4.1 大小开端*

FastProto默认使用小开端，可以通过`@DefaultEndian`注解修改全局开端类型，也可以通过endian属性修改特定字段开端，后者优先级更高。

```java
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.DefaultEndian;

@DefaultEndian(EndianPolicy.BIG)
public class Weather {
    @UInt16Type(offset = 10, endian = EndianPolicy.LITTLE)
    int humidity;

    @UInt32Type(offset = 14)
    long pressure;
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
    @AutoType(offset = 10, endian = EndianPolicy.LITTLE)
    int humidity;   // default Int32Type

    @AutoType(offset = 14)
    long pressure;  // default Int64Type
}
```


#### *2.4.4 忽略字段*
在特殊场景下，如果在解析时忽略某些字段，或者封包时忽略某些字段，那么可通过注解`@DecodingIgnore`和`@EncodingIgnore`实现。

```java
import org.indunet.fastproto.annotation.*;

public class Weather {
    @DecodingFormula
    @Int16Type(offset = 10)
    int humidity;   // ignore when parsing

    @EncodingIgnore
    @Int32Type(offset = 14)
    long pressure; // ignore when packaging
}
```

## *3. Scala*

FastProto支持case class，但是Scala并不完全兼容Java注解，所以请使用如下方式引用FastProto。

```scala
import org.indunet.fastproto.annotation.scala._
```


## *4. 不使用注解的解析和封包*

在一些特殊的情况下，开发者不希望或者无法使用注解修饰数据对象，例如数据对象来自第三方库，开发者不能修改源代码，又如开发者仅希望通过简单的方法创建二进制数据块。
FastProto提供了精简的API解决了上述问题，具体如下：

### *4.1 解析二进制数据*

* *直接解析，不需要数据对象*

```java
boolean f1 = FastProto.parse(bytes)
        .boolType(0, 0)
        .getAsBoolean();
int f2 = FastProto.parse(bytes)
        .int8Type(1)      // 在字节偏移量1位置解析有符号8位整型数据
        .getAsInt();
int f3 = FastProto.parse(bytes)
        .int16Type(2)     // 在字节偏移量2位置解析有符号16位整型数据
        .getAsInt();
```

* *解析后映射成数据对象*

```java
byte[] bytes = ... // 待解析的二进制数据

public class DataObject {
    Boolean f1;
    Integer f2;
    Integer f3;
}

JavaObject obj = FastProto.parse(bytes)
        .boolType(0, 0, "f1")           
        .int8Type(1, "f2")              // 在字节偏移量1位置解析有符号8位整型数据，字段名称f2
        .int16Type(2, "f3")
        .mapTo(JavaObject.class);       // 将解析结果按照字段名称映射成指定的数据对象
```

### *4.2 创建二进制数据块*

```java
byte[] bytes = FastProto.toBytes()
        .length(16)             // 二进制数据块的长度
        .uint8Type(0, 1)        // 在字节偏移量0位置写入无符号8位整型数据1
        .uint16Type(2, 3, 4)    // 在字节偏移量2位置连续写入2个无符号16位整型数据3和4
        .uint32Type(6, EndianPolicy.BIG, 32)
        .get();
```

## *5. 基准测试*

*   windows 11, i7 11th, 32gb
*   openjdk 1.8.0_292
*   二进制数据固定大小60字节，数据对象共包含13个不同类型的字段

|Benchmark |    模式  | 样本数量  | 评分 |   误差   |   单位   |
|:--------:|:--------:|:--------:|:--:|:---------:|:---------:|
| `FastProto::parse` |  吞吐量   |   10  | 240 | ± 4.6    |  次/毫秒   |
| `FastProto::toBytes` | 吞吐量  |   10  | 317 | ± 11.9    |  次/毫秒   |

## *6. 构建要求*

*   Java 1.8+  
*   Maven 3.5+    

## *7. 贡献*

FastProto取得了etBrain开源计划的支持，可提供核心开发人员免费的全家桶许可证。
如果你对该项目感兴趣，并希望加入承担部分工作（开发/测试/文档），请通过邮件<deng_ran@foxmail.com>联系我。

## *8. 许可证*

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
