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

package org.indunet.fastproto.graph.resolve;

import lombok.val;
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.EncodingFormula;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.formula.FormulaBuilder;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.mapper.JavaTypeMapper;

/**
 * FormulaFlow Class.
 * This class is responsible for resolving the decoding and encoding formulas in the context.
 * It checks the protocol field for the DecodingFormula and EncodingFormula annotations and sets the decoding and encoding formulas in the reference accordingly.
 * This class extends the ResolvePipeline class and overrides the process method to implement its functionality.
 *
 * @author Deng Ran
 * @since 3.5.0
 */
public class FormulaFlow extends ResolvePipeline {
    @Override
    public void process(Reference reference) {
        val field = reference.getField();

        if (field.isAnnotationPresent(DecodingFormula.class)) {
            setDecodingFormula(reference, field.getAnnotation(DecodingFormula.class));
        }

        if (field.isAnnotationPresent(EncodingFormula.class)) {
            setEncodingFormula(reference, field.getAnnotation(EncodingFormula.class));
        }

        this.forward(reference);
    }

    private void setDecodingFormula(Reference reference, DecodingFormula formula) {
        if (formula.value().length != 0) {
            val clazz = formula.value()[0];

            try {
                reference.setDecodingFormulaClass(clazz);
                reference.setDecodingFormula(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(String.format("fail initializing formula %s", clazz.getSimpleName()), e);
            }
        } else if (!formula.lambda().isEmpty()) {
            val inputType = JavaTypeMapper.get(reference.getDataTypeAnnotation().annotationType());
            val builder = FormulaBuilder.create(inputType, formula.lambda());

            reference.setDecodingLambda(builder.build());
        } else {
            throw new DecodingException(
                    String.format("value and lambda of @DecodingFormula of %s should not be empty at the same time.",
                            reference.getField().toString()));
        }
    }

    private void setEncodingFormula(Reference reference, EncodingFormula formula) {
        if (formula.value().length != 0) {
            val clazz = formula.value()[0];

            try {
                reference.setEncodingFormulaClass(clazz);
                reference.setEncodingFormula(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new EncodingException(String.format("fail initializing formula %s", clazz.getSimpleName()), e);
            }
        } else if (!formula.lambda().isEmpty()) {
            val inputType = reference.getField().getType();
            val builder = FormulaBuilder.create(inputType, formula.lambda());

            reference.setEncodingLambda(builder.build());
        } else {
            throw new EncodingException(
                    String.format("value and lambda of @EncodingFormula of %s should not be empty at the same time.",
                            reference.getField().toString()));
        }
    }
}