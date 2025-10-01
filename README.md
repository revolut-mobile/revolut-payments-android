# Revolut Android SDKs

This repository contains demo applications for Revolut's Android SDKs, showcasing how to integrate and use them in your Android applications.

## Demo Applications

### Revolut Pay Lite SDK Demo (New v3.+)
Located in the `revolut-pay-lite-sdk-demo/` module. This demonstrates the Revolut Pay SDK for accepting Revolut Pay payments in your app.

**Integration guide:** [Revolut Pay Lite Android SDK](https://developer.revolut.com/docs/guides/accept-payments/payment-methods/revolut-pay/mobile/android)

### Revolut Pay Legacy SDK Demo (Old v2.+)
Located in the `revolut-pay-legacy-sdk-demo/` module. This demonstrates the Revolut Pay SDK for accepting Revolut Pay payments in your app.

### Merchant Card Form SDK Demo
Located in the `merchant-card-form-sdk-demo/` module. This demonstrates the Revolut Card Form SDK for collecting card payment details.

**Integration guide:** [Revolut Card Form Android SDK](https://developer.revolut.com/docs/sdks/merchant-android-sdk/revolut-card-form-android-sdk/introduction)

## Running the Demos

To build and run a specific demo:

```bash
# Revolut Pay Lite SDK Demo (New v3.+)
./gradlew :revolut-pay-lite-sdk-demo:assembleDebug
./gradlew :revolut-pay-lite-sdk-demo:installDebug

# Revolut Pay Legacy SDK Demo (Old v2.+ )
./gradlew :revolut-pay-legacy-sdk-demo:assembleDebug
./gradlew :revolut-pay-legacy-sdk-demo:installDebug

# Merchant Card Form SDK Demo  
./gradlew :merchant-card-form-sdk-demo:assembleDebug
./gradlew :merchant-card-form-sdk-demo:installDebug
```

Both demo apps can be installed simultaneously as they have different application IDs.