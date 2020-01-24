package com.seamlesspay.api.models;



/**
 * Contains the remote configuration for the Seamlesspay Android SDK.
 */
public class Configuration {

    public final class Keys {
        public static final String SANDBOX = "sandbox";
        public static final String PRODUCTION = "production";
    }

    public static final String API_VERSION = "v1";

    private static final String SANDBOX_API_URL = "https://sandbox.seamlesspay.com";
    private static final String PRODUCTION_API_URL = "https://api.seamlesspay.com";

    private static final String SANDBOX_PANVAULT_URL = "https://sandbox-pan-vault.seamlesspay.com";
    private static final String PRODUCTION_PANVAULT_URL = "https://pan-vault.seamlesspay.com";

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

    Configuration( String environment) {

        if (environment == null || environment != Keys.PRODUCTION) {
            mEnvironment = Keys.SANDBOX;
        } else {
            mEnvironment = Keys.PRODUCTION;
        }
    }

    /**
     * @return The url of the SeamlessPay client API for the current environment.
     */
    public String getApiUrl() {

        if (mEnvironment == Keys.SANDBOX) {
            return SANDBOX_API_URL + '/' + API_VERSION + '/';
        } else {
            return PRODUCTION_API_URL + '/' + API_VERSION + '/';
        }
    }

    /**
     * @return The tokenization url for the current environment.
     */
    public String getPanVaultUrl() {

        if (mEnvironment == Keys.SANDBOX) {
            return SANDBOX_PANVAULT_URL + '/';
        } else {
            return PRODUCTION_PANVAULT_URL + '/';
        }
    }


    public String environment() {
        return mEnvironment;
    }
}
