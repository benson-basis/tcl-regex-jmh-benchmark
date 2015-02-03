/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.basistech.tclre.benchmark;

import com.basistech.tclre.HsrePattern;
import com.basistech.tclre.PatternFlags;
import com.basistech.tclre.ReMatcher;
import com.basistech.tclre.RegexException;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class FindBenchmark {

    private final String engSample;
    private final HsrePattern engSamplePattern;

    public FindBenchmark()  {
        URL engUrl = Resources.getResource(FindBenchmark.class, "eng-1.txt");
        try {
            engSample = Resources.toString(engUrl, Charsets.UTF_8);
            engSamplePattern = (HsrePattern) HsrePattern.compile("\\m(?:0?[1-9]|1[0-2])([-/\\.\\s])(?:0?[1-9]|[12]\\d|3[01])\\1(?:\\d{2}|\\d{4})\\M", PatternFlags.ADVANCED, PatternFlags.EXPANDED);
        } catch (IOException | RegexException e) {
            throw new RuntimeException(e);
        }

    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void englishLookingAtBackref() throws RegexException {
        ReMatcher matcher = engSamplePattern.matcher(engSample);
        matcher.lookingAt();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FindBenchmark.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

}
