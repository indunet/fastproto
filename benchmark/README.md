# *FastProto Benchmark*

*   windows 11, i7 11th, 32gb
*   openjdk 1.8.0_292
*   datagram of 60 bytes and protocol class of 13 fields

|Benchmark |    Mode  | Samples  | Score | Error  |   Units   |
|:--------:|:--------:|:--------:|:-----:|:------:|:---------:|
| `FastProto::parse` |  throughput   |   10  |  240  | ± 4.6  |  ops/ms   |
| `FastProto::toBytes` | throughput  |   10  |  317  | ± 11.9 |  ops/ms   |