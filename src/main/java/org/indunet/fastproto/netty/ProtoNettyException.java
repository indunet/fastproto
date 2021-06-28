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

package org.indunet.fastproto.netty;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * FastProto netty exception.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class ProtoNettyException extends RuntimeException {
    public ProtoNettyException(ProtoNettyError error) {
        this(error.getMessage());
    }

    public ProtoNettyException(ProtoNettyError error, Throwable cause) {
        this(error.getMessage(), cause);
    }

    public ProtoNettyException(String message) {
        super(message);
    }

    public ProtoNettyException(String message, Throwable cause) {
        super(message, cause);
    }

    @AllArgsConstructor
    @Getter
    public enum ProtoNettyError {
        ILLEAL_LENGTH("Illegal length");

        String message;
    }
}
