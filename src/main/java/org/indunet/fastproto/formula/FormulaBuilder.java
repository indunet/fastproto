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
 * @author Deng Ran
 * @since 3.7.0
 */
public interface FormulaBuilder {
    static FormulaBuilder create(Class inputType, String lambda) {
        // Allow disabling string-lambda compilation via system property: fastproto.lambda.disabled=true
        if (Boolean.parseBoolean(System.getProperty("fastproto.lambda.disabled", "false"))) {
            throw new ResolvingException("String lambda compilation is disabled. Set -Dfastproto.lambda.disabled=false or use a precompiled Function class.");
        }
        // Quick check: without a system compiler we should fail fast with a clear message
        if (ToolProvider.getSystemJavaCompiler() == null) {
            throw new ResolvingException("No system Java compiler (javax.tools). Run with a JDK or include jdk.compiler module, or avoid lambda formulas.");
        }
        try {
            JavaStringCompiler compiler = new JavaStringCompiler();
            FormulaBuilderTemplate template = new FormulaBuilderTemplate(inputType, lambda);
            Map<String, byte[]> results = compiler.compile(template.fileName(), template.toSourceCode());
            Class clazz = compiler.loadClass(template.fullName(), results);

            return (FormulaBuilder) clazz.newInstance();
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new ResolvingException(String.format("Fail compiling lambda expression: %s", lambda), e);
        }
    }

    @SuppressWarnings("rawtypes")
    Function build();
}
