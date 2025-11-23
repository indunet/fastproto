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

import org.indunet.fastproto.annotation.Validator;
import org.indunet.fastproto.codec.CodecContext;
import org.indunet.fastproto.exception.ResolvingException;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.graph.resolve.validate.TypeValidator;
import org.indunet.fastproto.graph.resolve.validate.ValidatorContext;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.mapper.CodecMapper;
import org.indunet.fastproto.mapper.JavaTypeMapper;

import java.text.MessageFormat;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.nio.charset.Charset;
import java.lang.reflect.Array;
import java.util.function.Supplier;

/**
 * This class is responsible for resolving the decoder and encoder flow.
 * It creates a CodecContext and then parses out the decoder and encoder based on this context.
 * Finally, it validates whether the parsed references are valid.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class CodecFlow extends ResolvePipeline {

    @Override
    public void process(Reference reference) {
        Annotation original = reference.getDataTypeAnnotation();
        Annotation decodeAnno = original;
        Annotation encodeAnno = original;

        // Wrap with dynamic offset if available
        boolean hasOffset = original != null &&
                java.util.Arrays.stream(original.annotationType().getMethods()).anyMatch(m -> m.getName().equals("offset") && m.getParameterCount() == 0);
        if (hasOffset && reference.getOffsetSupplier() != null) {
            decodeAnno = wrapAnnotationWithDynamicOffset(decodeAnno, reference.getOffsetSupplier());
            encodeAnno = wrapAnnotationWithDynamicOffset(encodeAnno, reference.getOffsetSupplier());
        }

        // Only wrap if the annotation provides length()
        boolean hasLength = original != null &&
                java.util.Arrays.stream(original.annotationType().getMethods()).anyMatch(m -> m.getName().equals("length") && m.getParameterCount() == 0);

        if (hasLength) {
            Supplier<Integer> decodeLenSupplier = () -> {
                if (reference.getLengthSupplier() != null) {
                    return reference.getLengthSupplier().get();
                } else {
                    try {
                        Integer l = (Integer) original.annotationType().getMethod("length").invoke(original);
                        if (l != null && l == 0) {
                            throw new org.indunet.fastproto.exception.ResolvingException(
                                    String.format("Length is 0 and no lengthRef provided for %s", reference.getField()));
                        }
                        return l;
                    } catch (org.indunet.fastproto.exception.ResolvingException re) {
                        throw re;
                    } catch (Exception e) {
                        throw new org.indunet.fastproto.exception.ResolvingException(
                                String.format("Failed to resolve length for %s", reference.getField()), e);
                    }
                }
            };
            Supplier<Integer> encodeLenSupplier = () -> {
                // If useSelfOnEncode=true use actual runtime length; else fall back to referenced lengthSupplier or original
                boolean useSelf = false;
                try {
                    java.lang.reflect.Method m = original.annotationType().getMethod("useSelfOnEncode");
                    useSelf = (Boolean) m.invoke(original);
                } catch (Exception ignore) {}

                if (useSelf) {
                    return computeSelfLength(reference, reference.getValue().get());
                }
                if (reference.getLengthSupplier() != null) {
                    return reference.getLengthSupplier().get();
                }
                try {
                    return (Integer) original.annotationType().getMethod("length").invoke(original);
                } catch (Exception e) {
                    return 0;
                }
            };

            // Wrap length on top of any existing offset wrapper to preserve both behaviors
            decodeAnno = wrapAnnotationWithDynamicLength(decodeAnno, decodeLenSupplier);
            encodeAnno = wrapAnnotationWithDynamicLength(encodeAnno, encodeLenSupplier);
        }

        CodecContext decodeCtx = CodecContext.builder()
                .dataTypeAnnotation(decodeAnno)
                .fieldType(reference.getField().getType())
                .field(reference.getField())
                .defaultByteOrder(reference.getByteOrder())
                .defaultBitOrder(reference.getBitOrder())
                .build();

        CodecContext encodeCtx = CodecContext.builder()
                .dataTypeAnnotation(encodeAnno)
                .fieldType(reference.getField().getType())
                .field(reference.getField())
                .defaultByteOrder(reference.getByteOrder())
                .defaultBitOrder(reference.getBitOrder())
                .build();

        reference.setDecoder(resolveDecoder(reference, decodeCtx));
        reference.setEncoder(resolveEncoder(reference, encodeCtx));

        validateReference(reference);

        this.forward(reference);
    }

    // Resolve decoder.
    protected Function<ByteBufferInputStream, ?> resolveDecoder(Reference reference, CodecContext context) {
        if (reference.getDecodingFormulaClass() != null) {
            return CodecMapper.getDecoder(context, reference.getDecodingFormulaClass());
        } else if (reference.getDecodingLambda() != null) {
            Class<?> javaType = JavaTypeMapper.get(reference.getDataTypeAnnotation().annotationType());
            Function<ByteBufferInputStream, ?> decoder = CodecMapper.getDefaultDecoder(context, javaType);

            return decoder.andThen(reference.getDecodingLambda());
        } else {
            return CodecMapper.getDecoder(context, null);
        }
    }

    // Resolve encoder.
    protected BiConsumer<ByteBufferOutputStream, Object> resolveEncoder(Reference reference, CodecContext context) {
        if (reference.getEncodingFormulaClass() != null) {
            return CodecMapper.getEncoder(context, reference.getEncodingFormulaClass());
        } else if (reference.getEncodingLambda() != null) {
            Class<?> javaType = JavaTypeMapper.get(reference.getDataTypeAnnotation().annotationType());
            BiConsumer<ByteBufferOutputStream, Object> encoder = CodecMapper.getDefaultEncoder(context, javaType);
            Function<Object, Object> func = reference.getEncodingLambda();

            return (outputStream, value) -> encoder.accept(outputStream, func.apply(value));
        } else {
            return CodecMapper.getEncoder(context, null);
        }
    }

    // Validate reference.
    protected void validateReference(Reference reference) {
        try {
            ValidatorContext ctx = ValidatorContext.builder()
                    .field(reference.getField())
                    .typeAnnotation(reference.getDataTypeAnnotation())
                    .protocolType(reference.getProtocolType())
                    .decodingFormulaClass(reference.getDecodingFormulaClass())
                    .encodingFormulaClass(reference.getEncodingFormulaClass())
                    .build();
            Validator validator = reference.getDataTypeAnnotation()
                    .annotationType()
                    .getAnnotation(Validator.class);

            TypeValidator.create(validator.value()).process(ctx);
        } catch (ResolvingException e) {
            throw new ResolvingException(MessageFormat.format(
                    "Failed resolving the field of %s", reference.getField().toString()
            ), e);
        }
    }

    private static Annotation wrapAnnotationWithDynamicLength(Annotation original, Supplier<Integer> lengthSupplier) {
        Class<?> at = original.annotationType();
        return (Annotation) Proxy.newProxyInstance(CodecFlow.class.getClassLoader(), new Class<?>[]{at}, (proxy, method, args) -> {
            if (method.getName().equals("length") && method.getParameterCount() == 0) {
                return lengthSupplier.get();
            }
            return at.getMethod(method.getName(), method.getParameterTypes()).invoke(original, args);
        });
    }

    private static Annotation wrapAnnotationWithDynamicOffset(Annotation original, Supplier<Integer> offsetSupplier) {
        Class<?> at = original.annotationType();
        return (Annotation) Proxy.newProxyInstance(CodecFlow.class.getClassLoader(), new Class<?>[]{at}, (proxy, method, args) -> {
            if (method.getName().equals("offset") && method.getParameterCount() == 0) {
                return offsetSupplier.get();
            }
            return at.getMethod(method.getName(), method.getParameterTypes()).invoke(original, args);
        });
    }

    private static int computeSelfLength(Reference r, Object value) {
        if (value == null) {
            return 0;
        }
        String annName = r.getDataTypeAnnotation().annotationType().getSimpleName();
        switch (annName) {
            case "StringType": {
                try {
                    String cs = (String) r.getDataTypeAnnotation().annotationType().getMethod("charset").invoke(r.getDataTypeAnnotation());
                    return value.toString().getBytes(Charset.forName(cs)).length;
                } catch (Exception e) {
                    return value.toString().getBytes(Charset.forName("UTF-8")).length;
                }
            }
            case "BinaryType": {
                if (value instanceof byte[]) return ((byte[]) value).length;
                break;
            }
            default:
                break;
        }
        if (value.getClass().isArray()) {
            return Array.getLength(value);
        }
        if (value instanceof java.util.Collection) {
            return ((java.util.Collection<?>) value).size();
        }
        return 0;
    }
}
