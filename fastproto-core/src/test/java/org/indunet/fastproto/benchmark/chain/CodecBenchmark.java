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

import org.indunet.fastproto.FastProto;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * FastProto benchmark of chain API.
 *
 * @author Deng Ran
 * @since 3.11.0
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@Threads(1)
public class CodecBenchmark {
    private byte[] bytes;
    private Sample sample;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CodecBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    @Setup
    public void setup() {
        // Initialize single thread resources
        this.sample = new Sample(true);
        this.bytes = this.sample.toBytes();
    }

    @Benchmark
    @Group("decode")
    public Sample decode() {
        return FastProto.decode(bytes)
                .readBool("bool1", 0, 0)
                .readByte("byte8", 1)
                .readShort("short16", 2)
                .readInt32("int32", 4)
                .readUInt32("uint32", 8)
                .readFloat("float32", 12)
                .readInt64("long64", 16)
                .readDouble("double64", 24)
                .readByte("int8", 32)
                .readShort("int16", 34)
                .readUInt8("uint8", 36)
                .readUInt16("uint16", 38)
                .mapTo(Sample.class);
    }

    @Benchmark
    @Group("encode")
    public byte[] encode() {
        return FastProto.create(60)
                .appendBool(sample.isBool1())
                .appendInt8(sample.getByte8())
                .appendInt16(sample.getShort16())
                .appendInt32(sample.getInt32())
                .appendUInt32(sample.getUint32())
                .appendFloat(sample.getFloat32())
                .appendInt64(sample.getLong64())
                .appendDouble(sample.getDouble64())
                .appendInt8(sample.getInt8())
                .appendInt16(sample.getInt16())
                .appendUInt8(sample.getUint8())
                .appendUInt16(sample.getUint16())
                .appendBytes(sample.getBytes())
                .get();
    }
}
