# Media

Media components cover reusable image and rich-media display patterns.

## Components

- `CachedAsyncImage`

## Example

```kotlin
CachedAsyncImage(
    data = thumbnailUrl,
    contentDescription = title,
    contentScale = ContentScale.Crop,
)
```

`CachedAsyncImage` enables Coil memory and disk caching by default. Override `memoryCachePolicy`, `diskCachePolicy`, or `networkCachePolicy` when a screen needs read-only, write-only, or disabled cache behavior.

The public API uses `ImageCachePolicy` instead of Coil cache types so app feature modules do not need to depend on Coil directly.
