/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Wraps a SSLSocketFactory and enables more TLS versions on older versions of Android.
 */
public class SeamlesspaySSLSocketFactory extends SSLSocketFactory {
  private final boolean tlsv11Supported;
  private final boolean tlsv12Supported;

  private final SSLSocketFactory under;

  private static final String TLSv11Proto = "TLSv1.1";
  private static final String TLSv12Proto = "TLSv1.2";

  /**
   * Constructor for a socket factory instance.
   */
  public SeamlesspaySSLSocketFactory() {
    this.under = HttpsURLConnection.getDefaultSSLSocketFactory();

    // For Android prior to 4.1, TLSv1.1 and TLSv1.2 might not be supported
    boolean tlsv11Supported = false;
    boolean tlsv12Supported = false;

    String[] supportedProtocols;

    try {
      supportedProtocols =
        SSLContext.getDefault().getSupportedSSLParameters().getProtocols();
    } catch (NoSuchAlgorithmException e) {
      supportedProtocols = new String[0];
    }

    for (String proto : supportedProtocols) {
      if (proto.equals(TLSv11Proto)) {
        tlsv11Supported = true;
      } else if (proto.equals(TLSv12Proto)) {
        tlsv12Supported = true;
      }
    }

    this.tlsv11Supported = tlsv11Supported;
    this.tlsv12Supported = tlsv12Supported;
  }

  @Override
  public String[] getDefaultCipherSuites() {
    return this.under.getDefaultCipherSuites();
  }

  @Override
  public String[] getSupportedCipherSuites() {
    return this.under.getSupportedCipherSuites();
  }

  @Override
  public Socket createSocket(
    Socket s,
    String host,
    int port,
    boolean autoClose
  )
    throws IOException {
    return fixupSocket(this.under.createSocket(s, host, port, autoClose));
  }

  @Override
  public Socket createSocket(String host, int port) throws IOException {
    return fixupSocket(this.under.createSocket(host, port));
  }

  @Override
  public Socket createSocket(
    String host,
    int port,
    InetAddress localHost,
    int localPort
  )
    throws IOException {
    return fixupSocket(
      this.under.createSocket(host, port, localHost, localPort)
    );
  }

  @Override
  public Socket createSocket(InetAddress host, int port) throws IOException {
    return fixupSocket(this.under.createSocket(host, port));
  }

  @Override
  public Socket createSocket(
    InetAddress address,
    int port,
    InetAddress localAddress,
    int localPort
  )
    throws IOException {
    return fixupSocket(
      this.under.createSocket(address, port, localAddress, localPort)
    );
  }

  private Socket fixupSocket(Socket sock) {
    if (!(sock instanceof SSLSocket)) {
      return sock;
    }

    SSLSocket sslSock = (SSLSocket) sock;

    Set<String> protos = new HashSet<>(
      Arrays.asList(sslSock.getEnabledProtocols())
    );

    if (tlsv11Supported) {
      protos.add(TLSv11Proto);
    }

    if (tlsv12Supported) {
      protos.add(TLSv12Proto);
    }

    sslSock.setEnabledProtocols(protos.toArray(new String[0]));

    return sslSock;
  }
}
