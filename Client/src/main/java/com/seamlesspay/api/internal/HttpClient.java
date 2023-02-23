/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.internal;

import static java.net.HttpURLConnection.HTTP_ACCEPTED;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.net.HttpURLConnection.HTTP_UNAVAILABLE;

import android.net.http.X509TrustManagerExtensions;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import com.seamlesspay.api.exceptions.AuthenticationException;
import com.seamlesspay.api.exceptions.AuthorizationException;
import com.seamlesspay.api.exceptions.DownForMaintenanceException;
import com.seamlesspay.api.exceptions.RateLimitException;
import com.seamlesspay.api.exceptions.ServerException;
import com.seamlesspay.api.exceptions.UnexpectedException;
import com.seamlesspay.api.exceptions.UnprocessableEntityException;
import com.seamlesspay.api.exceptions.UpgradeRequiredException;
import com.seamlesspay.api.interfaces.HttpResponseCallback;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class HttpClient<T extends HttpClient> {
  private static final String METHOD_GET = "GET";
  private static final String METHOD_POST = "POST";
  private static final String UTF_8 = "UTF-8";

  //We should use only root Amazon Certificates
  private static final String[] CERTIFICATE_KEY_VALUES = new String[] {
      "++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI=",
      "f0KW/FtqTjs108NpYj42SrGvOB2PpxIVM8nWxjPqJGE=",
      "NqvDJlas/GRcYbcWE8S/IceH9cq77kg0jVhZeAPXq8k=",
      "9+ze1cZgR9KO1kZrVDxA4HQ6voHRCSVNz4RdTCx4U8U=",
      "KwccWaCgrnaw6tsrrSO61FgLacNgG2MMLq8GE6+oP5I="
  };
  private static final Set<String> CERTIFICATE_SET = new HashSet<>(Arrays.asList(CERTIFICATE_KEY_VALUES));

  private final Handler mMainThreadHandler;

  protected final ExecutorService mThreadPool;

  private String mUserAgent;
  private SSLSocketFactory mSSLSocketFactory;
  private int mConnectTimeout;
  private int mReadTimeout;

  protected String mBaseUrl;

  public HttpClient() {
    mThreadPool = Executors.newCachedThreadPool();
    mMainThreadHandler = new Handler(Looper.getMainLooper());
    mUserAgent = "seamlesspay_android";
    mConnectTimeout = (int) TimeUnit.SECONDS.toMillis(30);
    mReadTimeout = (int) TimeUnit.SECONDS.toMillis(30);

    try {
      mSSLSocketFactory = new TLSSocketFactory();
    } catch (SSLException e) {
      mSSLSocketFactory = null;
    }
  }

  /**
   * @param userAgent the user agent to be sent with all http requests.
   * @return {@link HttpClient} for method chaining.
   */
  @SuppressWarnings("unchecked")
  public T setUserAgent(String userAgent) {
    mUserAgent = userAgent;

    return (T) this;
  }

  /**
   * @param sslSocketFactory the {@link SSLSocketFactory} to use for
   * all https requests.
   * @return {@link HttpClient} for method chaining.
   */
  @SuppressWarnings("unchecked")
  public T setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
    mSSLSocketFactory = sslSocketFactory;

    return (T) this;
  }

  /**
   * @param baseUrl the base url to use when only a path is supplied
   * to {@link #get(String, HttpResponseCallback)}
   * or {@link #post(String, String, HttpResponseCallback)}
   * @return {@link HttpClient} for method chaining.
   */
  @SuppressWarnings("unchecked")
  public T setBaseUrl(String baseUrl) {
    mBaseUrl = (baseUrl == null) ? "" : baseUrl;

    return (T) this;
  }

  /**
   * @param timeout the time in milliseconds to wait for a
   * connection before timing out.
   * @return {@link HttpClient} for method chaining.
   */
  @SuppressWarnings("unchecked")
  public T setConnectTimeout(int timeout) {
    mConnectTimeout = timeout;

    return (T) this;
  }

  /**
   * @param timeout the time in milliseconds to read a response
   * rom the server before timing out.
   * @return {@link HttpClient} for method chaining.
   */
  @SuppressWarnings("unchecked")
  public T setReadTimeout(int timeout) {
    mReadTimeout = timeout;

    return (T) this;
  }

  /**
   * Make a HTTP GET request to using the base url and path provided.
   * If the path is a full url, it will be used instead of the
   * previously provided base url.
   *
   * @param path The path or url to request from the server via GET
   * @param callback The {@link HttpResponseCallback} to receive
   * the response or error.
   */
  public void get(final String path, final HttpResponseCallback callback) {
    if (path == null) {
      postCallbackOnMainThread(
        callback,
        new IllegalArgumentException("Path cannot be null")
      );
      return;
    }

    final String url;
    if (path.startsWith("http")) {
      url = path;
    } else {
      url = mBaseUrl + path;
    }

    mThreadPool.submit(
      new Runnable() {

        @Override
        public void run() {
          HttpURLConnection connection = null;

          try {
            connection = init(url);
            connection.setRequestMethod(METHOD_GET);

            postCallbackOnMainThread(callback, parseResponse(connection));
          } catch (Exception e) {
            postCallbackOnMainThread(callback, e);
          } finally {
            if (connection != null) {
              connection.disconnect();
            }
          }
        }
      }
    );
  }

  /**
   * Make a HTTP POST request using the base url and path provided.
   * If the path is a full url, it will be used instead of the
   * previously provided url.
   *
   * @param path The URL to request from the server via HTTP POST
   * @param data The body of the POST request
   * @param callback The {@link HttpResponseCallback} handler
   */
  public void post(
    final String path,
    final String data,
    final HttpResponseCallback callback
  ) {

    if (path == null) {
      postCallbackOnMainThread(
        callback,
        new IllegalArgumentException("Path cannot be null")
      );

      return;
    }

    mThreadPool.submit(
      new Runnable() {

        @Override
        public void run() {
          try {
            postCallbackOnMainThread(callback, post(path, data));
          } catch (Exception e) {
            postCallbackOnMainThread(callback, e);
          }
        }
      }
    );
  }

  /**
   * Performs a synchronous post request.
   *
   * @param path the path or url to request from the server via HTTP POST
   * @param data the body of the post request
   * @return The HTTP body the of the response
   *
   * @see HttpClient#post(String, String, HttpResponseCallback)
   * @throws Exception
   */
  public String post(String path, String data) throws Exception {
    HttpURLConnection connection = null;

    try {
      if (path.startsWith("http")) {
        connection = init(path);
      } else {
        connection = init(mBaseUrl + path);
      }

      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestMethod(METHOD_POST);
      connection.setDoOutput(true);

      if (mBaseUrl.indexOf("sbx") != -1 || mBaseUrl.indexOf("dev") != -1) {
        String headers = "";
        Map<String, List<String>> headerFields = connection.getRequestProperties();
        Set<String> keys = headerFields.keySet();
        for (String key : keys) {
          String val = connection.getRequestProperty(key);
          headers += key + "    " + val + "\n";
        }
        Log.i("HTTP request headers:\n", headers);
        Log.i("HTTP POST request:\n", "Path: " + mBaseUrl + path + "\n" + "Data: " + data);
      }

      writeOutputStream(connection.getOutputStream(), data);

      return parseResponse(connection);
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  protected HttpURLConnection init(String url) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url)
    .openConnection();

    if (connection instanceof HttpsURLConnection) {
      if (mSSLSocketFactory == null) {
        throw new SSLException(
          "SSLSocketFactory was not set or failed to initialize"
        );
      }

      ((HttpsURLConnection) connection).setSSLSocketFactory(mSSLSocketFactory);
    }

    connection.setRequestProperty("User-Agent", mUserAgent);
    connection.setRequestProperty(
      "Accept-Language",
      Locale.getDefault().getLanguage()
    );
    connection.setRequestProperty("Accept-Encoding", "gzip");
    connection.setConnectTimeout(mConnectTimeout);
    connection.setReadTimeout(mReadTimeout);

    return connection;
  }

  protected void writeOutputStream(OutputStream outputStream, String data)
    throws IOException {
    Writer out = new OutputStreamWriter(outputStream, UTF_8);

    out.write(data, 0, data.length());
    out.flush();
    out.close();
  }

  protected String parseResponse(HttpURLConnection connection)
    throws Exception {
    if (connection instanceof HttpsURLConnection) {
      validatePinning((HttpsURLConnection)connection);
    }
    int responseCode = connection.getResponseCode();

    boolean gzip = "gzip".equals(connection.getContentEncoding());

    switch (responseCode) {
      case HTTP_OK:
      case HTTP_CREATED:
      case HTTP_ACCEPTED:
        return readStream(connection.getInputStream(), gzip);
      case HTTP_UNAUTHORIZED:
        throw new AuthenticationException(
          readStream(connection.getErrorStream(), gzip)
        );
      case HTTP_FORBIDDEN:
        throw new AuthorizationException(
          readStream(connection.getErrorStream(), gzip)
        );
      case HTTP_BAD_REQUEST:
      case 422: // HTTP_UNPROCESSABLE_ENTITY
        throw new UnprocessableEntityException(
          readStream(connection.getErrorStream(), gzip)
        );
      case 426: // HTTP_UPGRADE_REQUIRED
        throw new UpgradeRequiredException(
          readStream(connection.getErrorStream(), gzip)
        );
      case 429: // HTTP_TOO_MANY_REQUESTS
        throw new RateLimitException(
          "You are being rate-limited. Please try again in a few minutes."
        );
      case HTTP_INTERNAL_ERROR:
        throw new ServerException(
          readStream(connection.getErrorStream(), gzip)
        );
      case HTTP_UNAVAILABLE:
        throw new DownForMaintenanceException(
          readStream(connection.getErrorStream(), gzip)
        );
      default:
        throw new UnexpectedException(
          readStream(connection.getErrorStream(), gzip)
        );
    }
  }

  void postCallbackOnMainThread(
    final HttpResponseCallback callback,
    final String response
  ) {
    if (callback == null) {
      return;
    }

    mMainThreadHandler.post(
      new Runnable() {

        @Override
        public void run() {
          callback.success(response);
        }
      }
    );
  }

  void postCallbackOnMainThread(
    final HttpResponseCallback callback,
    final Exception exception
  ) {
    if (callback == null) {
      return;
    }

    mMainThreadHandler.post(
      new Runnable() {

        @Override
        public void run() {
          callback.failure(exception);
        }
      }
    );
  }

  private String readStream(InputStream in, boolean gzip) throws IOException {
    if (in == null) {
      return null;
    }

    try {
      if (gzip) {
        in = new GZIPInputStream(in);
      }

      ByteArrayOutputStream out = new ByteArrayOutputStream();

      byte[] buffer = new byte[1024];

      for (int count; (count = in.read(buffer)) != -1;) {
        out.write(buffer, 0, count);
      }

      return new String(out.toByteArray(), UTF_8);
    } finally {
      try {
        in.close();
      } catch (IOException ignored) {}
    }
  }

  private void validatePinning(HttpsURLConnection conn) throws SSLException {
    StringBuilder certChainMsg = new StringBuilder();
    try {
      X509TrustManagerExtensions trustManagerExt = getTrustManager();
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      List<X509Certificate> trustedChain = trustedChain(trustManagerExt, conn);
      for (X509Certificate cert : trustedChain) {
        byte[] publicKey = cert.getPublicKey().getEncoded();
        md.update(publicKey, 0, publicKey.length);
        String pin = Base64.encodeToString(md.digest(), Base64.NO_WRAP);
        certChainMsg.append("    sha256/")
                    .append(pin)
                    .append(" : ")
                    .append(cert.getSubjectDN().toString())
                    .append("\n");
        if (HttpClient.CERTIFICATE_SET.contains(pin)) {
          return;
        }
      }
    } catch (NoSuchAlgorithmException | KeyStoreException e) {
      throw new SSLException(e);
    } throw new SSLPeerUnverifiedException("Certificate pinning " +
        "failure\n  Peer certificate chain:\n" + certChainMsg);
  }

  private List<X509Certificate> trustedChain(
      X509TrustManagerExtensions trustManagerExt,
      HttpsURLConnection conn) throws SSLException {
    Certificate[] serverCerts = conn.getServerCertificates();
    X509Certificate[] untrustedCerts = Arrays.copyOf(serverCerts, serverCerts.length, X509Certificate[].class);
    String host = conn.getURL().getHost();
    try {
      return trustManagerExt.checkServerTrusted(untrustedCerts, "RSA", host);
    } catch (CertificateException e) {
      throw new SSLException(e);
    }
  }

  private X509TrustManagerExtensions getTrustManager()
      throws NoSuchAlgorithmException, KeyStoreException {
      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init((KeyStore) null);
      // Find first X509TrustManager in the TrustManagerFactory
      X509TrustManager x509TrustManager = null;
      for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
        if (trustManager instanceof X509TrustManager) {
          x509TrustManager = (X509TrustManager) trustManager;
          break;
        }
      }
      return new X509TrustManagerExtensions(x509TrustManager);
  }
}
