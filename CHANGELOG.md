# CHANGELOG

## X SDK XX.XX.XX - 20XX-XX-XX

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

* Added a user-abandoned payment callback in the 