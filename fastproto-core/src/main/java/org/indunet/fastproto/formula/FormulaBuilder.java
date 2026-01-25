/*
 * Copyright 2019-2022 indunet.org
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

package org.indunet.fastproto.formula;

import org.indunet.fastproto.exception.ResolvingException;
import org.indunet.fastproto.formula.compiler.JavaStringCompiler;

import javax.tools.ToolProvider;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

/**
 * Formula Builder Interface.
 * This interface provides a static method to create a FormulaBuilder instance.
 * It takes an input type and a lambda expression as parameters, compiles the lambda expression into a class, and returns an instance of that class.
 * The created FormulaBuilder instance can be used to build a function from the compiled lambda expression.
 *
 * <p>Note: Dynamic compilation is disabled by default since version 3.13.0 for better compatibility
 * with Android and Java 11+ JRE environments. Use the FastProto annotation processor to generate
 * formula classes at compile time, or explicitly enable dynamic compilation by setting
 * {@code -Dfastproto.lambda.enabled=true} (requires JDK with javax.tools).
 *
 * @author Deng Ran
 * @since 3.7.0
 */
public interface FormulaBuilder {
    /**
     * System property to enable dynamic lambda compilation at runtime.
     * Default is false (disabled). Set to true to enable dynamic compilation.
     * Requires running on a JDK (not JRE) with javax.tools available.
     */
    String LAMBDA_ENABLED_PROPERTY = "fastproto.lambda.enabled";

    /**
     * Creates a FormulaBuilder by dynamically compiling the given lambda expression.
     * This method requires dynamic compilation to be enabled and a JDK environment.
     *
     * @param inputType the input type for the lambda function
     * @param lambda the lambda expression string (e.g., "x -> x * 0.1")
     * @return a FormulaBuilder instance
     * @throws ResolvingException if dynamic compilation is disabled or unavailable
     */
    static FormulaBuilder create(Class inputType, String lambda) {
        // Dynamic compilation is disabled by default for Android/JRE compatibility
        // Enable via system property: -Dfastproto.lambda.enabled=true
        if (!Boolean.parseBoolean(System.getProperty(LAMBDA_ENABLED_PROPERTY, "false"))) {
            throw new ResolvingException(
                    "Dynamic lambda compilation is disabled by default. " +
                    "Please use one of the following options:\n" +
                    "  1. (Recommended) Use FastProto annotation processor to generate formula classes at compile time.\n" +
                    "  2. Use @DecodingFormula(value = YourFunction.class) with a precompiled Function class.\n" +
                    "  3. Enable dynamic compilation by setting -Dfastproto.lambda.enabled=true (requires JDK, not compatible with Android).");
        }
        // Quick check: without a system compiler we should fail fast with a clear message
        if (ToolProvider.getSystemJavaCompiler() == null) {
            throw new ResolvingException(
                    "No system Java compiler available (javax.tools.JavaCompiler is null). " +
                    "Dynamic compilation requires running on a JDK (not JRE). " +
                    "Please use the FastProto annotation processor or a precompiled Function class instead.");
        }
        try {
            JavaStringCompiler compiler = new JavaStringCompiler();
            FormulaBuilderTemplate template = new FormulaBuilderTemplate(inputType, lambda);
            Map<String, byte[]> results = compiler.compile(template.fileName(), template.toSourceCode());
            Class clazz = compiler.loadClass(template.fullName(), results);

            return (FormulaBuilder) clazz.newInstance();
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new ResolvingException(String.format("Failed to compile lambda expression: %s", lambda), e);
        }
    }

    @SuppressWarnings("rawtypes")
    Function build();
}
