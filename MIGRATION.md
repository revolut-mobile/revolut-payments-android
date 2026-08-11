# Migration Guide

## 3.2.0 → 3.3.0

### Summary

The `Variant` enum values have been **renamed** to describe literal button appearance instead of the system theme mode they are intended for, aligning with the iOS SDK. The `VariantModes` data class defaults have been updated accordingly. The `ButtonParams` constructor parameter `variantModes` has been renamed to `variants`.

### Old → New Mapping

| Old `Variant` (3.2.0) | New `Variant` (3.3.0) | Appearance |
|---|---|---|
| `Variant.DARK` | `Variant.LIGHT` | Light background |
| `Variant.LIGHT` | `Variant.DARK` | Dark background |
| `Variant.DARK_OUTLINED` | `Variant.LIGHT_OUTLINED` | Light background with dark outline |
| `Variant.LIGHT_OUTLINED` | `Variant.DARK_OUTLINED` | Dark background with light outline |

### Defaults

| Type | `lightMode` default | `darkMode` default |
|---|---|---|
| `VariantModes` (3.2.0) | `Variant.LIGHT` | `Variant.DARK` |
| `VariantModes` (3.3.0) | `Variant.DARK` | `Variant.LIGHT` |

Both produce the same visual result: dark button in light mode, light button in dark mode.

### Programmatic API Migration

#### Before (3.2.0)

```kotlin
val params = ButtonParams(
    radius = Radius.NONE,
    buttonSize = Size.LARGE,
    variantModes = VariantModes(
        lightMode = Variant.LIGHT,
        darkMode = Variant.DARK,
    ),
    boxText = BoxText.NONE,
)
```

#### After (3.3.0)

```kotlin
val params = ButtonParams(
    radius = Radius.NONE,
    buttonSize = Size.LARGE,
    variants = VariantModes(
        lightMode = Variant.DARK,
        darkMode = Variant.LIGHT,
    ),
    boxText = BoxText.NONE,
)
```

Or using the defaults:

```kotlin
val params = ButtonParams(
    radius = Radius.NONE,
    buttonSize = Size.LARGE,
)
```

#### copy() Migration

```kotlin
// Before
val updated = params.copy(
    variantModes = VariantModes(
        lightMode = Variant.LIGHT,
        darkMode = Variant.DARK,
    ),
)

// After
val updated = params.copy(
    variants = VariantModes(
        lightMode = Variant.DARK,
        darkMode = Variant.LIGHT,
    ),
)
```

#### Destructuring

The third component type remains `VariantModes`, but its `Variant` values have changed meaning. Code that destructures `ButtonParams` must recompile:

```kotlin
val (_, _, variants) = params
```

### Revolut Pay Lite XML Attribute Migration

The XML attributes described in this section are part of the Revolut Pay Lite SDK.

#### Before (removed)

```xml
app:revolutPay_VariantLightTheme="Light"
app:revolutPay_VariantDarkTheme="Dark"
```

#### After

```xml
app:revolutPay_VariantLightMode="Dark"
app:revolutPay_VariantDarkMode="Light"
```

The XML enum value names (`Light`, `Dark`, `LightOutlined`, `DarkOutlined`) are the same for both old and new attrs. The new attr names use `VariantLightMode`/`VariantDarkMode` to match the `VariantModes` field names.

### Breaking Changes

- `Variant` enum values — **semantically inverted**. `DARK` now means dark background (was white), `LIGHT` now means light background (was black).
- `ButtonParams(variantModes = ...)` constructor — **renamed**. Use `ButtonParams(variants = ...)`.
- `ButtonParams.variantModes` property — **renamed**. Use `ButtonParams.variants`.
- `VariantModes` defaults — **changed**. `lightMode` defaults to `Variant.DARK` (was `Variant.LIGHT`), `darkMode` defaults to `Variant.LIGHT` (was `Variant.DARK`).
- `revolutPay_VariantLightTheme` / `revolutPay_VariantDarkTheme` XML attrs — **deleted**. Use `revolutPay_VariantLightMode` / `revolutPay_VariantDarkMode`.
- The JVM descriptors of `copy`, `copy$default`, and `component3` changed. Applications or libraries containing code compiled against the previous SDK must be recompiled.

### Theme Resolution

Both Lite and Native use a single iOS-aligned theme resolver. The resolver maps `Variant` names literally to button appearance:

| `Variant` | Appearance |
|---|---|
| `LIGHT` | White background |
| `DARK` | Black background |
| `LIGHT_OUTLINED` | White background with outline |
| `DARK_OUTLINED` | Black background with outline |
