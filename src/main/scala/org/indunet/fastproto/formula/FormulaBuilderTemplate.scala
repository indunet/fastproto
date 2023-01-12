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

package org.indunet.fastproto.formula

import org.indunet.fastproto.formula.FormulaBuilderTemplate.PACKAGE

/**
 * Formula Builder Template.
 *
 * @author Deng Ran
 * @since 3.7.0
 */
object FormulaBuilderTemplate {
  val PACKAGE = "org.indunet.fastproto.formula"
}

class FormulaBuilderTemplate(inputType: Class[_], lambda: String) {
  val expression = if (lambda.endsWith(";") || lambda.endsWith("}")) {
    lambda
  } else {
    lambda + ";"
  }
  val className = "Formula" + inputType.getTypeName
    .concat(lambda)
    .hashCode
    .toString
  val fileName = className.concat(".java")
  val fullName = PACKAGE.concat("." + className)

  val sourceCode =
    s"""
       | package ${PACKAGE};
       |
       | import java.util.function.Function;
       | import org.indunet.fastproto.formula.FormulaBuilder;
       |
       | public class ${className} implements FormulaBuilder {
       |   @Override
       |   public Function build() {
       |     Function<${inputType.getTypeName}, Object> func = ${expression}
       |
       |     return func;
       |   }
       | }
       |""".stripMargin

  def toSourceCode(): String = this.sourceCode
}
