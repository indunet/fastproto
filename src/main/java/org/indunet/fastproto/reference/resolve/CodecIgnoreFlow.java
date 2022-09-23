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
import org.indunet.fastproto.annotation.DecodingIgnore;
import org.indunet.fastproto.annotation.EncodingIgnore;
import org.indunet.fastproto.reference.Reference;

/**
 * Resolve decode ignore and encode ignore flow.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class CodecIgnoreFlow extends ResolvePipeline {
    @Override
    public void process(Reference reference) {
        val field = reference.getField();

        if (field != null) {
            val decodeIgnore = field.isAnnotationPresent(DecodingIgnore.class);
            val encodeIngore = field.isAnnotationPresent(EncodingIgnore.class);

            reference.setDecodingIgnore(decodeIgnore);
            reference.setEncodingIgnore(encodeIngore);
        }

        this.forward(reference);
    }
}