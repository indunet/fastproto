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

package org.indunet.fastproto;

import lombok.*;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.annotation.type.AutoType;
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.TypeDecoder;
import org.indunet.fastproto.encoder.EncodeContext;
import org.indunet.fastproto.encoder.TypeEncoder;
import org.indunet.fastproto.exception.*;
import org.indunet.fastproto.util.TypeUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.*;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Type assist.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class TypeAssist {
    protected static ConcurrentHashMap<Class<?>, TypeAssist> typeAssists = new ConcurrentHashMap<>();
    protected final static ThreadLocal<Object> instance = new ThreadLocal<>();

    TypeAssist parent;
    Class<?> clazz;
    Field field;
    Annotation typeAnnotation;
    EndianPolicy endianPolicy;
    Boolean decodeIgnore;
    Boolean encodeIgnore;
    ElementType elementType;
    List<TypeAssist> elements;
    Class<? extends TypeDecoder> decoderClass;
    Class<? extends TypeEncoder> encoderClass;
    Class<? extends Function> decodeFormula;
    Class<? extends Function> encodeFormula;
    Function<DecodeContext, ?> decoder;
    Consumer<?> encoder;
    Integer minLength = 0;  // Used to store minimum length of datagram.

    Optional<EnableCrypto> opEnableCrypto;
    Optional<byte[]> opKey;

    Optional<EnableCompress> opEnableCompress;
    Optional<EnableProtocolVersion> opProtocolVersion;
    Optional<EnableChecksum> opChecksum;

    int codecFeature;

    protected TypeAssist() {

    }

    public static TypeAssist byClass(Class<?> protocolClass) {
        return typeAssists.computeIfAbsent(protocolClass, c -> TypeAssist.get(c));
    }

    public static TypeAssist get(@NonNull Class<?> protocolClass) {
        TypeAssist assist = of(protocolClass);

        // Crypto policy and key.
        val opEnableCrypto = Optional.of(protocolClass)
                .map(c -> c.getAnnotation(EnableCrypto.class));

        if (opEnableCrypto.isPresent()) {
            assist.setOpEnableCrypto(opEnableCrypto);

//            if (!opEnableCrypto
//                    .get()
//                    .key()
//                    .isEmpty()) {
//                assist.setOpKey(opEnableCrypto
//                        .get()
//                        .key());
//            }

            assist.setOpKey(assist.getOpEnableCrypto()
                    .map(EnableCrypto::key)
                    .map(String::getBytes));
        }

        assist.setOpEnableCompress(Optional.of(protocolClass)
                .map(c -> c.getAnnotation(EnableCompress.class)));
        assist.setOpProtocolVersion(Optional.of(protocolClass)
                .map(c -> c.getAnnotation(EnableProtocolVersion.class)));
        assist.setOpChecksum(Optional.of(protocolClass)
                .map(c -> c.getAnnotation(EnableChecksum.class)));
        assist.setCodecFeature(CodecFeature.valueOf(assist));

        return assist;
    }

    public static TypeAssist of(Class<?> clazz) {
        Predicate<Field> isType = f -> Arrays.stream(f.getAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(t -> t.isAnnotationPresent(TypeFlag.class));

        // Nested types.
        Stream<TypeAssist> typeStream = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(DecodeIgnore.class)
                        && !f.isAnnotationPresent(EncodeIgnore.class))
                .filter(isType.negate())
                .filter(f -> !f.isEnumConstant())  // Stack overflow
                .filter(f -> !Modifier.isFinal(f.getModifiers()))
                .map(f -> {
                    f.setAccessible(true);
                    Class<?> c = f.getType();
                    TypeAssist a = TypeAssist.of(c);
                    Boolean decodeIgnore = f.isAnnotationPresent(DecodeIgnore.class);
                    Boolean encodeIgnore = f.isAnnotationPresent(EncodeIgnore.class);
                    a.setDecodeIgnore(decodeIgnore);
                    a.setEncodeIgnore(encodeIgnore);
                    a.setField(f);

                    return a;
                }).filter(TypeAssist::hasElement);

        // Default as little endian.
        EndianPolicy endianPolicy = Optional.ofNullable(clazz.getAnnotation(Endian.class))
                .map(Endian::value)
                .orElse(EndianPolicy.LITTLE);
        Boolean decodeIgnore = clazz.isAnnotationPresent(DecodeIgnore.class);
        Boolean encodeIgnore = clazz.isAnnotationPresent(EncodeIgnore.class);

        Stream<TypeAssist> fieldStream = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(DecodeIgnore.class)
                        && !f.isAnnotationPresent(EncodeIgnore.class))
                .filter(isType)
                .peek(f -> f.setAccessible(true))
                .map(TypeAssist::of)
                .peek(a -> {
                    if (a.getEndianPolicy() == null) {
                        a.setEndianPolicy(endianPolicy);
                    }
                });

        TypeAssist assist = TypeAssist.builder()
                .clazz(clazz)
                .field(null)
                .typeAnnotation(null)
                .decoderClass(null)
                .encoderClass(null)
                .decodeFormula(null)
                .encodeFormula(null)
                .endianPolicy(endianPolicy)
                .decodeIgnore(decodeIgnore)
                .encodeIgnore(encodeIgnore)
                .elementType(ElementType.TYPE)
                .minLength(0)
                .build();

        List<TypeAssist> elements = Stream.concat(fieldStream, typeStream)
                .peek(a -> a.setParent(assist))
                .collect(Collectors.toList());
        assist.setElements(elements);

        return assist;
    }

    public static Integer getMinLength(Annotation typeAnnotation) {
        if (typeAnnotation == null) {
            return null;
        }

        int minLength = 0;

        try {
            int value = (Integer) typeAnnotation
                    .getClass()
                    .getMethod("value")
                    .invoke(typeAnnotation);

            if (value >= 0) {
                minLength += value;
            } else {
                return -1;
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {

        }

        try {
            int size = typeAnnotation
                        .getClass()
                        .getField("SIZE")
                        .getInt(null);
            minLength += size;
        } catch (IllegalAccessException | NoSuchFieldException e) {

        }

        try {
            int length = (Integer) typeAnnotation
                        .getClass()
                        .getMethod("length")
                        .invoke(typeAnnotation);

            if (length >= 0) {
                minLength += length;
            } else {
                return -1;
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {

        }

        return minLength;
    }

    protected static Class<? extends Annotation> getTypeAnnotationClass(@NonNull Field field) {
        Annotation typeAnnotation = getTypeAnnotation(field);

        if (typeAnnotation instanceof AutoType) {
            return ProtocolType
                    .byAutoType(field.getType())
                    .typeAnnotationClass;
        } else {
            return typeAnnotation.annotationType();
        }
    }

    protected static Annotation getTypeAnnotation(@NonNull Field field) {
        return Arrays.stream(field.getAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(TypeFlag.class))
                .findAny()
                .orElseThrow(CodecException::new);
    }

    protected static Annotation getProxyTypeAnnotation(@NonNull Field field) {
        Annotation typeAnnotation = getTypeAnnotation(field);

        if (typeAnnotation instanceof AutoType) {
            Class<? extends Annotation> typeAnnotationClass = ProtocolType.byAutoType(field.getType()).typeAnnotationClass;
            return typeAnnotationClass.cast(Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{typeAnnotationClass},
                    (object, method, parameters) -> {
                        return Arrays.stream(typeAnnotation.getClass().getMethods())
                                .filter(m -> m.getName().equals(method.getName()))
                                .findAny()
                                .orElseThrow(CodecException::new)
                                .invoke(typeAnnotation);
                    }));
        } else {
            return typeAnnotation;
        }
    }

    protected static TypeAssist of(Field field) {
        EndianPolicy policy = Optional.ofNullable(field.getAnnotation(Endian.class))
                .map(Endian::value)
                .orElse(null);
        Boolean decodeIgnore = field.isAnnotationPresent(DecodeIgnore.class);
        Boolean encodeIgnore = field.isAnnotationPresent(EncodeIgnore.class);

        Class<? extends Annotation> typeAnnotationClass = getTypeAnnotationClass(field);
        Annotation typeAnnotation = getTypeAnnotation(field);

        Class<? extends TypeDecoder> decoder = Optional.of(typeAnnotationClass)
                .map(t -> t.getAnnotation(Decoder.class))
                .map(Decoder::value)
                .orElse(null);
        Class<? extends TypeEncoder> encoder = Optional.of(typeAnnotationClass)
                .map(t -> t.getAnnotation(Encoder.class))
                .map(Encoder::value)
                .orElse(null);


        Function<String, Class<? extends Function>> formula = name -> {
            try {
                Method method = typeAnnotation.getClass().getMethod(name);
                Object array = method.invoke(typeAnnotation);

                return Optional.of(array)
                        .filter(a -> a.getClass().isArray())
                        .filter(a -> Array.getLength(a) >= 1)
                        .map(a -> Array.get(a, 0))
                        .map(o -> (Class<? extends Function>) o)
                        .orElse(null);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                throw new DecodeFormulaException(
                        MessageFormat.format(
                                CodecError.FAIL_GETTING_DECODE_FORMULA.getMessage(), typeAnnotation.annotationType().getName(), field.getName()), e);
            }
        };

        Class<? extends Function> afterDecode = formula.apply("afterDecode");
        Class<? extends Function> beforeEncode = formula.apply("beforeEncode");

        // Check if the field and the type annotation match.
        try {
            // No encoder and decoder.
            if (afterDecode == null && beforeEncode == null) {
                val f = typeAnnotationClass.getField("JAVA_TYPES");

                Arrays.stream((Type[]) f.get(typeAnnotation))
                        .filter(t -> t == field.getType())
                        .findAny()
                        .orElseThrow(() -> new CodecException(MessageFormat.format(
                                CodecError.ANNOTATION_FIELD_NOT_MATCH.getMessage(), typeAnnotation.annotationType().getName(), field.getName())));
            }

            // Validate decoder return type.
            if (afterDecode != null) {
                Arrays.stream(afterDecode.getGenericInterfaces())
                        .filter(i -> i instanceof ParameterizedType)
                        .map(i -> ((ParameterizedType) i).getActualTypeArguments())
                        .map(a -> a[1])
                        .filter(t -> {
                            if (field.getType().isPrimitive()) {
                                return t == TypeUtils.getWrapperClass(field.getType().getName());
                            } else {
                                return t == field.getType();
                            }
                        }).findAny()
                        .orElseThrow(() -> new DecodeFormulaException(MessageFormat.format(
                                CodecError.ANNOTATION_FIELD_NOT_MATCH.getMessage(), typeAnnotation.annotationType().getName(), field.getName())));
            }

            // Validate encoder parameter type.
            if (beforeEncode != null) {
                Arrays.stream(beforeEncode.getGenericInterfaces())
                        .filter(i -> i instanceof ParameterizedType)
                        .map(i -> ((ParameterizedType) i).getActualTypeArguments())
                        .map(a -> a[0])
                        .filter(t -> {
                            if (field.getType().isPrimitive()) {
                                return t == TypeUtils.getWrapperClass(field.getType().getName());
                            } else {
                                return t == field.getType();
                            }
                        }).findAny()
                        .orElseThrow(() -> new EncodeFormulaException(MessageFormat.format(
                                CodecError.ANNOTATION_FIELD_NOT_MATCH.getMessage(), typeAnnotation.annotationType().getName(), field.getName())));
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new CodecException(MessageFormat.format(
                    CodecError.ANNOTATION_FIELD_NOT_MATCH.getMessage(), typeAnnotation.annotationType().getName(), field.getName()), e);
        }

        return TypeAssist.builder()
                .clazz(field.getType())
                .field(field)
                .typeAnnotation(getProxyTypeAnnotation(field))
                .decoderClass(decoder)
                .encoderClass(encoder)
                .decodeFormula(afterDecode)
                .encodeFormula(beforeEncode)
                .endianPolicy(policy)
                .decodeIgnore(decodeIgnore)
                .encodeIgnore(encodeIgnore)
                .elementType(ElementType.FIELD)
                .minLength(getMinLength(getProxyTypeAnnotation(field)))     // From proxy type annotation.
                .build();
    }

    public Integer getMaxLength() {
        Integer length = this.minLength;

        if (this.elements == null) {
            return length;
        } else if (this.elements.stream()
                .mapToInt(TypeAssist::getMaxLength)
                .anyMatch(l -> l < 0)) {
            throw new AddressingException(CodecError.UNABLE_INFER_LENGTH);
        }

        int max = this.elements.stream()
                .mapToInt(TypeAssist::getMaxLength)
                .max()
                .getAsInt();

        return length >= max ? length : max;
    }

    public <T> T getObject(Class<T> clazz) {
        T object = (T) instance.get();
        instance.remove();

        return object;
    }

    public boolean hasElement() {
        return this.elements != null && !this.elements.isEmpty();
    }

    public void setValue(Object object, Object value) {
        try {
            this.field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new DecodeException(
                    MessageFormat.format(CodecError.FAIL_ASSIGN_VALUE.getMessage(), this.field.getName()), e);
        }
    }

    protected DecodeContext toDecodeContext(byte[] datagram, Object object) {
        return DecodeContext.builder()
                .object(object)
                .datagram(datagram)
                .typeAssist(this)
                .build();
    }

    protected List<DecodeContext> toDecodeContexts(byte[] datagram, Object parent) {
        try {
            Object value = this.clazz.newInstance();

            if (parent == null) {
                instance.set(value);
            }

            if (parent != null && field != null) {
                try {
                    this.field.set(parent, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            Stream<DecodeContext> fieldStream = this.elements.stream()
                    .filter(a -> a.getElementType() == ElementType.FIELD)
                    .map(a -> a.toDecodeContext(datagram, value));

            Stream<DecodeContext> classStream = this.elements.stream()
                    .filter(a -> a.getElementType() == ElementType.TYPE)
                    .flatMap(a -> a.toDecodeContexts(datagram, value).stream());

            return Stream.concat(fieldStream, classStream)
                    .collect(Collectors.toList());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new DecodeException(
                    MessageFormat.format(CodecError.FAIL_INITIALIZING_DECODE_OBJECT.getMessage(), this.clazz.getName()), e);
        }
    }

    public List<DecodeContext> toDecodeContexts(byte[] datagram) {
        return this.toDecodeContexts(datagram, null);
    }

    public EncodeContext toEncodeContext(Object object, byte[] datagram) {
        try {
            return EncodeContext.builder()
                    .datagram(datagram)
                    .typeAssist(this)
                    .value(this.field.get(object))
                    .build();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new EncodeException(
                    MessageFormat.format(CodecError.FAIL_GETTING_FIELD_VALUE.getMessage(), this.field.getName()), e);
        }
    }

    public List<EncodeContext> toEncodeContexts(Object object, byte[] datagram) {
        Stream<EncodeContext> fieldStream = this.elements.stream()
                .filter(a -> a.getElementType() == ElementType.FIELD)
                .map(a -> a.toEncodeContext(object, datagram));

        Stream<EncodeContext> classStream = this.elements.stream()
                .filter(a -> a.getElementType() == ElementType.TYPE)
                .flatMap(a -> {
                    try {
                        if (object != null && a.field.get(object) != null) {
                            return a.toEncodeContexts(a.field.get(object), datagram).stream();
                        } else {
                            return Stream.empty();
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new DecodeException(
                                MessageFormat.format(CodecError.FAIL_GETTING_FIELD_VALUE.getMessage(), this.clazz.getName()), e);
                    }
                });

        return Stream.concat(fieldStream, classStream)
                .filter(c -> c.getValue() != null)
                .collect(Collectors.toList());
    }
}