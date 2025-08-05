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
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(2)
@Threads(4)
public class CodecBenchmark {
    @Param({"SINGLE_THREAD", "MULTI_THREAD"})
    private TestMode testMode;

    private byte[] bytes;
    private Sample sample;
    private ThreadLocal<byte[]> threadLocalBytes;
    private ThreadLocal<Sample> threadLocalSample;

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

        // Initialize thread local resources
        this.threadLocalSample = ThreadLocal.withInitial(() -> new Sample(true));
        this.threadLocalBytes = ThreadLocal.withInitial(() -> threadLocalSample.get().toBytes());
    }

    @Benchmark
    @Group("decode")
    public Sample decode() {
        byte[] data = testMode == TestMode.SINGLE_THREAD ? bytes : threadLocalBytes.get();
        return FastProto.decode(data)
                .readBool("bool1", 0, 0)
                .readByte("byte8", 1)
                .readShort("short16", 2)
                .readInt32("int32", 4)
                .readUInt32("uint32", 8)
                .readFloat("float32", 12)
                .readInt64("long64", 16)
                .readDouble("double64", 24)
                .readInt8("int8", 32)
                .readInt16("int16", 34)
                .readUInt8("uint8", 36)
                .readUInt16("uint16", 38)
                .mapTo(Sample.class);
    }

    @Benchmark
    @Group("encode")
    public byte[] encode() {
        Sample data = testMode == TestMode.SINGLE_THREAD ? sample : threadLocalSample.get();
        return FastProto.create(60)
                .appendBool(data.isBool1())
                .appendInt8(data.getByte8())
                .appendInt16(data.getShort16())
                .appendInt32(data.getInt32())
                .appendUInt32(data.getUint32())
                .appendFloat(data.getFloat32())
                .appendInt64(data.getLong64())
                .appendDouble(data.getDouble64())
                .appendInt8(data.getInt8())
                .appendInt16(data.getInt16())
                .appendUInt8(data.getUint8())
                .appendUInt16(data.getUint16())
                .appendBytes(data.getBytes())
                .get();
    }

    public enum TestMode {
        SINGLE_THREAD,
        MULTI_THREAD
    }
}
