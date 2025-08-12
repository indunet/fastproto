# Transformation Formulas

A practical guide to transforming values during decode/encode.

## Overview

Formulas let you convert between raw on‑wire values and engineering values. You can express them as inline lambdas or as classes implementing `java.util.function.Function`.

- Decode formula: raw -> target field type
- Encode formula: target field type -> raw

## Quick Steps

1. Pick the field type and its data annotation (e.g., `@UInt32Type`).
2. Decide if a lambda is sufficient, or a reusable class is better.
3. Provide the correct input/output types for each direction.
4. Test round‑trip: value -> encode -> decode -> value.
5. Keep formulas pure (no side effects, deterministic).

## Core Concepts

### Direction & Types
- Decode: `Function<RawType, FieldType>`
  - Example: `@UInt32Type` raw is `Long`; field may be `Double` (engineering).
- Encode: `Function<FieldType, RawType>`
  - Example: reverse the above conversion.

### Lambda vs Class
- Lambda: `@DecodingFormula(lambda = "...")` / `@EncodingFormula(lambda = "...")`
- Class: `@DecodingFormula(MyFunc.class)` / `@EncodingFormula(MyFunc.class)` where class implements `Function<In, Out>`
- Precedence: Class‑based formulas win if both lambda and class are present.

### Lifecycle & Errors
- Formulas are compiled/bound during graph resolve and invoked per field during decode/encode.
- Throwing exceptions inside formulas will surface as `DecodingException` / `EncodingException` at call sites.

## Examples

### Lambda‑based unit conversion
```java
import org.indunet.fastproto.annotation.*;

public class Weather {
  @UInt32Type(offset = 14)
  @DecodingFormula(lambda = "x -> x * 0.1")            // raw uint32 -> Pa * 0.1 -> engineering
  @EncodingFormula(lambda = "x -> (long) (x * 10)")    // engineering -> raw
  double pressure;
}
```

### Class‑based (reusable) conversion
```java
import java.util.function.Function;

public class PressureDecode implements Function<Long, Double> {
  @Override public Double apply(Long raw) { return raw * 0.1; }
}

public class PressureEncode implements Function<Double, Long> {
  @Override public Long apply(Double eng) { return (long) (eng * 10); }
}
```

```java
public class Weather {
  @UInt32Type(offset = 14)
  @DecodingFormula(PressureDecode.class)
  @EncodingFormula(PressureEncode.class)
  double pressure;
}
```

### One‑way formulas
- You may specify only one side if the other is identity, e.g., only `@DecodingFormula`.

## Advanced

- Constants & context: Prefer constructor parameters or class constants inside class‑based formulas; avoid hidden globals.
- Composition: Split complex logic into smaller `Function` classes and delegate.
- Performance: Lambdas are compiled once and reused; keep formulas simple and avoid heavy allocations in hot paths.
- Validation: Add unit tests with known vectors; ensure symmetry if both directions are specified.

## Best Practices & Troubleshooting

- Ensure types match the codec direction (e.g., `UInt32` raw is `Long`).
- Document non‑obvious conversions (units, offsets) near the field.
- If results are off by a factor, verify integer vs floating arithmetic and cast points.
- If exceptions bubble up, catch and wrap with clear messages in class‑based formulas to ease debugging.
