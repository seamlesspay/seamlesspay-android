<!-- [<img width="250" height="119" src="https://raw.githubusercontent.com/seamlesspay/seamlesspay-android/master/assets/stripe_logo_slate_small.png"/>](https://seamlesspay.com/docs/mobile/android) -->

# SeamlessPay Android

[![License](https://img.shields.io/github/license/seamlesspay/seamlesspay-android)](https://github.com/seamlesspay/seamlesspay-android/blob/master/LICENSE)

_The Seamless Payments Android SDK makes it quick and easy to build an excellent
payment experience in your Android app_ :robot:

## Overview

SeamlessPay Android provides drop-in customizable UI elements that can be used
to collect your users' payment details and process payments from within your
app. We also expose the low-level APIs that power those UIs so that you can
build fully custom experiences.

There are several components that comprise this SDK:

- [Client](Client) provides the networking, communication and modeling layer
- [CardForm](CardForm) provides a drop-in UI for collecting payment information in your app
- [Singlefield](Singlefield) provides an advanced drop-in UI that uses a single input

The individual components may be used for advanced integrations
and are available as modules in maven.

| Package                                                                 | Description                                                 |
| ----------------------------------------------------------------------- | ----------------------------------------------------------- | 
| [**`api`**](https://repo1.maven.org/maven2/com/seamlesspay/api)                 | Seamless Payments API Client for Android                    | 
| [**`card-form`**](https://repo1.maven.org/maven2/com/seamlesspay/card-form)     | UI component for card input using a traditional form layout |
| [**`singlefield`**](https://repo1.maven.org/maven2/com/seamlesspay/singlefield) | UI component for card input using a single-field widget     |

Get started with our [📚 integration guide](https://docs.seamlesspay.com/#tag/SeamlessPayCoreFrameworkAndroid) and [example projects](#examples).

## Installation

Your development environment must have minimum requirements configured:

<details><summary><strong>Requirements</strong></summary><p>

- [Java 8](https://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html) is installed and available in your `PATH`
- [Android Studio](https://developer.android.com/studio/)
- [Gradle](https://gradle.org/releases/) 5.4.1+
- [Android SDK](https://developer.android.com/studio/releases/sdk-tools) >= 21 -
  If you do not have the Android SDK installed, run `./gradlew build`
  3 times to download the Android SDK and install all required tools
  as well as set your `local.properties` file (we use
  [sdk-manager-plugin](https://github.com/JakeWharton/sdk-manager-plugin) to do this automatically).

Note: If you do have the Android SDK installed, add a `local.properties`
file to the top level directory with `sdk.dir=/path/to/your/sdk/.android-sdk`

</p></details>

Once your environment is configured, add the SeamlessPay Android SDK
to your project using your preferred repository, for example:

<details><summary><strong>Gradle</strong></summary><p>

Add the required dependencies to your project's **`build.gradle`**:

```groovy
dependencies {
  /* API Client */
  implementation 'com.seamlesspay.api:Client:[VERSION]'

  /* Card Form UI */
  implementation 'com.seamlesspay.sdk:CardForm:[VERSION]'

  /* Single Field UI */
  implementation 'com.seamlesspay.ui:Singlefield:[VERSION]'
}
```

_Note: Be sure to replace **`[VERSION]`** with the correct semantic
version of package._

</p></details>

<details><summary><strong>Maven</strong></summary><p>

Add the required dependencies to your project's **`pom.xml`**:

```xml
<!-- API Client -->
<dependency>
	<groupId>com.seamlesspay.api</groupId>
	<artifactId>Client</artifactId>
	<version>[VERSION]</version>
	<type>pom</type>
</dependency>

<!-- Card Form UI -->
<dependency>
	<groupId>com.seamlesspay.sdk</groupId>
	<artifactId>CardForm</artifactId>
	<version>[VERSION]</version>
	<type>pom</type>
</dependency>

<!-- Single Field UI -->
<dependency>
	<groupId>com.seamlesspay.ui</groupId>
	<artifactId>Singlefield</artifactId>
	<version>[VERSION]</version>
	<type>pom</type>
</dependency>
```

_Note: Be sure to replace **`[VERSION]`** with the correct
semantic version of package._

</p></details>

<details><summary><strong>Apache Ivy</strong></summary><p>

Add the required dependencies to your project's **`ivy.xml`**:

```xml
<!-- API Client -->
<dependency org="com.seamlesspay.api" name="Client" rev="[VERSION]">
	<artifact name="Client" ext="pom"></artifact>
</dependency>

<!-- Card Form UI -->
<dependency org="com.seamlesspay.sdk" name="CardForm" rev="[VERSION]">
	<artifact name="CardForm" ext="pom"></artifact>
</dependency>

<!-- Single Field UI -->
<dependency org="com.seamlesspay.ui" name="Singlefield" rev="[VERSION]">
	<artifact name="Singlefield" ext="pom"></artifact>
</dependency>
```

_Note: Be sure to replace **`[VERSION]`** with the correct
semantic version of package._

</p></details>

In most cases, you will only need to install the **`api`** and one of the
UI packages, either `card-form` **or** `singlefield`.<br/>

## UI Components

We provide several drop-in native UI components for easily collecting
payment information in your Android Application:

### CardForm Component

The `CardForm` Component is a drop-in UI layout that can be included in your
app making it easy to accept credit and debit cards.

<details><summary><strong>Usage & Example</strong></summary><p>

<img align="right" width="20%" src="/files/cardform.png"/>

`CardForm` is a `LinearLayout` widget that you can easily add to your app:

```xml
<com.seamlesspay.cardform.view.CardForm
  android:id="@+id/card_form"
  android:layout_width="match_parent"
  android:layout_height="match_parent" />
```

To initialize the view and change which fields are required for the user to
enter, use the required field methods
and **`CardForm#setup(AppCompatActivity activity)`**:

```java
CardForm cardForm = (CardForm) findViewById(R.id.card_form);
cardForm.cardRequired(true)
  .expirationRequired(true)
  .cvvRequired(true)
  .postalCodeRequired(true)
  .mobileNumberRequired(false)
  .actionLabel(getString(R.string.purchase))
  .setup(activity);
```

#### CardForm API

The `CardForm` instance exposes several helper methods:

- **`isValid()`**: Checks if `CardForm` input is valid
- **`validate()`**: Validates each required field, shows validation errors
- **`setCardNumberError(String)`**: Sets a custom error messages on given field

Additionally `CardForm` has 4 available event listeners:

- **`setOnCardFormValidListener`**: CardForm validation has changed state
- **`setOnCardFormSubmitListener`**: Called when CardForm should be submitted
- **`setOnFormFieldFocusedListener`**: A field in the form was focused
- **`setOnCardTypeChangedListener`**: The `CardType` has changed

#### CardForm Demo

Start with the provided **[`Demo`](Demo)**
App for an example of basic setup and usage of CardForm.

</p></details>

### Single Field Input

The `Singlefield` Input is a drop-in UI layout that can be included in your app
which uses a single input field to accept account information.

<details><summary><strong>Usage & Example</strong></summary><p>

<img align="right" width="20%" src="/files/singlefield.gif"/>

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
  xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:tools="http://schemas.android.com/tools"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  app:layout_behavior="@string/appbar_scrolling_view_behavior"
  tools:showIn="@layout/activity_checkout"
  tools:context=".CardActivity">

  <!--  ...  -->

  <com.seamlesspay.ui.view.CardInputWidget
    android:id="@+id/cardInputWidget"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginLeft="20dp"
    android:layout_marginRight="20dp"/>

  <Button
    android:text="Pay"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:id="@+id/payButton"
    android:layout_marginTop="20dp"
    app:layout_constraintTop_toBottomOf="@+id/cardInputWidget"
    app:layout_constraintStart_toStartOf="@+id/cardInputWidget"
    app:layout_constraintEnd_toEndOf="@+id/cardInputWidget"/>

  <!--  ...  -->

</androidx.constraintlayout.widget.ConstraintLayout>
```

To access the values in the form, use provided getters for each field:

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);

  setContentView(R.layout.activity_main);

  Button payButton = findViewById(R.id.payButton);

  mCardInputWidget = (CardInputWidget) findViewById(R.id.cardInputWidget);
  mCardInputWidget.configureForUs();

  payButton.setOnClickListener(new View.OnClickListener() {
    public void onClick(View v) {
      // Executes on main thread after user presses button
      mCardInputWidget.clearFocus();

      CardBuilder cardBuilder = new CardBuilder()
        .accountNumber(mCardInputWidget.getCardNumber())
        .expirationMonth(mCardInputWidget.getExpirationMonth())
        .expirationYear(mCardInputWidget.getExpirationYear())
        .setTxnType(CardBuilder.Keys.CREDIT_CARD_TYPE)
        .billingZip(mCardInputWidget.getPostalCode())
        .cvv(mCardInputWidget.getCvv())
        .verification(true);

      PanVault.tokenize(mSeamlesspayFragment, cardBuilder);
    }
  });

  // ...
```

#### Singlefield Demo

Start with the provided demo **[`DemoSinglefield`](DemoSingleField)** App
for a working example with basic setup and usage.

</p></details>

## Authorization

To authenticate requests, use **`Authorization#fromKeys()`** with your
`environment` and `publishable_key` to generate credentials.

<details><summary><strong>Example & API</strong></summary><p>

### Example

```java
import com.seamlesspay.api.SeamlesspayFragment;
import com.seamlesspay.api.Authorization;

public class CardActivity { // ...

  Authorization authorization = Authorization.fromKeys(
    "sandbox",                        // environment
    "pk_XXXXXXXXXXXXXXXXXXXXXXXXXX"   // publishable_key
  );

  mSeamlesspayFragment = SeamlesspayFragment.newInstance(this, authorization);
```

### API

- **`fromKeys(environment, publishable_key)`**: Creates authorization
  credentials

</p></details>

## PAN Vault

The PAN Vault is a way to store payment instruments for future use
while remaining outside the scope of PCI. Use `PanVault` to
create a token with given payment data.

<details><summary><strong>Example & API</strong></summary><p>

### Example

```java
CardBuilder cardBuilder = new CardBuilder()
  .accountNumber(mCardForm.getCardNumber())
  .expirationMonth(mCardForm.getExpirationMonth())
  .expirationYear(mCardForm.getExpirationYear())
  .setTxnType(CardBuilder.Keys.CREDIT_CARD_TYPE)
  .billingZip(mCardForm.getPostalCode())
  .cvv(mCardForm.getCvv())
  .verification(true);

PanVault.tokenize(mSeamlesspayFragment, cardBuilder);
```

### API

- **`PanVault.tokenize(mSeamlesspayFragment, cardBuilder)`**: Creates a
  reusable token

Available listeners:

- **`PaymentMethodTokenCreatedListener`**: A `PaymentMethodToken` has
  been created

</p></details>

## Charge

Create a `Charge` (payment transaction) using a token from `PanVault`:

<details><summary><strong>Example & API</strong></summary><p>

### Example

```java
CardChargeBulder chargeBulder = new CardChargeBulder()
  .setAmount("1")
  .setCurrency(CardChargeBulder.Keys.CURRENCY_USD)
  .setCapture(true)
  .setToken(token.getToken())
  .setDescription("Demo Android Client Charge")
  .setCvv(mCardForm.getCvv());

Charge.create(mSeamlesspayFragment, chargeBulder);
```

### `Charge` API

- **`Charge.create(mSeamlesspayFragment, chargeBulder)`**: Creates a charge
  using provided data

Available listeners:

- **`BaseChargeTokenCreatedListener`**: A `chargeToken` was successfully created

</p></details>

## Card Scanning

The [CardForm](#card-input-form) UI component is compatible with
[card.io](https://github.com/card-io/card.io-Android-SDK), which
allows users to input card information using their phone's camera.
For more information, please refer to the github repository and
docs for **[`card.io-Android-SDK`](https://github.com/card-io/card.io-Android-SDK)**.

<details><summary><strong>Usage & API</strong></summary><p>

### Usage

To use card.io, add the dependency to your `build.gradle`:

```groovy
dependencies {
  api 'io.card:android-sdk:[5.5.0,6.0.0]'
}
```

### `CardForm` API for card.io

- **`cardForm.isCardScanningAvailable()`**: Is `card.io` is available for use
- **`cardForm.scanCard(activity)`**: Initiates a card scan on device

</p></details>

## Example Apps

The SeamlessPay Android SDK comes bundled with the following demo apps:

- **[`CardForm`](Demo)**: Basic setup and usage of API Client & `CardForm`
- **[`Singlefield`](DemoSinglefield)**: Setup and usage of `Singlefield` UI

Run `./gradlew :Demo:installDebug` to install the [Demo](Demo) app on a device.

### Feedback

The SeamlessPay Android SDK is in active development, we welcome your feedback!

Here are a few ways to get in touch:

- [GitHub Issues](https://github.com/seamlesspay/seamlesspay_android/issues) - For generally applicable issues and feedback
- [SeamlessPay Portal](https://portal.seamlesspay.com/) / [support@seamlesspay.com](mailto:support@seamlesspay.com) -
  for personal support at any phase of integration

### Help

- [Read the docs](https://docs.seamlesspay.com/#tag/SeamlessPayCoreFrameworkAndroid) :books:
- Find a bug? [Open an issue](https://github.com/seamlesspay/seamlesspay_android/issues) :bug:
- Want to contribute? [Check out contributing guidelines](CONTRIBUTING.md) and [submit a pull request](https://help.github.com/articles/creating-a-pull-request).
