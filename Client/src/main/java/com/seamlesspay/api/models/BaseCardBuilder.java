package com.seamlesspay.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Base builder class used to build various types of cards
 */
public abstract class BaseCardBuilder<T> extends PaymentMethodBuilder<T> implements Parcelable {

    static final String NUMBER_KEY = "accountNumber";
    static final String COMPANY_KEY = "company";
    static final String BILLING_ADDRESS_KEY = "billingAddress";
    static final String BILLING_ADDRESS2_KEY = "billingAddress2";
    static final String BILLING_ZIP_KEY = "billingZip";
    static final String BILLING_CITY_KEY = "billingCity";
    static final String BILLING_STATE_KEY = "billingState";
    static final String BILLING_COUNTRY_KEY = "billingCountry";
    static final String EMAIL_KEY = "email";
    static final String PHONE_KEY = "phoneNumber";
    static final String EXPIRATION_DATE_KEY = "expDate";
    static final String CARDHOLDER_NAME_KEY = "name";


    String mCardType;
    String mAccountNumber;
    String mExpirationMonth;
    String mExpirationYear;
    String mFirstName;
    String mLastName;
    String mCardholderName;
    String mEmail;
    String mPhoneNumber;
    String mCompany;
    String mBillingCity;
    String mBillingZip;
    String mBillingState;
    String mBillingCountry;
    String mBillingAddress;
    String mBillingAddress2;

    public BaseCardBuilder() {}


