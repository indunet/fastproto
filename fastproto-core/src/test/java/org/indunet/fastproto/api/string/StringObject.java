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

package org.indunet.fastproto.api.string;

import lombok.Data;
import org.indunet.fastproto.annotation.StringType;

/**
 * String object.
 *
 * @author Deng Ran
 * @since 3.4.0
 */
@Data
public class StringObject {
    @StringType(offset = 0, length = 6)
    String string = "string";
    @StringType(offset = 16, length = 13)
    StringBuilder stringBuilder = new StringBuilder("stringBuilder");
    @StringType(offset = 32, length = 12)
    StringBuffer stringBuffer = new StringBuffer("stringBuffer");
}
