# SeamlessPay Android SDK

Welcome to SeamlessPay Android SDK. This library will help you accept card and alternative payments in your Android app.

SeamlessPay Android SDK is available for Android SDK >= 21.

## Adding It To Your Project

Add the dependency in your `build.gradle`:

```groovy
dependencies {
  implementation 'com.seamlesspay.api:Client:1.0.2'
}
```

# Authentication

```java
import com.seamlesspay.api.SeamlesspayFragment;
import com.seamlesspay.api.Authorization;

public class CardActivity ...

Authorization authorization = Authorization.fromKeys(
                    "sandbox",
                    "pk_XXXXXXXXXXXXXXXXXXXXXXXXXX");

mSeamlesspayFragment = SeamlesspayFragment.newInstance(this, authorization);
```

We provide native screens and elements to collect payment

# Single Field

Singlefield widget drop-in UI component provided by the SDK.

## Adding It To Your Project

Add the dependency in your `build.gradle`:

```groovy
dependencies {
    implementation 'com.seamlesspay.ui:Singlefield:1.0.1'
}
```

## Usage

Create an instance of the card component and a Pay button by adding the following to your checkout page’s layout:

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
        tools:context=".CheckoutActivity">

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

## To access the values in the form, there are getters for each field:

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
                        // Code here executes on main thread after user presses button
                        mCardInputWidget.clearFocus();

                        CardBuilder cardBuilder = new CardBuilder()
                                .accountNumber(mCardInputWidget.getCardNumber())
                                .expirationMonth(mCardInputWidget.getExpirationMonth())
                                .expirationYear(mCardInputWidget.getExpirationYear())
                                .setTxnType(CardBuilder.Keys.CREDIT_CARD_TYPE)
                                .billingZip(mCardInputWidget.getPostalCode())
                                .verification(true);

                        PanVault.tokenize(mSeamlesspayFragment, cardBuilder);
                    }
                });
```

## Example
![](/files/singlefield.gif)

Start with [**'Demo APP'**](https://github.com/seamlesspay/seamlesspay-android/tree/master/DemoSingleField) for sample on basic setup and usage.


# Card Form

Card Form is a ready made card form layout that can be included in your app making it easy to
accept credit and debit cards.

## Adding It To Your Project

Add the dependency in your `build.gradle`:

```groovy
dependencies {
    implementation 'com.seamlesspay.sdk:CardForm:1.0.1'
}
```

## Usage

Card Form is a LinearLayout that you can add to your layout:

```xml
<com.seamlesspay.cardform.view.CardForm
    android:id="@+id/card_form"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

To initialize the view and change which fields are required for the user to enter, use the required
field methods and `CardForm#setup(AppCompatActivity activity)`.

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

To check if `CardForm` is valid call `CardForm#isValid()`. To validate each required field
and show the user which fields are incorrect, call `CardForm#validate()`.

To set custom error messages on a field call `CardForm#setCardNumberError(String)` on the given field.

Additionally `CardForm` has 4 available listeners:

* `CardForm#setOnCardFormValidListener` called when the form changes state from valid to invalid or invalid to valid.
* `CardForm#setOnCardFormSubmitListener` called when the form should be submitted.
* `CardForm#setOnFormFieldFocusedListener` called when a field in the form is focused.
* `CardForm#setOnCardTypeChangedListener` called when the `CardType` in the form changes.

# Create Token

Create the token of given payment data.
To access the values in the form, there are getters for each field

```java
 CardBuilder cardBuilder = new CardBuilder()
                    .accountNumber(mCardForm.getCardNumber())
                    .expirationMonth(mCardForm.getExpirationMonth())
                    .expirationYear(mCardForm.getExpirationYear())
                    .setTxnType(CardBuilder.Keys.CREDIT_CARD_TYPE)
                    .billingZip(mCardForm.getPostalCode())
                    .verification(true);

            PanVault.tokenize(mSeamlesspayFragment, cardBuilder);
```
Available listener:
* `PaymentMethodTokenCreatedListener` called when the `PaymentMethodToken` getting card token.

# Create a Charge

```java
CardChargeBulder chargeBulder = new CardChargeBulder()
                .setAmount("1")
                .setCurrency(CardChargeBulder.Keys.CURRENCY_USD)
                .setCapture(true)
                .setToken(token.getToken())
                .setDescription("Demo Android Client Charge")
                .setCvv(mCardForm.getCVV());

        Charge.create(mSeamlesspayFragment, chargeBulder);
```
Available listener:
* `BaseChargeTokenCreatedListener` called when the `chargeToken` getting charge info.

## Example
![](/files/cardform.png)

Start with [**'Demo APP'**](https://github.com/seamlesspay/seamlesspay-android/tree/master/Demo) for sample on basic setup and usage.

## card.io

The card form is compatible with [card.io](https://github.com/card-io/card.io-Android-SDK).

To use card.io, add the dependency in your `build.gradle`:

```groovy
dependencies {
    api 'io.card:android-sdk:[5.5.0,6.0.0)'
}
```

Check if card.io is available for use:

```java
cardForm.isCardScanningAvailable();
```

Scan a card:

```java
cardForm.scanCard(activity);
```