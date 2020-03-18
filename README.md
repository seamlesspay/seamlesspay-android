# SeamlessPay Android SDK

The SeamlessPay Android SDK makes it easy to accept card and alternative payments in your Android app. Our Android SDK is available for Android SDK >= 21.

| Package                                                                 | Description                                                  | Release                                                                    |
| ----------------------------------------------------------------------- | ------------------------------------------------------------ | -------------------------------------------------------------------------- |
| [**`api`**](https://bintray.com/seamless-ops/maven/api)                 | Seamless Payments API Client for Android.                    | ![Bintray](https://img.shields.io/bintray/v/seamlesspay/maven/api)         |
| [**`card-form`**](https://bintray.com/seamless-ops/maven/card-form)     | UI component for card input using a traditional form layout. | ![Bintray](https://img.shields.io/bintray/v/seamlesspay/maven/card-form)   |
| [**`singlefield`**](https://bintray.com/seamless-ops/maven/singlefield) | UI component for card input using a single-field widget.     | ![Bintray](https://img.shields.io/bintray/v/seamlesspay/maven/singlefield) |

### Installation

Add the required dependencies to your project using your preferred repository:

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

_Note: Be sure to replace **`[VERSION]`** with the correct semantic version of package._

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

_Note: Be sure to replace **`[VERSION]`** with the correct semantic version of package._

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

_Note: Be sure to replace **`[VERSION]`** with the correct semantic version of package._

</p></details><br>

In most cases, you will only need to install the **`api`** and one of the UI packages, either `card-form` **or** `singlefield`.

# Usage

## Drop-In UI Components

We provide several drop-in native UI components for easily collecting payment information in your Android Application:

<details><summary><strong>Card Input Form Usage</strong></summary><p>

<img style="float: right; margin: 1rem; width: 30%;" src="/files/cardform.png"/>

### Card Input Form

The Card Input Form is a drop-in UI layout that can be included in your app making it easy to accept credit and debit cards.

#### Installation

Add the dependency to your `build.gradle`:

```groovy
dependencies {
  implementation 'com.seamlesspay.sdk:CardForm:[VERSION]'
}
```

#### Usage

CardForm is a LinearLayout that you can add to your layout:

```xml
<com.seamlesspay.cardform.view.CardForm
  android:id="@+id/card_form"
  android:layout_width="match_parent"
  android:layout_height="match_parent" />
```

To initialize the view and change which fields are required for the user to enter, use the required field methods and `CardForm#setup(AppCompatActivity activity)`:

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

- **`setOnCardFormValidListener`**: CardForm has changed state from valid to invalid or invalid to valid
- **`setOnCardFormSubmitListener`**: Called when CardForm should be submitted
- **`setOnFormFieldFocusedListener`**: A field in the form was focused
- **`setOnCardTypeChangedListener`**: The `CardType` has changed

#### Demo

Start with the provided [**`Demo`**](https://github.com/seamlesspay/seamlesspay-android/tree/master/Demo) App for basic setup and usage.

</p></details>

<details><summary><strong>Single Field Input Usage</strong></summary><p>

<img style="float: right; margin: 1rem; width: 30%;" src="/files/singlefield.gif"/>

### Single Field Input

The Card Input Form is a drop-in UI layout that can be included in your app making it easy to accept credit and debit cards.

#### Installation

Add the dependency to your `build.gradle`:

```groovy
dependencies {
  implementation 'com.seamlesspay.ui:Singlefield:[VERSION]'
}
```

#### Usage

The Single Field Input is a drop-in UI widget that can be included in your app making it easy to accept credit and debit cards.

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

#### Example

Start with the provided demo [**`DemoSinglefield`**](https://github.com/seamlesspay/seamlesspay-android/tree/master/DemoSingleField) App for a working example with basic setup and usage.

</p></details>

## Authorization

To authenticate requests, use **`Authorization#fromKeys()`** with your `environment` and `publishable_key` to generate credentials.

<details><summary><strong>Example & API</strong></summary><p>

##### Example

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

##### API

- **`fromKeys`**: Creates credentials using `environment` and `publishable_key`

</p></details>

## PAN Vault

The PAN Vault is a way to store payment instruments for future use while remaining outside the scope of PCI. Use `PanVault` to create a token with given payment data.

<details><summary><strong>Example & API</strong></summary><p>

##### Example

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

##### API

- **`tokenize`**: Creates a reusable token

Available listeners:

- **`PaymentMethodTokenCreatedListener`**: The `PaymentMethodToken` has been created

</p></details>

## Charge

Create a `Charge` (payment transaction) using a token from `PanVault`:

<details><summary><strong>Example & API</strong></summary><p>

##### Example

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

##### `Charge` API

- **`create`**: Creates a charge using provided data

Available listeners:

- **`BaseChargeTokenCreatedListener`**: The `chargeToken` was successfully created

</p></details>

## Card Scanning

The [CardForm](#card-input-form) UI component is compatible with [card.io](https://github.com/card-io/card.io-Android-SDK), which allows users to input card information using their phone's camera. For more information, please refer to the github repository and docs for **[`card.io-Android-SDK`](https://github.com/card-io/card.io-Android-SDK)**.

<details><summary><strong>Usage & API</strong></summary><p>

##### Usage

To use card.io, add the dependency to your `build.gradle`:

```groovy
dependencies {
  api 'io.card:android-sdk:[5.5.0,6.0.0]'
}
```

##### `CardForm` API for card.io

- **`cardForm.isCardScanningAvailable()`**: Checks if `card.io` is available for use
- **`cardForm.scanCard(activity)`**: Initiates a card scan on device

</p></details>
