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
import org.indunet.fastproto.annotation.DecodingIgnore;
import org.indunet.fastproto.annotation.EncodingIgnore;
import org.indunet.fastproto.graph.Reference;

import java.lang.reflect.Field;

/**
 * CodecIgnoreFlow Class.
 * This class is responsible for resolving the decoding and encoding ignore flags in the context.
 * It checks the protocol field for the DecodingIgnore and EncodingIgnore annotations and sets the decoding and encoding ignore flags in the reference accordingly.
 * This class extends the ResolvePipeline class and overrides the process method to implement its functionality.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class CodecIgnoreFlow extends ResolvePipeline {
    @Override
    public void process(Reference reference) {
        val field = reference.getField();

        if (field != null) {
            reference.setDecodingIgnore(isDecodingIgnored(field));
            reference.setEncodingIgnore(isEncodingIgnored(field));
        }

        this.forward(reference);
    }

    protected boolean isDecodingIgnored(Field field) {
        return field.isAnnotationPresent(DecodingIgnore.class);
    }

    protected boolean isEncodingIgnored(Field field) {
        return field.isAnnotationPresent(EncodingIgnore.class);
    }
}