## Android Compatibility Guide

This page summarizes how to use FastProto on Android and current limitations.

## What Works

- Annotation-based mapping for primitive types, arrays, strings, enums, bit/byte order, and checksum/CRC
- Decode/encode via `FastProto.decode(...)` and `FastProto.encode(...)`
- Builder/Utils APIs (no annotations)

## Current Limitations

- Dynamic formula compilation is unavailable on Android
  - Reason: Android runtime does not ship `javax.tools.JavaCompiler`, nor support loading JVM `.class` via `URLClassLoader`/`defineClass`. Only DEX is supported at runtime.
  - Impact: Using string-based lambda in `@DecodingFormula(lambda = "...")` / `@EncodingFormula(lambda = "...")` will fail on Android.
- `java.sql.Timestamp` is not part of Android standard APIs
  - Impact: Types/codec relying on `java.sql.*` will not work. Prefer `long` epoch millis or `java.time.*` (with desugaring) instead.

## How to Use on Android

- Formulas
  - Do NOT use string-based lambdas in annotations.
  - Instead, implement `java.util.function.Function` (or a typed interface) as a normal class and reference the class in annotations.
- Time Types
  - Prefer `long` (epoch millis) or `java.time.Instant/LocalDateTime`.
  - Enable core library desugaring to use `java.time.*` on older Android.
- Gradle Setup (library/app module)
  - compileOptions.sourceCompatibility/targetCompatibility = 1.8
  - coreLibraryDesugaringEnabled = true
  - Add dependency: `com.android.tools:desugar_jdk_libs`
- R8/ProGuard
  - Keep runtime annotations and members used by reflection
  - Example rules:

```pro
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, Signature, InnerClasses, EnclosingMethod
-keep class org.indunet.fastproto.annotation.** { *; }
# If you include the core jar and want to silence unused tools warnings:
-dontwarn javax.tools.**
-dontwarn java.net.URLClassLoader
```

## Known Issues and Roadmap

- Dynamic compiler will be made optional/disabled on Android to avoid `javax.tools` and custom classloader usage
- Provide time codecs that do not depend on `java.sql.Timestamp` (prefer `Instant/long`)
- Consider publishing an Android-friendly artifact variant

These improvements are planned for future releases.

## FAQ

- Can I still transform values? Yes. Implement a class-based function and refer to it in annotations, or transform in your own code before/after FastProto.
- Do I need ProGuard rules? If you rely on reflection/annotations, keep them as shown above. If you do not use dynamic compilation, shrinkers will strip those classes automatically. 