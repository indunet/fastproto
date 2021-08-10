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

package org.indunet.fastproto.pipeline.decode;

import lombok.val;
import org.indunet.fastproto.TypeAssist;
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.DecoderFactory;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.CodecContext;
import org.indunet.fastproto.pipeline.FlowCode;

import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Decode flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class DecodeFlow extends AbstractFlow<CodecContext> {
    @Override
    public void process(CodecContext context) {
        val assist = context.getTypeAssist();
        val datagram = context.getDatagram();

        if (assist.getNoArgsConstructor()) {
            context.setObject(linearDecode(datagram, assist));
        } else {
            context.setObject(dfsDecode(datagram, assist));
        }

        this.nextFlow(context);
    }

    public Object linearDecode(byte[] datagram, TypeAssist assist) {
        List<DecodeContext> decodeContexts = assist.toDecodeContexts(datagram);
        val protocolClass = assist.getClazz();

        decodeContexts
                .forEach(c -> {
                    TypeAssist a = c.getTypeAssist();
                    Function<DecodeContext, ?> func = DecoderFactory.getDecoder(
                            a.getDecoderClass(),
                            a.getDecodeFormula());
                    try {
                        Object value = func.apply(c);
                        Object o = c.getObject();
                        a.setValue(o, value);
                    } catch (DecodeException e) {
                        throw new DecodeException(MessageFormat.format(
                                CodecError.FAIL_DECODING_FIELD.getMessage(), a.getField().toString()), e);
                    }
                });

        return assist.getObject(protocolClass);
    }

    public Object dfsDecode(byte[] datagram, TypeAssist assist) {
        val classes = Arrays.stream(assist.getClazz().getDeclaredFields())
                .map(Field::getType)
                .toArray(Class<?>[]::new);
        val clazz = assist.getClazz();
        Constructor<?> constructor = null;

        try {
            constructor = clazz.getConstructor(classes);
        } catch (NoSuchMethodException e) {
            throw new DecodeException(MessageFormat.format(
                    CodecError.FAIL_DECODING_FIELD.getMessage(), assist.getClazz().getName()), e);
        }

        val objects = assist.getElements()
                .stream()
                .map(a -> {
                    if (a.getElementType() == ElementType.FIELD) {
                        Function<DecodeContext, ?> func = DecoderFactory.getDecoder(
                                a.getDecoderClass(),
                                a.getDecodeFormula());
                        try {
                            return func.apply(a.toDecodeContext(datagram, null));
                        } catch (DecodeException e) {
                            throw new DecodeException(MessageFormat.format(
                                    CodecError.FAIL_DECODING_FIELD.getMessage(), a.getField().toString()), e);
                        }
                    } else {
                        if (assist.getCircularReference()) {
                            return null;
                        } else {
                            return this.dfsDecode(datagram, a);
                        }
                    }
                }).toArray(Object[]::new);

        try {
            return constructor.newInstance(objects);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DecodeException(
                    MessageFormat.format(
                            CodecError.FAIL_INITIALIZING_DECODE_OBJECT.getMessage(), assist.getClazz().getName()), e);
        }
    }

    @Override
    public long getFlowCode() {
        return FlowCode.DECODE_FLOW_CODE;
    }
}
