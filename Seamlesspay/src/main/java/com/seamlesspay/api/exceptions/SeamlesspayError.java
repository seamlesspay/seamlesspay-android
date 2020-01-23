package com.seamlesspay.api.exceptions;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.seamlesspay.api.Json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SeamlesspayError implements Parcelable {

    private static final String FIELD_KEY = "field";
    private static final String MESSAGE_KEY = "message";
    private static final String FIELD_ERRORS_KEY = "fieldErrors";

    private String mField;
    private String mMessage;
    private List<SeamlesspayError> mFieldErrors;

    public static List<SeamlesspayError> fromJsonArray(JSONArray json) {
        if (json == null) {
            json = new JSONArray();
        }

        List<SeamlesspayError> errors = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            try {
                errors.add(SeamlesspayError.fromJson(json.getJSONObject(i)));
            } catch (JSONException ignored) {}
        }

        return errors;
    }

    public static SeamlesspayError fromJson(JSONObject json) {
        SeamlesspayError error = new SeamlesspayError();
        error.mField = Json.optString(json, FIELD_KEY, null);
        error.mMessage = Json.optString(json, MESSAGE_KEY, null);
        error.mFieldErrors = SeamlesspayError.fromJsonArray(json.optJSONArray(FIELD_ERRORS_KEY));

        return error;
    }

    private static void addGraphQLFieldError(List<String> inputPath, JSONObject errorJSON, List<SeamlesspayError> errors) throws JSONException {
        String field = inputPath.get(0);

        if (inputPath.size() == 1) {
            SeamlesspayError error = new SeamlesspayError();
            error.mField = field;
            error.mMessage = errorJSON.getString("message");
            error.mFieldErrors = new ArrayList<>();

            errors.add(error);
            return;
        }

        SeamlesspayError nestedError = null;
        List<String> nestedInputPath = inputPath.subList(1, inputPath.size());

        for (SeamlesspayError error : errors) {
            if (error.mField.equals(field)) {
                nestedError = error;
            }
        }

        if (nestedError == null) {
            nestedError = new SeamlesspayError();
            nestedError.mField = field;
            nestedError.mFieldErrors = new ArrayList<>();

            errors.add(nestedError);
        }

        addGraphQLFieldError(nestedInputPath, errorJSON, nestedError.mFieldErrors);
    }

    /**
     * @return Human readable summary of the error for field. May be {@code null}.
     */
    @Nullable
    public String getMessage() {
        return mMessage;
    }

    /**
     * @return Field name this object represents.
     */
    public String getField() {
        return mField;
    }

    /**
     * @return {@link SeamlesspayError} objects for any errors nested under this field.
     */
    public List<SeamlesspayError> getFieldErrors() {
        return mFieldErrors;
    }

    /**
     * Method to extract an error for an individual field, e.g. creditCard, customer, etc.
     *
     * @param field Name of the field desired, expected to be in camelCase.
     * @return {@link SeamlesspayError} for the field searched, or {@code null} if not found.
     */
    @Nullable
    public SeamlesspayError errorFor(String field) {
        SeamlesspayError returnError;
        if (mFieldErrors != null) {
            for (SeamlesspayError error : mFieldErrors) {
                if (error.getField().equals(field)) {
                    return error;
                } else if (error.getFieldErrors() != null) {
                    returnError = error.errorFor(field);
                    if (returnError != null) {
                        return returnError;
                    }
                }
            }
        }
        return null;
    }

    public String toString() {
        return "SeamlesspayError for " + mField + ": " + mMessage + " -> " +
                (mFieldErrors != null ? mFieldErrors.toString() : "");
    }

    public SeamlesspayError() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mField);
        dest.writeString(mMessage);
        dest.writeTypedList(mFieldErrors);
    }

    protected SeamlesspayError(Parcel in) {
        mField = in.readString();
        mMessage = in.readString();
        mFieldErrors = in.createTypedArrayList(SeamlesspayError.CREATOR);
    }

    public static final Creator<SeamlesspayError> CREATOR = new Creator<SeamlesspayError>() {
        public SeamlesspayError createFromParcel(Parcel source) {
            return new SeamlesspayError(source);
        }

        public SeamlesspayError[] newArray(int size) {
            return new SeamlesspayError[size];
        }
    };
}

