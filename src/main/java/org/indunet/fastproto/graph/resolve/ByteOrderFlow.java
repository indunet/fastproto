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
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.DefaultByteOrder;
import org.indunet.fastproto.graph.Reference;

import java.util.Optional;

/**
 * Resolve byte order flow.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class ByteOrderFlow extends ResolvePipeline {
    protected final static ByteOrder DEFAULT_BYTE_ORDER = ByteOrder.LITTLE;

    @Override
    public void process(@NonNull Reference reference) {
        if (reference.getReferenceType() == Reference.ReferenceType.CLASS) {
            val protocolClass = reference.getProtocolClass();
            val endianPolicy = Optional.ofNullable(protocolClass.getAnnotation(DefaultByteOrder.class))
                    .map(DefaultByteOrder::value)
                    .orElse(DEFAULT_BYTE_ORDER);

            reference.setByteOrder(endianPolicy);
        } else if (reference.getReferenceType() == Reference.ReferenceType.FIELD) {
            val field = reference.getField();
            val endianPolicy = Optional.ofNullable(field.getAnnotation(DefaultByteOrder.class))
                    .map(DefaultByteOrder::value)
                    .orElseGet(() -> Optional.ofNullable(reference.getField().getDeclaringClass())
                            .map(c -> c.getAnnotation(DefaultByteOrder.class))
                            .map(DefaultByteOrder::value)
                            .orElse(DEFAULT_BYTE_ORDER));     // Inherit endian of declaring class.

            reference.setByteOrder(endianPolicy);
        }

        this.forward(reference);
    }
}
