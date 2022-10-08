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

package org.indunet.fastproto.util;

import java.util.*;

/**
 * Collection utils.
 *
 * @author Deng Ran
 * @since 3.6.2
 */
public class CollectionUtils {
    public static Collection newInstance(Class<? extends Collection> clazz) throws InstantiationException, IllegalAccessException {
        if (!clazz.isInterface()) {
           return clazz.newInstance();
        } else if (clazz.equals(List.class)) {
            return new ArrayList<>();
        } else if (clazz.equals(Set.class)) {
            return new HashSet<>();
        } else if (clazz.equals(Deque.class)) {
            return new ArrayDeque<>();
        } else {
            throw new IllegalArgumentException(
                    String.format("%s type is not supported", clazz.getName()));
        }
    }
}
