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

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

/**
 * Formula Builder.
 *
 * @author Deng Ran
 * @since 3.7.0
 */
public interface FormulaBuilder {
    static FormulaBuilder create(Class inputType, String lambda) {
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

    Function build();
}
