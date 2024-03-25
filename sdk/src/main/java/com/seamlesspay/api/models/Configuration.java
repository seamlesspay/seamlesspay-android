/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api.models;

/**
 * Contains the remote configuration for the Seamlesspay Android SDK.
 */
public class Configuration {

  public final class Keys {
    public static final String PRODUCTION = "production";
    public static final String SANDBOX = "sandbox";
    public static final String STAGING = "staging";
    public static final String QAT = "QAT";
  }

  //public static final String API_VERSION = "v1";
  private static final String API_VERSION = "v2020";
  private static final String API_USER_AGENT = "SeamlessPay.android.sdk";
  private static final String SANDBOX_API_URL = "https://api.sbx.seamlesspay.com";
  private static final String PRODUCTION_API_URL = "https://api.seamlesspay.com";
  private static final String STAGING_API_URL = "https://api.seamlesspay.dev";
  private static final String QAT_API_URL = "https://api.seamlesspay.io";
  private static final String SANDBOX_PANVAULT_URL = "https://pan-vault.sbx.seamlesspay.com";
  private static final String PRODUCTION_PANVAULT_URL = "https://pan-vault.seamlesspay.com";
  private static final String STAGING_PANVAULT_URL = "https://pan-vault.seamlesspay.dev";
  private static final String QAT_PANVAULT_URL = "https://pan-vault.seamlesspay.id";

  private String mEnvironment;

  /**
   * Creates a new {@link Configuration} instance from a environment.
   *
   * @param environment The  string 'sandbox' or 'production'.
   * @return {@link Configuration} instance.
   */
  public static Configuration fromEnvironment(String environment) {
    return new Configuration(environment);
  }

  Configuration(String environment) {
    if (environment == null) {
      mEnvironment = Keys.SANDBOX;
    } else  {
      mEnvironment = environment;
    }
  }

  /**
   * @return The ApiVersion of the SeamlessPay client API for the current environment.
   */
  public static String getApiVersion() {
      return API_VERSION;
  }

  public static String getApiUserAgent() {
    return API_USER_AGENT;
  }

  /**
   * @return The url of the SeamlessPay client API for the current environment.
   */
  public String getApiUrl() {
    switch (mEnvironment) {
      case Keys.SANDBOX:
        return SANDBOX_API_URL + "/";
      case Keys.STAGING:
        return STAGING_API_URL + "/";
      case Keys.QAT:
        return QAT_API_URL + "/";
      default:
        return PRODUCTION_API_URL + "/";
    }
  }

  /**
   * @return The tokenization url for the current environment.
   */
  public String getPanVaultUrl() {
    switch (mEnvironment) {
      case Keys.SANDBOX:
        return SANDBOX_PANVAULT_URL + '/';
      case Keys.STAGING:
        return STAGING_PANVAULT_URL + '/';
      case Keys.QAT:
        return QAT_PANVAULT_URL + '/';
      default:
        return PRODUCTION_PANVAULT_URL + '/';
    }
  }

  public String environment() {
    return mEnvironment;
  }
}
