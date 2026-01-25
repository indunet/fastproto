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

public class FormulaBuilderTemplate {
    public static final String PACKAGE = "org.indunet.fastproto.formula";

    private final String expression;
    private final String className;
    private final String fileName;
    private final String fullName;
    private final String sourceCode;

    public FormulaBuilderTemplate(Class<?> inputType, String lambda) {
        if (lambda.endsWith(";") || lambda.endsWith("}")) {
            this.expression = lambda;
        } else {
            this.expression = lambda + ";";
        }
        this.className = "Formula" + (inputType.getTypeName() + lambda).hashCode();
        this.fileName = this.className + ".java";
        this.fullName = PACKAGE + "." + this.className;
        this.sourceCode = String.format(
                "package %s;%n%n" +
                "import java.util.function.Function;%n" +
                "import org.indunet.fastproto.formula.FormulaBuilder;%n%n" +
                "public class %s implements FormulaBuilder {%n" +
                "    @Override%n" +
                "    public Function build() {%n" +
                "        Function<%s, Object> func = %s%n" +
                "        return func;%n" +
                "    }%n" +
                "}%n",
                PACKAGE,
                this.className,
                inputType.getTypeName(),
                this.expression
        );
    }

    public String fileName() {
        return this.fileName;
    }

    public String fullName() {
        return this.fullName;
    }

    public String toSourceCode() {
        return this.sourceCode;
    }
}
