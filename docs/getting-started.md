# Getting Started

## Modules

Use the modules you need:

```kotlin
implementation("io.github.maniramezan.compose:theme:<version>")
implementation("io.github.maniramezan.compose:icons:<version>")
implementation("io.github.maniramezan.compose:components:<version>")
implementation("io.github.maniramezan.compose:secure-storage:<version>") // optional, standalone
```

`secure-storage` is independent of the Compose modules — see [Secure Storage](secure-storage.md).

## App setup

```kotlin
AppTheme(
    icons = defaultAppIcons(),
    dynamicColor = true,
) {
    PrimaryButton(
        text = "Continue",
        onClick = onContinue,
    )
}
```

For edge-to-edge apps, call `enableEdgeToEdge()` in every Activity and pass `Scaffold` insets into scrollable content.
