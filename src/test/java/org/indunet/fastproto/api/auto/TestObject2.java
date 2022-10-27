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

package org.indunet.fastproto.api.auto;

import lombok.Data;
import org.indunet.fastproto.annotation.AutoType;

/**
 * Test object2.
 *
 * @author Deng Ran
 * @since 3.7.2
 */
@Data
public class TestObject2 {
    @AutoType(offset = 0)
    int[] ints = new int[] {1, 2, 3, 4};
}
