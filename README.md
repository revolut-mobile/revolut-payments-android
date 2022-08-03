# Revolut Pay: Android SDK

The Revolut Pay SDK for Android lets you
accept [Revolut Pay](https://www.revolut.com/help/making-payments/what-is-revolut-pay-payment-method)
payments from Revolut users directly from your app. It is designed for easy implementation and
usage. The SDK allows you to create a Revolut Pay button and interact with the Revolut Retail app to
verify the payment status.

**NOTE**

In order to use and accept payment via Revolut Pay, you need to have
been [accepted as a Merchant](https://www.revolut.com/business/help/merchant-accounts/getting-started/how-do-i-apply-for-a-merchant-account)
in your [Revolut Business](https://business.revolut.com/merchant) account.

## Get started with the Revolut Pay SDK for Android

Set up the Revolut Pay SDK to accept Revolut Pay payments directly in your app. It requires 5 steps:

1. [Install the SDK](#markdown-header-install-the-sdk)

2. [Configure the SDK](#markdown-header-configure-the-sdk)

3. [Get your merchant API key](#markdown-header-get-your-merchant-api-key)

4. [Button instantiation](#markdown-header-button-instantiation)

### Install the SDK

1. Since the SDK is hosted on mavenCentral, in order to fetch the dependencies please add the
   following lines to your project level build.gradle:

```groovy
allprojects {
    repositories {
        mavenCentral()
    }
}
```

2. Add the dependency to the module level build.gradle:

```groovy
implementation 'com.revolut:revolutpayments:1.0.0'
implementation 'com.revolut:revolutpay:2.0.0'
```

3. Sync your project

**NOTE**

The minimum Android SDK version that is supported by the SDK is Android 5.0 (API 21).

### Configure the SDK

Initialise the SDK by
invoking `RevolutPayments.revolutPay.init(environment: RevolutPayEnvironment, returnUrl: String, merchantPublicKey: String)`
, where you will need to define:

1. `environment` : either `MAIN` or `SANDBOX`. You can use the Revolut Business Sandbox environment
   to test your Merchant account integration before you push the code changes to the production
   environment.

2. `returnUrl`  : a mandatory deep link parameter which is going to be used by Revolut app to return
   back to your app after the payment is confirmed or rejected. This will greatly improve the
   customer experience as it will allow them to return to your app after they authorize the payment.
   The deep link should be registered in manifest to allow opening an activity if you want to
   support automatic redirection. Please note that the `returnUrl` might be as well based on a
   custom host+scheme that can be defined within your application. Here is an example of an activity
   that can handle `returnUrl` (please note that the launchMode should be set to `singleTop`,
   otherwise it will not be possible to return back to your app):

```xml
<activity
    android:name=".MainActivity"
    android:launchMode="singleTop">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />

        <data
            android:host="your_app_name"
            android:scheme="app" />
    </intent-filter>
</activity>
```

If this value is not provided, the customer will continue on the Revolut app and will need to return
to your app. If your app does not support universal links, providing the `returnUrl` will trigger
the browser to attempt to open the link

3. `merchantPublicKey` : a public key for your merchant account from Revolut console

Now you can integrate the `RevolutPayButton` into your layout. It can be done both by including it
into you `.xml` file, or by creating it using `RevolutPayments.revolutPay.provideButton()`.

**NOTE**

Since the SDK relies on the internet connection in order to process your order, you need to make
sure that the internet permission is added for your app. If it isn't, please add the following line
to the manifest:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Also please note that if your app targets Android 11 (API level 30) or higher and you would like to
check if the Revolut retail app is installed before showing the Revolut Pay button, then you'll need
to add the following code to your manifest in order to allow the SDK to check for the presence of
the Revolut app on your client's device:

```xml
<queries>
    <package android:name="com.revolut.revolut" />
</queries>
```

### Get your merchant API key

Go to your Revolut app to generate
the [Merchant API key](https://business.revolut.com/settings/merchant-api). You need it as part of
the authorisation header for each Merchant API request.

**NOTE**

Use this key only for the production environment. For
the [Revolut Business Sandbox environment](https://sandbox-business.revolut.com/), use
the [sandbox API key](https://sandbox-business.revolut.com/settings/merchant-api). For more
information,
see [Test in the Sandbox environment](https://developer.revolut.com/docs/accept-payments/tutorials/test-in-the-sandbox-environment/configuration)

### 4. Button instantiation

#### a) Via adding button from kotlin/java code

In case you want to create a button from code, you can use
the `RevolutPayments.revolutPay.provideButton()` method, which has the following params:

`context` - an instance of context used to create a view

`params` - a set of parameters that allow to setup the appearance of the button

```kotlin
RevolutPayments.revolutPay.provideButton(
    context: Context,
    params: ButtonParams
): RevolutPayButton
```

#### b) Via adding button to an .xml layout file

You can also integrate the button into your layout by adding it to an .xml file in the `layout`
directory.

```
<com.revolut.revolutpay.ui.button.RevolutPayButton
    android:id="@+id/revolut_pay_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:revolutPay_Radius="Medium"
    app:revolutPay_Size="Large"
    app:revolutPay_BoxText="GetCashbackValue"
    app:revolutPay_BoxTextCurrency="GBP"
    app:revolutPay_BoxTextValue="Medium"
    app:revolutPay_VariantDarkTheme="Dark"
    app:revolutPay_VariantLightTheme="Light" />
```

##### Button customization

In both cases, you can specify a list of parameters which would affect the appearance of the button.
The list of parameters includes the following attributes:

`revolutPay_Radius` - defines the corner radius of the button

`revolutPay_Size` - defines the size of the button

`revolutPay_BoxText` - defines the appearance of the view which is shown under the Revolut Pay
button for informing the user about the provided cashback

`revolutPay_BoxTextCurrency` - defines the cashback currency of the view which is shown under the
Revolut Pay button for informing the user about the provided cashback

`revolutPay_BoxTextValue` - defines the value for cashback of the view which is shown under the
Revolut Pay button for informing the user about the provided cashback

`revolutPay_VariantLightTheme` - defines the style of the button used when device is in the light
mode

`revolutPay_VariantDarkTheme` - defines the style of the button used when device is in the dark mode

###### Available style settings:

```
enum class Size {
    EXTRA_SMALL,
    SMALL,
    MEDIUM,
    LARGE
}

enum class Radius {
    NONE,
    SMALL,
    MEDIUM,
    LARGE
}

enum class BoxText {
    NONE,
    GET_CASHBACK_VALUE
}

enum class BoxTextCurrency {
   GBP,
   EUR,
   USD
}

enum class BoxTextValue {
   HIGH,
   MEDIUM
}

enum class Variant {
    DARK,
    LIGHT,
    LIGHT_OUTLINED,
    DARK_OUTLINED
}

data class VariantModes(
    val lightMode: Variant = Variant.DARK,
    val darkMode: Variant = Variant.LIGHT
)
```

#### Handle Revolut Pay button clicks

In order to process the user's click of Revolut Pay button, the following should be done:

1. Invoke `RevolutPayButton.createController()` for the instance of the Revolut Pay button. This
   method will create an instance of `Controller`, to provide parameters for the payment
   configuration.
2. After an instance of `Controller` is created you have to set up a callback by
   calling `setHandler()` method. This callback will be invoked once the user clicks the button, and
   an instance of the `ConfirmationFlow` will be provided for the further processing. Once the
   callback is invoked, you have to create an order (please refer
   to [this document](https://developer.revolut.com/docs/api-reference/merchant/#operation/createOrder)
   to find more details about order creation), and once it's created please follow these steps:
    * Invoke the `ConfirmationFlow.setOrderToken()` method for setting the order token (which you
      should receive once you create an order).
    * Set up the lifecycle of the component that hosts the button (most likely it's going to be
      either `Fragment` or `Activity`) via `ConfirmationFlow.attachLifecycle()`. The Revolut Pay
      button internally polls the latest state of the order from BE in order to notify the user
      about success on a timely manner, and `Lifecycle` is used to pause/restart polling when the
      app goes to background, or user navigates from the screen that contains Revolut Pay button.
    * After the order token and lifecycle object are set, continue the flow by invoking
      the `ConfirmationFlow.continueConfirmationFlow()`.
3. Apart from click handler you should also set up the callback to be invoked when the payment
   succeeds or fails. For this purpose the `Controller.setOrderResultCallback()` should be used.

The following snippet showcases how to set up the Revolut Pay button properly:

```
revolutPayButton.createController().apply {
    setHandler { flow ->
        //create an order via sending a BE request
        flow.setOrderToken(orderToken = orderToken)
        flow.attachLifecycle(this@FlowDemoFragment.lifecycle)
        flow.continueConfirmationFlow()
    }
    setOrderResultCallback(object : OrderResultCallback {
        override fun onOrderCompleted() {
            //Inform the user about successful payment
        }
            override fun onOrderFailed(throwable: Throwable) {
            //Inform the user about a failure
        }
    })
}
```

**NOTE**

Please note that the callbacks that are set via `setHandler()` and `setOrderResultCallback()` are
going to be invoked on the main thread, so if you need to do make a network request or some other
time consuming operation, you will have to switch to background thread

#### Payment confirmation

After the order is created and the `ConfirmationFlow.continueConfirmationFlow()` has been invoked
there might be 2 different flows that will allow the user to confirm the payment. Which flow is
going to be utilised for the particular user is based on the presence/absence of the Revolut retail
app on their device.

In case if the Revolut retail app is installed, this app is going to be opened to allow the user to
confirm the payment within the app. This will simplify the confirmation procedure for the user since
the user most likely already has a Revolut account and signed in within the app. In such case, the
user will have to enter their passcode and simply click the `Confirm` button. Once the payment is
confirmed the client will see the success message and shortly after will be redirected back to your
app.

However it's possible that the user doesn't have the Revolut app installed. In such case they won't
leave the app where the SDK is integrated, instead the SDK will open an activity that includes a
webview, which is going to be used for letting the user to make a payment. Once the payment is
confirmed, the activity will be automatically closed and the user will return back to the screen
where the Revolut Pay button is integrated.

#### Methods available

Mandatory SDK initialization:

```
RevolutPayments.revolutPay.init(
    environment: RevolutPayEnvironment,
    returnUri: String,
    merchantPublicKey: String
)
```

Button instantiation:

```
RevolutPayments.revolutPay.provideButton(
    context: Context,
    params: ButtonParams
): RevolutPayButton
```

Check if Revolut app is installed:

```
RevolutPayments.revolutPay.isRevolutAppInstalled(context: Context): Boolean
```

Set up the callback to be invoked once the user clicks the Revolut Pay button:

```
Controller.setHandler(onClick: (ConfirmationFlow) -> Unit)
```

Set up the callback to be invoked once the payment is successfully confirmed or failed:

```
Controller.setOrderResultCallback(orderResultCallback: OrderResultCallback)
```

Set up order token once the order has been created:

```
ConfirmationFlow.setOrderToken(orderToken: String)
```

Attach an instance of `Lifecycle` to the button:

```
ConfirmationFlow.attachLifecycle(lifecycle: Lifecycle)
```

Proceed with the payment once order token and lifecycle has been set:

```
ConfirmationFlow.continueConfirmationFlow()
```
