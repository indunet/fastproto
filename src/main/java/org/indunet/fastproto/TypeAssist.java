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
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.ValidationContext;
import org.indunet.fastproto.util.TypeUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.*;
import java.text.MessageFormat;
import java.util.*;
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
    protected static final ThreadLocal<Map<Class<?>, Object>> objects = new ThreadLocal<>();
    protected static final ThreadLocal<Set<Class<?>>> protocolClasses = new ThreadLocal<>();

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

    Integer fixedLength;

    EnableCrypto enableCrypto;
    byte[] key;

    EnableCompress enableCompress;
    EnableProtocolVersion enableProtocolVersion;
    EnableChecksum enableChecksum;

    Boolean circularReference = false;

    Integer byteOffset;
    Integer bitOffset;
    Integer size;
    Integer length;

    long codecFeature;

    Boolean noArgsConstructor;

    protected TypeAssist() {

    }

    public static TypeAssist byClass(Class<?> protocolClass) {
        return typeAssists.computeIfAbsent(protocolClass, c -> TypeAssist.get(c));
    }

    public static TypeAssist get(@NonNull Class<?> protocolClass) {
        protocolClasses.set(new HashSet<>());
        TypeAssist assist = resolveClass(protocolClass);

        // Crypto policy and key.
        val enableCrypto = Optional.of(protocolClass)
                .map(c -> c.getAnnotation(EnableCrypto.class))
                .orElse(null);

        assist.setEnableCrypto(enableCrypto);

        if (enableCrypto != null) {
            if (!enableCrypto.key().isEmpty()) {
                assist.setKey(
                        enableCrypto.key().getBytes());
            } else if (enableCrypto.keySupplier().length != 0) {
                assist.setKey(Optional.of(enableCrypto)
                        .map(EnableCrypto::keySupplier)
                        .map(a -> {
                            try {
                                val c = a[0];

                                return c.newInstance()
                                        .get();
                            } catch (InstantiationException | IllegalAccessException  e) {
                                throw new CryptoException(CodecError.INVALID_CRYPTO_KEY_SUPPLIER, e);
                            }
                        }).get()
                );
            } else {
                throw new CryptoException(CodecError.NO_CRYPTO_KEY);
            }
        }

        assist.setEnableCompress(Optional.of(protocolClass)
                .map(c -> c.getAnnotation(EnableCompress.class))
                .orElse(null));
        assist.setEnableProtocolVersion(Optional.of(protocolClass)
                .map(c -> c.getAnnotation(EnableProtocolVersion.class))
                .orElse(null));
        assist.setEnableChecksum(Optional.of(protocolClass)
                .map(c -> c.getAnnotation(EnableChecksum.class))
                .orElse(null));
        assist.setFixedLength(Optional.of(protocolClass)
                .map(c -> c.getAnnotation(EnableFixedLength.class))
                .map(EnableFixedLength::value)
                .orElse(null));

        assist.setCodecFeature(CodecFeature.of(assist));
        protocolClasses.remove();

        return assist;
    }

    protected static TypeAssist resolveClass(@NonNull Class<?> protocolClass) {
        protocolClasses.get().add(protocolClass);

        // Constructor flag
        val noArgsConstructor = Arrays.stream(protocolClass.getConstructors())
                .anyMatch(c -> c.getParameterCount() == 0);

        Predicate<Field> isType = f -> Arrays.stream(f.getAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(t -> t.isAnnotationPresent(TypeFlag.class));

        // Nested types.
        Stream<TypeAssist> typeStream = Arrays.stream(protocolClass.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(DecodeIgnore.class)
                        && !f.isAnnotationPresent(EncodeIgnore.class))
                .filter(isType.negate())
                .filter(f -> !f.getType().isEnum())  // Non enum.
                // .filter(f -> !Modifier.isFinal(f.getModifiers()))   // Non final.
                .filter(f -> !Modifier.isTransient(f.getModifiers()))   // Non transient.
                .filter(f -> !f.getType().isArray())    // Non array.
                .filter(f -> f.getType() instanceof Class)
                .filter(f -> Arrays.stream(ProtocolType.supportedTypes())
                        .noneMatch(t -> t == f.getType()))
                .map(f -> {
                    f.setAccessible(true);
                    Class<?> c = f.getType();

                    if (protocolClasses.get().contains(c)) {
                        Boolean decodeIgnore = f.isAnnotationPresent(DecodeIgnore.class);

                        return TypeAssist.builder()
                                .clazz(c)
                                .field(f)
                                .typeAnnotation(null)
                                .decoderClass(null)
                                .encoderClass(null)
                                .decodeFormula(null)
                                .encodeFormula(null)
                                .endianPolicy(EndianPolicy.LITTLE)
                                .decodeIgnore(decodeIgnore)
                                .encodeIgnore(true)
                                .elementType(ElementType.TYPE)
                                .circularReference(true)
                                .elements(new ArrayList<TypeAssist>())
                                .build();
                    } else {
                        TypeAssist a = TypeAssist.resolveClass(c);
                        Boolean decodeIgnore = f.isAnnotationPresent(DecodeIgnore.class);
                        Boolean encodeIgnore = f.isAnnotationPresent(EncodeIgnore.class);
                        a.setDecodeIgnore(decodeIgnore);
                        a.setEncodeIgnore(encodeIgnore);
                        a.setField(f);
                        a.setCircularReference(false);

                        return a;
                    }
                })
                .filter(a -> a.hasElement() || a.getCircularReference());

        // Default as little endian.
        EndianPolicy endianPolicy = Optional.ofNullable(protocolClass.getAnnotation(Endian.class))
                .map(Endian::value)
                .orElse(EndianPolicy.LITTLE);
        Boolean decodeIgnore = protocolClass.isAnnotationPresent(DecodeIgnore.class);
        Boolean encodeIgnore = protocolClass.isAnnotationPresent(EncodeIgnore.class);

        Stream<TypeAssist> fieldStream = Arrays.stream(protocolClass.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(DecodeIgnore.class)
                        && !f.isAnnotationPresent(EncodeIgnore.class))
                .filter(isType)
                .peek(f -> f.setAccessible(true))
                .map(TypeAssist::resolveField)
                .peek(a -> {
                    if (a.getEndianPolicy() == null) {
                        a.setEndianPolicy(endianPolicy);
                    }
                });

        TypeAssist assist = TypeAssist.builder()
                .clazz(protocolClass)
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
                .circularReference(false)
                .noArgsConstructor(noArgsConstructor)
                .build();

        List<TypeAssist> elements = Stream.concat(fieldStream, typeStream)
                .peek(a -> a.setParent(assist))
                .collect(Collectors.toList());
        assist.setElements(elements);

        return assist;
    }

    protected static TypeAssist resolveField(Field field) {
        EndianPolicy policy = Optional.ofNullable(field.getAnnotation(Endian.class))
                .map(Endian::value)
                .orElse(null);
        Boolean decodeIgnore = field.isAnnotationPresent(DecodeIgnore.class);
                // || Modifier.isFinal(field.getModifiers());       // Removed for case class.
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

        val context = ValidationContext.builder()
                .field(field)
                .typeAnnotation(getProxyTypeAnnotation(field))
                .build();

        try {
            AbstractFlow.getValidateFlow()
                    .process(context);
        } catch(CodecException e) {
            throw new CodecException(MessageFormat.format(
                    CodecError.FAIL_RESOLVING_FILED.getMessage(), field.toString()
            ), e);
        }


        val proxyTypeAnnotation = getProxyTypeAnnotation(field);

        return TypeAssist.builder()
                .clazz(field.getType())
                .field(field)
                .typeAnnotation(proxyTypeAnnotation)
                .decoderClass(decoder)
                .encoderClass(encoder)
                .decodeFormula(afterDecode)
                .encodeFormula(beforeEncode)
                .endianPolicy(policy)
                .decodeIgnore(decodeIgnore)
                .encodeIgnore(encodeIgnore)
                .elementType(ElementType.FIELD)
                .circularReference(false)
                .byteOffset(TypeUtils.byteOffset(proxyTypeAnnotation))
                .bitOffset(TypeUtils.bitOffset(proxyTypeAnnotation))
                .length(TypeUtils.length(proxyTypeAnnotation))
                .size(TypeUtils.size(proxyTypeAnnotation))
                .build();
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
            Class<? extends Annotation> typeAnnotationClass = ProtocolType
                    .byAutoType(field.getType())
                    .typeAnnotationClass;

            return typeAnnotationClass.cast(Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{typeAnnotationClass},
                    (object, method, parameters) -> {
                        return Arrays.stream(typeAnnotation.annotationType().getMethods())
                                .filter(m -> m.getName().equals(method.getName()))
                                .findAny()
                                .orElseThrow(CodecException::new)
                                .invoke(typeAnnotation);
                    }));
        } else {
            return typeAnnotation;
        }
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

    public DecodeContext toDecodeContext(byte[] datagram, Object object) {
        return DecodeContext.builder()
                .object(object)
                .datagram(datagram)
                .typeAssist(this)
                .build();
    }

    protected List<DecodeContext> toDecodeContexts(byte[] datagram, Object parent) {
        try {
            Object value;

            if (objects.get().containsKey(this.clazz)) {
                value = objects.get().get(this.clazz);
            } else {
                value = this.clazz.newInstance();
                objects.get().put(this.clazz, value);
            }

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
            throw new DecodeException(
                    MessageFormat.format(CodecError.FAIL_INITIALIZING_DECODE_OBJECT.getMessage(), this.clazz.getName()), e);
        }
    }

    public List<DecodeContext> toDecodeContexts(byte[] datagram) {
        objects.set(new HashMap<Class<?>, Object>());
        val contexts = this.toDecodeContexts(datagram, null);
        objects.remove();

        return contexts;
    }

    public EncodeContext toEncodeContext(Object object, byte[] datagram) {
        try {
            return EncodeContext.builder()
                    .datagram(datagram)
                    .typeAssist(this)
                    .value(this.field.get(object))
                    .build();
        } catch (IllegalAccessException e) {
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
                        throw new DecodeException(
                                MessageFormat.format(CodecError.FAIL_GETTING_FIELD_VALUE.getMessage(), this.clazz.getName()), e);
                    }
                });

        return Stream.concat(fieldStream, classStream)
                .filter(c -> c.getValue() != null)
                .collect(Collectors.toList());
    }
}