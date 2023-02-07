/*
 * Copyright 2019-2021 indunet.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.indunet.fastproto.benchmark.chain;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * FastProto benchmark of chain api.
 *
 * @author Deng Ran
 * @since 3.9.1
 */
@State(Scope.Benchmark)
public class FastProtoBenchmark {
    byte[] bytes;
    Sample sample;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FastProtoBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(3)
                .measurementIterations(10)
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void parse() {
        FastProto.decode(bytes)
                    .readByte("byte8")
                    .readShort("short16")
                    .readInt32("int32")
                    .readInt64("long64")
                    .readFloat("float32")
                    .readDouble("double64")
                    .readInt8("int8")
                    .readInt16("int16")
                    .readUInt8("uint8")
                    .readUInt16("uint16")
                    .readUInt32("uint32")
                    .mapTo(Sample.class);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void toBytes() {
        FastProto.create(this.bytes.length)
                .appendInt8(this.sample.byte8)
                .appendInt16(this.sample.int16)
                .appendInt32(this.sample.int32)
                .appendInt64(this.sample.long64)
                .appendFloat(this.sample.float32)
                .appendDouble(this.sample.double64)
                .appendInt8(this.sample.int8)
                .appendInt16(this.sample.int16)
                .appendUInt8(this.sample.uint8)
                .appendUInt16(this.sample.uint16)
                .appendUInt32(this.sample.uint32)
                .get();
    }

    @Setup
    public void setup() {
        val sample = new Sample();

        this.bytes = sample.toBytes();
        this.sample = sample;
    }
}
