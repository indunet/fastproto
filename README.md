# What is FastProto?

FastProto is was conceived and designed in 2018. The original intention was to solve practical problems encountered in work. To put it simply, in the field of IoT, binary messages are usually used to transmit data between devices and servers, instead of the commonly used JSON format in Web. The server usually needs to parse binary messages according to the specified protocol. If send data to device, it also needs to perform data packets according to the specified protocol. Although it is not challenging for most developers, the whole process is extremely boring and error-prone. If there is an easier way to convert binary messages into Java objects, or convert Java objects into binary messages, developers can focus more on the business implementation of the system. Based on the above ideas, FastProto was born.

# Features
* Serialization and deserialization
* Support all basic Java data types
* Support String type and Timestamp type
* User-defined endian

# *Developing*
* Unsigned types
* Auto type

# Quick Start

## Build Requirements
* Java 1.8+
* Maven 3.5+

### License

FastProto is released under the [Apache 2.0 license](license.txt).

```
Copyright 1999-2020 indunet.org group Holding Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at the following link.

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.