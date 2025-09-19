# Lets_Go_Activity_Setup — seed Activities, Categories & Icons (Android/Kotlin)

Small internal Android tool that **creates/updates server-side activities, categories, and icon assets** for the Lets Go platform via **gRPC/Protobuf**. Useful for initial catalog setup and quick iterations on naming, age-gates, colors, and icons.

> **Stack:** Kotlin · Android · gRPC (AndroidChannelBuilder) · Protobuf  
> **Talks to:** `Lets_Go_Server` admin RPCs (`ServerSpecificCommandsService`, `RequestFieldsService`)

---

## What it does (skim me)
- **Set Categories** — name, min age, color, icon index.  
- **Set Activities** — name, min age, category index, icon index.  
- **Upload Icons** — basic, inverted, and no-border PNG variants in one request.  
- **Request Icons** — fetch server-hosted icons for verification.  
- Includes simple **test streams / unary** stubs for debugging the gRPC path.

---

## How it works

- **Main UI**: three buttons → **Categories**, **Activities**, **Icons**.  
  Each builds a batch of requests and calls the corresponding RPC:
  - `setServerCategoryRPC(SetServerActivityOrCategoryRequest)`  
  - `setServerActivityRPC(SetServerActivityOrCategoryRequest)`  
  - `setServerIconRPC(SetServerIconRequest)`
- **gRPC client**: uses `AndroidChannelBuilder.forAddress("10.0.2.2", 50051).usePlaintext()` by default (Android emulator → host).
- **Security**: requests include a shared **password** (`ClientRPC.password`) for simple server-side gating.
- **Icon pipeline**: reads PNGs from `res/drawable*`, converts to `ByteString`, and sends **three variants** (basic/inverted/no-border) with sizes.

---

## Code tour (where to look)

**App code**
- `app/src/main/java/com/example/seticonsactivitiesandcategoriesletsgo/`
  - **`MainActivity.kt`** — UI entry; button handlers that:
    - Build sample **categories/activities** payloads (`SetServerActivityOrCategoryRequest`)
    - Convert **drawable** resources to `ByteString` and send **icons** (`SetServerIconRequest`)
    - Display success/failure messages
  - **`ClientRPC.kt`** — thin gRPC wrapper:
    - `setCategories(...)`, `setActivities(...)`, `setIcons(...)` → streaming client calls with `CountDownLatch` completion
    - **Icon request** example: `RequestFieldsServiceGrpc.requestServerIconsRPC(ServerIconsRequest)`
    - Debug helpers for **unary / server-streaming / client-streaming / BiDi** (test stubs)
    - `HeaderClientInterceptor` (captures trailers/status on stream close; handy for diagnosing admin RPCs)
    - `channel` + `password` (default endpoint and simple auth token)
  - **`Utility.kt`** — small DTOs (e.g., `ActivityCategoryObject`)

**Protobufs**
- `app/src/main/proto/` — admin RPC contracts used by this tool (compiled into stubs)

**Resources**
- `app/src/main/res/drawable*` — icon PNGs (basic/inverted/no-border) used by **Icons** upload  
- `layout`, `values`, `mipmap*` — standard Android resources

**Tests**
- `app/src/androidTest/java/...` & `app/src/test/java/...` — hooks for instrumentation/unit tests

---

## Example payloads (trimmed)

```kotlin
// Category
SetServerActivityOrCategoryRequest.newBuilder()
  .setPassword(ClientRPC.password)
  .setDeleteThis(false)
  .setName("Fitness")
  .setMinAge(13)
  .setColor("#8D4E76")
  .setCategoryIndex(0)
  .setIconIndex(0)
  .build()

// Activity
SetServerActivityOrCategoryRequest.newBuilder()
  .setPassword(ClientRPC.password)
  .setDeleteThis(false)
  .setName("Swimming")
  .setMinAge(13)
  .setCategoryIndex(0)
  .setIconIndex(0)
  .build()

// Icon (three variants)
SetServerIconRequest.newBuilder()
  .setPassword(ClientRPC.password)
  .setPushBack(true)
  .setIndexNumber(-1)
  .setIconActive(true)
  .setCompressedImage(basicPng)            // ByteString
  .setCompressedImageSize(basicPng.size())
  .setCompressedImageInverted(invertedPng)
  .setCompressedImageInvertedSize(invertedPng.size())
  .setCompressedImageNoBorder(noBorderPng)
  .setCompressedImageNoBorderSize(noBorderPng.size())
  .build()
```

> Note: Sample activities include obvious placeholders (e.g., “Dragon Riding”). Replace with your real catalog before running against a live server.

## Practical notes
- Default endpoint: `10.0.2.2:50051` for emulator → host; change in `ClientRPC.channel` as needed.  
- Icon sizes: this tool doesn’t enforce a max; prefer server-side validation.  
- The shared password is for convenience; production systems should use stronger auth.

---

## Related
- **Server (C++)** — receives these admin commands: https://github.com/lets-go-app-pub/Lets_Go_Server  
- **Desktop Admin (Qt)** — another admin surface: https://github.com/lets-go-app-pub/Lets_Go_Desktop_Interface  
- **Protobuf Definitions** — shared contracts: https://github.com/lets-go-app-pub/Lets_Go_Protobuf

## Status & compatibility
Portfolio reference / internal admin tool. Android/Gradle versions may be legacy; gRPC endpoints are placeholders.

## License
MIT
