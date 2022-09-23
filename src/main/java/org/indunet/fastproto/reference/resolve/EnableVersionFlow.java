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

package org.indunet.fastproto.reference.resolve;

import lombok.val;
import org.indunet.fastproto.annotation.EnableProtocolVersion;
import org.indunet.fastproto.annotation.EnableProtocolVersions;
import org.indunet.fastproto.annotation.type.UInt8Type;
import org.indunet.fastproto.exception.ResolveException;
import org.indunet.fastproto.reference.Reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;

/**
 * Resolve enable protocol version flow.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class EnableVersionFlow extends ResolvePipeline {
    @Override
    public void process(Reference reference) {
        val protocolClass = reference.getProtocolClass();
        List<EnableProtocolVersion> versions = new ArrayList<>();

        if (protocolClass.isAnnotationPresent(EnableProtocolVersions.class) &&
               protocolClass.isAnnotationPresent(EnableProtocolVersion.class)) {
            throw new ResolveException("Class cannot be annotated by EnableProtocolVersions and EnableProtocolVersion at the same time");
        } else if (protocolClass.isAnnotationPresent(EnableProtocolVersions.class)) {
            val enableProtocolVersions = protocolClass.getAnnotation(EnableProtocolVersions.class);

            Arrays.stream(enableProtocolVersions.value())
                    .forEach(versions::add);
        } else if (protocolClass.isAnnotationPresent(EnableProtocolVersion.class)) {
            versions.add(protocolClass.getAnnotation(EnableProtocolVersion.class));
        }

        IntPredicate validator = v -> v < UInt8Type.MIN_VALUE || v > UInt8Type.MAX_VALUE;
        val flag = versions.stream()
                .mapToInt(EnableProtocolVersion::version)
                .anyMatch(validator);

        if (flag) {
            throw new ResolveException(
                    String.format("Illegal protocol version for %s which must be uint8.", protocolClass.getName()));
        }

        reference.setEnableProtocolVersions(versions);
        this.forward(reference);
    }
}
