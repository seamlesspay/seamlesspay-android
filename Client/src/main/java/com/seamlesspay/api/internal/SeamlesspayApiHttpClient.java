/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.internal;

import android.text.TextUtils;
import com.seamlesspay.api.exceptions.SeamlesspayApiErrorResponse;
import com.seamlesspay.api.exceptions.UnprocessableEntityException;
import com.seamlesspay.api.models.Configuration;

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

    setUserAgent(Configuration.getApiUserAgent());
    setSSLSocketFactory(new SeamlesspaySSLSocketFactory());
  }

  @Override
  protected HttpURLConnection init(String url) throws IOException {
    HttpURLConnection connection = super.init(url);

    if (!TextUtils.isEmpty(mBearer)) {
      connection.setRequestProperty("Authorization", "Bearer " + mBearer);
    }

    connection.setRequestProperty("API-Version", Configuration.getApiVersion());

    return connection;
  }

  @Override
  protected String parseResponse(HttpURLConnection connection)
    throws Exception {
    try {
      return super.parseResponse(connection);
    } catch (UnprocessableEntityException e) {
      throw new SeamlesspayApiErrorResponse(e.getMessage());
    }
  }
}
