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

package org.indunet.fastproto.benchmark.annotation;

import org.indunet.fastproto.FastProto;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * FastProto benchmark of annotation API.
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
        return testMode == TestMode.SINGLE_THREAD ?
                FastProto.decode(bytes, Sample.class) :
                FastProto.decode(threadLocalBytes.get(), Sample.class);
    }

    @Benchmark
    @Group("encode")
    public byte[] encode() {
        return testMode == TestMode.SINGLE_THREAD ?
                FastProto.encode(sample, bytes.length) :
                FastProto.encode(threadLocalSample.get(), threadLocalBytes.get().length);
    }

    public enum TestMode {
        SINGLE_THREAD,
        MULTI_THREAD
    }
}