    /**
     * @param number The card number.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T accountNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            mAccountNumber = null;
        } else {
            mAccountNumber = number;
        }
        return (T) this;
    }

    /**
     * @param expirationMonth The expiration month of the card.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T expirationMonth(String expirationMonth) {
        if (TextUtils.isEmpty(expirationMonth)) {
            mExpirationMonth = null;
        } else {
            mExpirationMonth = expirationMonth;
        }
        return (T) this;
    }

    /**
     * @param expirationYear The expiration year of the card.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T expirationYear(String expirationYear) {
        if (TextUtils.isEmpty(expirationYear)) {
            mExpirationYear = null;
        } else {
            mExpirationYear = expirationYear;
        }
        return (T) this;
    }

    /**
     * @param expirationDate The expiration date of the card. May be in the form MM/YY or MM/YYYY.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T expirationDate(String expirationDate) {
        if (TextUtils.isEmpty(expirationDate)) {
            mExpirationMonth = null;
            mExpirationYear = null;
        } else {
            String[] splitExpiration = expirationDate.split("/");

            mExpirationMonth = splitExpiration[0];

            if (splitExpiration.length > 1) {
                mExpirationYear = splitExpiration[1];
            }
        }
        return (T) this;
    }

    /**
     * @param cardholderName Name on the card.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T cardholderName(String cardholderName) {
        if (TextUtils.isEmpty(cardholderName)) {
            mCardholderName = null;
        } else {
            mCardholderName = cardholderName;
        }
        return (T) this;
    }

    /**
     * @param firstName First name on the card.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T firstName(String firstName) {
        if (TextUtils.isEmpty(firstName)) {
            mFirstName = null;
        } else {
            mFirstName = firstName;
        }
        return (T) this;
    }

    /**
     * @param lastName Last name on the card.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T lastName(String lastName) {
        if (TextUtils.isEmpty(lastName)) {
            mLastName = null;
        } else {
            mLastName = lastName;
        }
        return (T) this;
    }

    /**
     * @param company Company associated with the card.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T company(String company) {
        if (TextUtils.isEmpty(company)) {
            mCompany = null;
        } else {
            mCompany = company;
        }
        return (T) this;
    }

    /**
     * @param countryCode The ISO 3166-1 alpha-3 country code specified in the card's billing address.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T billingCountry(String countryCode) {
        if (TextUtils.isEmpty(countryCode)) {
            mBillingCountry = null;
        } else {
            mBillingCountry = countryCode;
        }
        return (T) this;
    }

    /**
     * @param city of the card.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T billingCity(String city) {
        if (TextUtils.isEmpty(city)) {
            mBillingCity = null;
        } else {
            mBillingCity = city;
        }
        return (T) this;
    }

    /**
     * @param zipCode Postal code of the card.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T billingZip(String zipCode) {
        if (TextUtils.isEmpty(zipCode)) {
            mBillingZip = null;
        } else {
            mBillingZip = zipCode;
        }
        return (T) this;
    }

    /**
     * @param state Region of the card.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T billingState(String state) {
        if (TextUtils.isEmpty(state)) {
            mBillingState = null;
        } else {
            mBillingState = state;
        }
        return (T) this;
    }

    /**
     * @param streetAddress Street address of the card.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T billingAddress(String streetAddress) {
        if (TextUtils.isEmpty(streetAddress)) {
            mBillingAddress = null;
        } else {
            mBillingAddress = streetAddress;
        }
        return (T) this;
    }

    /**
     * @param extendedAddress  address of the card.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T extendedBillingAddress(String extendedAddress) {
        if (TextUtils.isEmpty(extendedAddress)) {
            mBillingAddress2 = null;
        } else {
            mBillingAddress2 = extendedAddress;
        }
        return (T) this;
    }

    /**
     * @param email  address of the card.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T email(String email) {
        if (TextUtils.isEmpty(email)) {
            mEmail = null;
        } else {
            mEmail = email;
        }
        return (T) this;
    }

    /**
     * @param phoneNumber  phone of the card.
     * @return {@link BaseCardBuilder}
     */
    @SuppressWarnings("unchecked")
    public T phoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumber = null;
        } else {
            mPhoneNumber = phoneNumber;
        }
        return (T) this;
    }

    @Override
    protected void build(JSONObject json) throws JSONException {
        json.put(NUMBER_KEY, mAccountNumber);

        if (mExpirationMonth != null && mExpirationYear != null) {
            json.put(EXPIRATION_DATE_KEY, mExpirationMonth + "/" + mExpirationYear.substring(mExpirationYear.length() - 2, mExpirationYear.length()));
        }
        if (mCardholderName != null) {
            json.put(CARDHOLDER_NAME_KEY, mCardholderName);
        } else if (mFirstName != null && mLastName != null) {
            json.put(CARDHOLDER_NAME_KEY, mFirstName + " " + mLastName);
        }

        json.put(COMPANY_KEY, mCompany);
        json.put(BILLING_CITY_KEY, mBillingCity);
        json.put(BILLING_ZIP_KEY, mBillingZip);
        json.put(BILLING_STATE_KEY, mBillingState);
        json.put(BILLING_COUNTRY_KEY, mBillingCountry);
        json.put(BILLING_ADDRESS_KEY, mBillingAddress);
        json.put(BILLING_ADDRESS2_KEY, mBillingAddress2);
        json.put(EMAIL_KEY, mEmail);
        json.put(PHONE_KEY, mPhoneNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected BaseCardBuilder(Parcel in) {
        super(in);
        mCardType = in.readString();
        mAccountNumber = in.readString();
        mExpirationMonth = in.readString();
        mExpirationYear = in.readString();
        mCardholderName = in.readString();
        mFirstName = in.readString();
        mLastName = in.readString();
        mCompany = in.readString();
        mBillingCountry = in.readString();
        mBillingCity = in.readString();
        mBillingZip = in.readString();
        mBillingState = in.readString();
        mBillingAddress = in.readString();
        mBillingAddress2 = in.readString();
        mEmail = in.readString();
        mPhoneNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mCardType);
        dest.writeString(mAccountNumber);
        dest.writeString(mExpirationMonth);
        dest.writeString(mExpirationYear);
        dest.writeString(mCardholderName);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mCompany);
        dest.writeString(mBillingCountry);
        dest.writeString(mBillingCity);
        dest.writeString(mBillingZip);
        dest.writeString(mBillingState);
        dest.writeString(mBillingAddress);
        dest.writeString(mBillingAddress2);
        dest.writeString(mEmail);
        dest.writeString(mPhoneNumber);
    }
}
