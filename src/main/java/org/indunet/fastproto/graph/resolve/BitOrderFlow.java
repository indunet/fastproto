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

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.annotation.DefaultBitOrder;
import org.indunet.fastproto.graph.Reference;

import java.util.Optional;

/**
 * BitOrderFlow Class.
 * This class is responsible for resolving the bit order in the context.
 * It checks the protocol class or field for the DefaultBitOrder annotation and sets the bit order in the reference accordingly.
 * If no DefaultBitOrder annotation is found, it uses a default bit order.
 * This class extends the ResolvePipeline class and overrides the process method to implement its functionality.
 *
 * @author Deng Ran
 * @since 3.9.1
 */
public class BitOrderFlow extends ResolvePipeline {
    protected final static BitOrder DEFAULT_BIT_ORDER = BitOrder.LSB_0;

    @Override
    public void process(@NonNull Reference reference) {
        BitOrder bitOrder = getBitOrder(reference);

        reference.setBitOrder(bitOrder);
        this.forward(reference);
    }

    protected BitOrder getBitOrder(Reference reference) {
        if (reference.getReferenceType() == Reference.ReferenceType.CLASS) {
            val protocolClass = reference.getProtocolClass();

            return Optional.ofNullable(protocolClass.getAnnotation(DefaultBitOrder.class))
                    .map(DefaultBitOrder::value)
                    .orElse(DEFAULT_BIT_ORDER);
        } else if (reference.getReferenceType() == Reference.ReferenceType.FIELD) {
            val field = reference.getField();

            return Optional.ofNullable(field.getAnnotation(DefaultBitOrder.class))
                    .map(DefaultBitOrder::value)
                    .orElseGet(() -> Optional.ofNullable(reference.getField().getDeclaringClass())
                            .map(c -> c.getAnnotation(DefaultBitOrder.class))
                            .map(DefaultBitOrder::value)
                            .orElse(DEFAULT_BIT_ORDER));     // Inherit endian of declaring class.
        }

        return DEFAULT_BIT_ORDER;
    }
}
