# What is FastProto?

FastProto is a project that was conceived and designed in 2018. The original intention was to solve practical problems encountered in work. To put it simply, in the field of IoT, binary messages are usually used to transmit data between devices and servers, instead of the commonly used JSON format in Web. The server usually needs to parse binary messages according to the specified protocol. If need to send data to device, it also needs to perform data packets according to the specified protocol. Although it is not challenging for most developers, the whole process is extremely boring and error-prone. If there is an easier way to convert binary messages into Java objects, or convert Java objects into binary messages, developers can focus more on the business implementation of the system. Based on the above ideas, FastProto was born.

