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

package org.indunet.fastproto.api.array;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.annotation.type.DoubleArrayType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Collection object.
 *
 * @author Deng Ran
 * @since 3.6.2
 */
@Data
public class CollectionObject {
    @DoubleArrayType(offset = 0, length = 16)
    List<Double> doubles = new ArrayList<>();

    public CollectionObject() {
        val ramdom = new Random();

        IntStream.range(0, 16)
                .mapToObj(__ -> ramdom.nextDouble())
                .forEach(this.doubles::add);
    }
}
