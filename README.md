# **Revolut Pay SDK - Android documentation**
The Revolut Pay SDK for Android lets you accept [Revolut Pay](https://www.revolut.com/help/making-payments/what-is-revolut-pay-payment-method) payments from Revolut users directly from your app. It is designed for easy implementation and usage. The SDK allows you to create a Revolut Pay button and interact with the Revolut Retail app to verify the payment status.

**NOTE**

In order to use and accept payment via Revolut Pay, you need to have been [accepted as a Merchant](https://www.revolut.com/business/help/merchant-accounts/getting-started/how-do-i-apply-for-a-merchant-account) in your [Revolut Business](https://business.revolut.com/merchant) account.
### Get started with the Revolut Pay SDK for Android
Set up the Revolut Pay SDK to accept Revolut Pay payments directly in your app. It requires 5 steps:


1. Install the SDK

2. Configure the SDK

3. Get your merchant API key

4. Create an order

5. Button instantiation

### 1. Install the SDK
1. Since the SDK is hosted on mavenCentral, in order to fetch the dependency please add the following lines to your project level build.gradle:
```
allprojects {
    repositories {
        mavenCentral()
    }
}
```
2. Add the dependency to the module level build.gradle. Please use the latest 1.0.2 version
```
implementation 'com.revolut:revolutpay:1.0.2'
```
3. Sync your project

### 2. Configure the SDK
Initialise the SDK by invoking `RevolutPay.init(environment: RevolutPayEnvironment, returnUrl: String?)`, where you will need to define:

1. `environment` : either `PROD` or `SANDBOX`. You can use the Revolut Business Sandbox environment to test your Merchant account integration before you push the code changes to the production environment.

2. `returnUrl`  : an optional deep link which is going to be used by Revolut app to return back to your app after the payment is confirmed or rejected. Providing this field will greatly improve the customer experience as it will allow them to return to your app after they authorize the payment. The deep link should be registered in manifest to allow opening an activity if you want to support automatic redirection. Please note that the `returnUrl` might be as well based on a custom host+scheme that can be defined within your application. Here is an example of an activity that can handle `returnUrl` (please note that the launchMode should be set to `singleTop`, otherwise it will not be possible to return back to your app):
```
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
If this value is not provided, the customer will continue on the Revolut app and will need to return to your app.
If your app does not support universal links, providing the `returnUrl` will trigger the browser to attempt to open the link

3. Now you can integrate the `RevolutPayButton` into your layout. It can be done both by including it into you `.xml` file, or by creating it via `RevolutPay.provideButton()`.

**NOTE**

Since the SDK provides a method for polling the state of the order from BE, you need to make sure that the internet permission is added for your app. If it isn't, please add the following line to the manifest:
```
<uses-permission android:name="android.permission.INTERNET" />
```

### 3. Get your merchant API key
Go to your Revolut app to generate the [Merchant API key](https://business.revolut.com/settings/merchant-api). You need it as part of the authorisation header for each Merchant API request.

**NOTE**

Use this key only for the production environment. For the [Revolut Business Sandbox environment](https://sandbox-business.revolut.com/), use the [sandbox API key](https://sandbox-business.revolut.com/settings/merchant-api). For more information, see [Test in the Sandbox environment](https://developer.revolut.com/docs/accept-payments/tutorials/test-in-the-sandbox-environment/configuration)
### 4. Create an order
When a user decides to make a purchase on your e-commerce website, on the server side, you create an order by sending a `POST` request to `https://merchant.revolut.com/api/1.0/orders`. You must include the authorization header in the request, which is in the following format:

`Bearer [yourAPIKey]`

Where `[yourAPIKey]` is the production API key that you [generated from your Merchant account](https://business.revolut.com/settings/merchant-api).

Server side: Create an order via the Merchant API request:
```
curl -X "POST" "https://merchant.revolut.com/api/1.0/orders" \
     -H 'Authorization: Bearer [yourApiKey]' \
     -H 'Content-Type: application/json; charset=utf-8' \
     -d $'{
          "amount": 100,
          "currency": "GBP"
        }'
```
When the order is created successfully, the Merchant API returns a JSON array in the [response](https://developer.revolut.com/api-reference/merchant/#operation/createOrder) that looks like this:
```
{
  "id": "<ID>",
  "public_id": "<PUBLIC_ID>",
  "type": "PAYMENT",
  "state": "PENDING",
  "created_date": "2020-10-15T07:46:40.648108Z",
  "updated_date": "2020-10-15T07:46:40.648108Z",
  "order_amount": {
    "value": 100,
    "currency": "GBP"
  }
}
```
You should save the `public_id` which will be used to identify the order when the RevolutPay button is built.

Note: you must create a new order for each purchase

### 5. Button instantiation
#### a) Via adding button from kotlin/java code
In case you want to create a button from code, you can use the `RevolutPay.provideButton()` method, which has the following params:

`context` - an instance of context used to create a view

`publicId` - an id of a Revolut order

`params` - a set of parameters that allow to setup the appearance of the button

`errorHandler` an optional handler for errors such as not being able to launch activity when button is clicked

```
RevolutPay.provideButton(
        context: Context,
        publicId: String,
        params: ButtonParams,
        errorHandler: ErrorHandler?
): RevolutPayButton
```
#### b) Via adding button to an .xml layout file
In case of integrating the `RevolutPayButton` directly into layout you will need to provide an id of an order, which can be set via `RevolutPayButton.setPublicId(publicId)` before allowing to use the button. The `publicId` is required for opening Revolut app to allow customer to confirm the payment.

**NOTE**

After creating an instance of `RevolutPayButton` you must provide an id by either invoking `View.setId()` on an instance of the button, or by setting it in .xml layout file with `android:id="@+id/revolutButton"`. This id is required for saving and restoring the internal state of the button.

##### Button customization

In both cases, you can specify a list of parameters which would affect the appearance of the button. The list of parameters includes the following attributes:

`revolutPay_ActionType` - defines the text of the button

`revolutPay_Radius` - defines the corner radius of the button

`revolutPay_Size` - defines the size of the button

`revolutPay_VariantLightTheme` - defines the style of the button used when device is in the light mode

`revolutPay_VariantDarkTheme` - defines the style of the button used when device is in the dark mode
##### Available style settings:
```
enum class ActionType {
    NONE,
    PAY_WITH_REVOLUT,
    BUY_WITH_REVOLUT,
    DONATE_WITH_REVOLUT,
    SUBSCRIBE_WITH_REVOLUT
}

enum class Size {
    SMALL,
    LARGE
}

enum class Radius {
    NONE,
    SMALL,
    LARGE
}

enum class Variant {
    DARK,
    LIGHT,
    LIGHT_OUTLINED
}

data class VariantModes(
    val lightMode: Variant = Variant.DARK,
    val darkMode: Variant = Variant.LIGHT
)
```

#### Check current state of the order
For checking the current state of an order you should use `RevolutPay.fetchOrderState(publicId)`. This method returns an `OrderState` enum that represents the current state of the order. Here are the possible states:

`CREATED` - the order has been created, but it's not yet confirmed

`PENDING` - the order has been confirmed by the customer in Revolut app, and now is being processed

`COMPLETED` - the order has been successfully confirmed and processed

`FAILED` - the order was failed

**NOTE**

The `RevolutPay.fetchOrderState(publicId)` makes a synchronous network request in order to fetch the order state, therefore it should only be invoked from a background thread.

The `RevolutPayButton` also provides methods to show/hide progress inside the button (`showLoadingProgress()`/`hideLoadingProgress()`). Normally progress should be shown only when the last fetched state of order is `PENDING` and hidden for any other state. In simple words, this indicator is meant for showing that the order is being processed at the moment, not for showing the fact that order state is being polled in background.

# Methods available
Mandatory SDK initialization:
```
RevolutPay.init(environment: RevolutPayEnvironment, returnUrl: String?)
```
Button instantiation:
```
RevolutPay.provideButton(
        context: Context,
        publicId: String,
        params: ButtonParams
    ): RevolutPayButton
```
Make a synchronous network request to fetch the current state of an order:
```
RevolutPay.fetchOrderState(publicId)
```
Check if Revolut app is installed:
```
RevolutPay.isRevolutAppInstalled(packageManager: PackageManager): Boolean
```
Show loading indicator inside the button:
```
RevolutPayButton.showLoadingProgress()
```
Hide loading indicator inside the button:
```
RevolutPayButton.hideLoadingProgress()
```
Set the callback to be invoked in case of an error when SDK tries to launch an activity after button is clicked:
```
RevolutPayButton.setErrorHandler(errorHandler: ErrorHandler?)
```