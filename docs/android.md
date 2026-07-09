## Android Compatibility Guide

This page summarizes how to use FastProto on Android.

## What Works

- Annotation-based mapping for primitive types, arrays, strings, enums, bit/byte order, and checksum/CRC
- Decode/encode via `FastProto.decode(...)` and `FastProto.encode(...)`
- Builder/Utils APIs (no annotations)
- **Lambda formulas with annotation processor** (since 4.0.0)

## Lambda Formulas on Android

FastProto provides compile-time support for lambda formulas so `@DecodingFormula(lambda = "...")` and `@EncodingFormula(lambda = "...")` remain Android-friendly.

### Setup

Android projects can use the `fastproto` dependency directly. If you use lambda formulas, make sure annotation processing is enabled in your build.

**Gradle (Kotlin DSL)**:
```kotlin
dependencies {
    implementation("org.indunet:fastproto:4.2.0")
}
```

**Gradle (Groovy DSL)**:
```groovy
dependencies {
    implementation 'org.indunet:fastproto:4.2.0'
}
```

**Maven (Android)**:
```xml
<dependency>
    <groupId>org.indunet</groupId>
    <artifactId>fastproto</artifactId>
    <version>4.2.0</version>
</dependency>
```

### How It Works

The annotation processor scans your code at compile time and generates `Function` implementation classes for each lambda expression. These generated classes are included in your APK and used at runtime instead of dynamic compilation.

For example, this annotation:
```java
@Int16Type(offset = 0)
@DecodingFormula(lambda = "x -> x * 0.1")
private double temperature;
```

Generates a class like:
```java
public class YourClass_temperature_DecodingFormula implements Function<Integer, Object> {
    @Override
    public Object apply(Integer x) {
        return x * 0.1;
    }
}
```

### Alternative: Custom Function Classes

You can also use custom function classes directly without relying on the annotation processor:

```java
public class TemperatureFormula implements Function<Integer, Double> {
    @Override
    public Double apply(Integer value) {
        return value * 0.1;
    }
}

public class SensorData {
    @Int16Type(offset = 0)
    @DecodingFormula(TemperatureFormula.class)
    private double temperature;
}
```

## Current Limitations

- `java.sql.Timestamp` is not part of Android standard APIs
  - Impact: Types/codec relying on `java.sql.*` will not work. Prefer `long` epoch millis or `java.time.*` (with desugaring) instead.

## Other Android Setup

- **Time Types**
  - Prefer `long` (epoch millis) or `java.time.Instant/LocalDateTime`.
  - Enable core library desugaring to use `java.time.*` on older Android.
- **Gradle Setup (library/app module)**
  - compileOptions.sourceCompatibility/targetCompatibility = 1.8
  - coreLibraryDesugaringEnabled = true
  - Add dependency: `com.android.tools:desugar_jdk_libs`
- **R8/ProGuard**
  - Keep runtime annotations and members used by reflection
  - Example rules:

```pro
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, Signature, InnerClasses, EnclosingMethod
-keep class org.indunet.fastproto.annotation.** { *; }
-keep class org.indunet.fastproto.formula.generated.** { *; }
```

## FAQ

- **Can I use lambda formulas on Android?** Yes. Lambda formulas are generated at compile time, so they are compatible with Android as long as annotation processing is enabled.
- **Do I need the annotation processor?** If you use `@DecodingFormula(lambda = "...")` or `@EncodingFormula(lambda = "...")`, yes. If you only use class-based formulas like `@DecodingFormula(MyFormula.class)`, the annotation processor is optional.
- **Do I need ProGuard rules?** If you rely on reflection/annotations, keep them as shown above. The generated formula classes should also be kept.
