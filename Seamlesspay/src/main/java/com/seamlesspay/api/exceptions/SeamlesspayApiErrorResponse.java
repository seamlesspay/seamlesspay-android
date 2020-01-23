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

/**
 *  Error container returned when Seamlesspay API returns a 400 Bad Request.
 *  A 400 occurs when a request is malformed or invalid (e.g. unexpected fields, invalid JSON, invalid version,
 *  invalid field value). Examine the errors details and either present the error(s) to the end user or review the
 *  request and configuration.
 *
 *  {@link SeamlesspayApiErrorResponse} parses the server's error response and exposes the errors.
 */
public class SeamlesspayApiErrorResponse extends Exception implements Parcelable {

    private String mMessage;
    private String mCode;
    private String mOriginalResponse;
    private List<String> mErrors = new ArrayList<>();

    public SeamlesspayApiErrorResponse(String jsonString) {
        mOriginalResponse = jsonString;

        try {
            JSONObject json = new JSONObject(jsonString);
            mMessage = Json.optString(json, "message", "No message was returned");
            mCode = Json.optString(json, "code", "-1");
            try {
                JSONArray errors = json.getJSONArray("errors");
                for (int i = 0; i < json.length(); i++) {
                    mErrors.add(errors.get(i).toString());
                }
            } catch (JSONException ignored) {}

        } catch (JSONException e) {
            mMessage = "Parsing error response failed";
        }
    }


    public String getCode() {
        return mCode;
    }

    /**
     * @return Human readable top level summary of the error.
     */
    @Override
    public String getMessage() {
        return mMessage;
    }

    /**
     * @return The full error response as a {@link String}.
     */
    public String getErrorResponse() {
        return mOriginalResponse;
    }

    /**
     * @return All the specific field errors.
     */
    @Nullable
    public List<String> getErrors() {
        return mErrors;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMessage);
        dest.writeString(mCode);
        dest.writeString(mOriginalResponse);
        dest.writeStringList(mErrors);
    }

    protected SeamlesspayApiErrorResponse(Parcel in) {
        mMessage = in.readString();
        mCode = in.readString();
        mOriginalResponse = in.readString();
        in.readStringList(mErrors);
    }

    public static final Creator<SeamlesspayApiErrorResponse> CREATOR = new Creator<SeamlesspayApiErrorResponse>() {
        @Override
        public SeamlesspayApiErrorResponse createFromParcel(Parcel source) {
            return new SeamlesspayApiErrorResponse(source);
        }

        @Override
        public SeamlesspayApiErrorResponse[] newArray(int size) {
            return new SeamlesspayApiErrorResponse[size];
        }
    };
}
