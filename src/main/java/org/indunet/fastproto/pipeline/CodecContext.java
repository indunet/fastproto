/*
 * Copyright 2019-2021 indunet
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

package org.indunet.fastproto.pipeline;

import lombok.Builder;
import lombok.Data;
import org.indunet.fastproto.TypeAssist;

/**
 * Codec Context.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
@Data
@Builder
public class CodecContext {
    byte[] datagram;
    TypeAssist typeAssist;
    Object object;
    Class<?> protocolClass;
    long codecFeature;

    public <T> T getObject(Class<T> clazz) {
        return (T) object;
    }
}
