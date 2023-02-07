# *FastProto Benchmark*

*   windows 11, i7 11th, 32gb
*   openjdk 1.8.0_292
*   binary data of 60 bytes and protocol class of 13 fields

1. api with annotations

|      Benchmark      |    Mode  | Samples  | Score | Error  |   Units   |
|:-------------------:|:--------:|:--------:|:-----:|:------:|:---------:|
| `FastProto::decode` |  throughput   |   10  |  240  | ± 4.6  |  ops/ms   |
| `FastProto::encode` | throughput  |   10  |  317  | ± 11.9 |  ops/ms   |


2. api without annotations

|Benchmark |    Mode  | Samples  | Score | Error  |   Units   |
|:--------:|:--------:|:--------:|:--:|:---------:|:---------:|
| `FastProto::decode` |  throughput   |   10  | 1273 | ± 17    |  ops/ms   |
| `FastProto::create` | throughput  |   10  | 6911 | ± 162    |  ops/ms   |