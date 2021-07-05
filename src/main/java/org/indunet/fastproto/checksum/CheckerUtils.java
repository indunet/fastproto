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

package org.indunet.fastproto.checksum;

import org.indunet.fastproto.annotation.EnableChecksum;

/**
 * @author Deng Ran
 * @since 1.6.2
 */
public class CheckerUtils {
    public static int getSize(Class<?> protocolCLass) {
        if (protocolCLass.isAnnotationPresent(EnableChecksum.class)) {
            return CheckerFactory
                    .create(protocolCLass.getAnnotation(EnableChecksum.class))
                    .getSize();
        } else {
            return 0;
        }
    }
}
