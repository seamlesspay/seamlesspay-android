package com.seamlesspay.api.internal;

import android.text.TextUtils;

import com.seamlesspay.api.BuildConfig;
import com.seamlesspay.api.exceptions.SeamlesspayApiErrorResponse;
import com.seamlesspay.api.exceptions.UnprocessableEntityException;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Network request class that handles SeamlessPay request specifics and threading.
 */
public class SeamlesspayApiHttpClient extends HttpClient {

    private final String mBearer;

    public SeamlesspayApiHttpClient(String baseUrl, String bearer) {
        super();

        mBaseUrl = baseUrl;
        mBearer = bearer;

        setUserAgent("seamlesspay-android:" + BuildConfig.VERSION_NAME);
        setSSLSocketFactory( new SeamlesspaySSLSocketFactory());
    }

    @Override
    protected HttpURLConnection init(String url) throws IOException {
        HttpURLConnection connection = super.init(url);

        if (!TextUtils.isEmpty(mBearer)) {
            connection.setRequestProperty("Authorization", "Bearer " + mBearer);
        }
        return connection;
    }

    @Override
    protected String parseResponse(HttpURLConnection connection) throws Exception {
        try {
            return super.parseResponse(connection);
        } catch (UnprocessableEntityException e) {
            throw new SeamlesspayApiErrorResponse(e.getMessage());
        }
    }
}