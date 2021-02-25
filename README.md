# fastproto

fastproto is a lightweight Java serialization and deserialization tool that can replace pattern files with Java annotations to achieve fine-grained control of serialization and deserialization, especially suitable for the communication between devices and servers in the Internet of Things field .


Weather monitoring is a typical application scenario. The weather monitoring point sends data to the server of the weather station for data aggregation. Embedded devices usually use C/C++ programming, and considering the cost of 4G traffic, edge devices and servers usually use binary messages to transmit data instead of the commonly used JSON in the Web.
Air temperature -40～+60℃
Air humidity 0～100%RH 0.1%RH
Wind direction 8 directions 3°
Wind speed 0～75m/s 0.1m/s
Atmospheric pressure 0~150KPa 0.1KPa
Noise 30~120dB 1dB
Rainfall 0-8mm/min 0.2m m
Illumination 0～200,000 Lux: