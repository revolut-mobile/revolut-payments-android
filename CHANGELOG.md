# CHANGELOG

## X SDK XX.XX.XX - 20XX-XX-XX

## Revolut Pay SDK 3.3.0 - 2026-08-06

### What's changed

* Set payment state Abandoned in case when WebViewConfirmationActivity is closed without handling redirect
* Align RevPay button customization with iOS
* Add min sdk to analytics context in the RevPay SDK
* fix missing spm state

## Revolut Pay SDK 3.2.1 - 2026-06-23

### What's changed

* Replaced an old PascalCase enum vals of PreferredMode with the UPPER_CASE

## Revolut Pay SDK 3.2.0 - 2026-06-05

### What's changed

* Added preferredMode support in OrderParams (Retail, RetailOnly, Business, BusinessOnly)
* Moved queries package declaration into the SDK manifest to simplify integration
* Added packaging exclusion for META-INF resources to resolve build conflicts

## Merchant Card Form SDK 3.2.0 - 2026-06-05

### What's changed

* Version alignment with Revolut Pay SDK 3.2.0
* Added packaging exclusion for META-INF resources to resolve build conflicts

## Revolut Pay SDK 3.1.2 - 2026-01-22

### What's changed

* Fixed an issue where the SDK would crash when selecting a country


## Revolut Pay SDK 3.1.1 - 2026-01-19

### What's changed

* Fixed a bug that caused the SDK to crash in the React Native wrapper

## Revolut Pay SDK 3.1.0 - 2025-12-17

### What's changed

* Added SPM support
* Added a new and improved Revolut Pay SDK

## Revolut Pay Lite SDK 3.1.0 - 2025-12-17

### What's changed

* Added SPM support

## Merchant Card Form SDK 3.1.0 - 2025-12-17

### What's changed

* Removed jitpack.io usages
* Updated UserAgent
* Improved insets
* Clean up dependencies
* MIT/CIT improvements

## Revolut Pay Lite SDK 3.0.0 - 2025-10-01

### What's changed

* Revolut Pay Lite SDK 3.0.0 is replacement of RevolutPay SDK 2.10
* No need to add core dependency manually – it's now added transitively.
* Unified configuration – single setup point for all SDKs (env + public key).
* Naming aligned with iOS – for smoother cross-platform integration.
* Improved integration layer – payment result survives config changes; leaks fixed.
* Extended public API – merchants can observe PaymentState and reflect it in the UI;
  PaymentController works standalone or with custom buttons.

## Merchant Card Form SDK 3.0.0 - 2025-09-30

### What's changed

* New way of sdk configuration RevolutPaymentsSDK.configure(..)
* New CardPaymentLauncher with typed CardPaymentResult callback
* CardPaymentResult.UserAbandonedPayment explicit result

## RevolutPay SDK 2.10 - 2025-09-19

### What's changed

* Check opaque urls before getting query parameters from the Uri to prevent UnsupportedOperationException
