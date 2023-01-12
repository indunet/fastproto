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

package org.indunet.fastproto.benchmark;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * FastProto benchmark.
 *
 * @author Deng Ran
 * @since 1.4.0
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
        FastProto.parse(bytes, Sample.class);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void toBytes() {
        FastProto.toBytes(sample, this.bytes.length);
    }

    @Setup
    public void setup() {
        val sample = new Sample();

        this.bytes = sample.toBytes();
        this.sample = sample;
    }
}
