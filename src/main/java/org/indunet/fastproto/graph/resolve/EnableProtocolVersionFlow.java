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
import org.indunet.fastproto.annotation.EnableFixedLength;
import org.indunet.fastproto.annotation.EnableProtocolVersion;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.graph.AbstractFlow;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

/**
 * Resolve enable protocol version flow.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
@Rule(name = "protocol")
public class EnableProtocolVersionFlow extends AbstractFlow<Reference> {
    @Condition
    public boolean evaluate(@Fact("reference") Reference reference) {
        return reference.getProtocolClass().isAnnotationPresent(EnableProtocolVersion.class);
    }

    @Action
    @Override
    public void process(@Fact("reference") Reference reference) {
        val protocolClass = reference.getProtocolClass();

        if (protocolClass.isAnnotationPresent(EnableProtocolVersion.class)) {
            val enableProtocolVersion = protocolClass.getAnnotation(EnableProtocolVersion.class);

            reference.setEnableProtocolVersion(enableProtocolVersion);
        }

        this.nextFlow(reference);
    }

    @Override
    public long getFlowCode() {
        return 0;
    }
}
