/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;
import com.seamlesspay.api.exceptions.InvalidArgumentException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

/**
 * Generic base class for SeamlessPay authorization
 */
public class Authorization implements Parcelable {
  private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
  private static final String KEY_ALIAS = "KEY_ALIAS";
  private static final String AES_MODE = "AES/ECB/PKCS7Padding";
  private static final String RSA_MODE =  "RSA/ECB/PKCS1Padding";

  private String mAuthorizationApiKey;
  private String mAuthorizationKey;
  private String mEnvironment;
  private String mEncryptionKey;
  private KeyStore keyStore;

  Authorization(
    String environmentString,
    String authorizationKeyString,
    String authorizationApiKeyString,
    Context context
  )
    throws InvalidArgumentException {
    if (TextUtils.isEmpty(authorizationApiKeyString)) {
      throw new InvalidArgumentException("Authorization key was invalid");
    }

    loadKeyStore(context);

    String authApiKey = Base64.encodeToString(authorizationApiKeyString.getBytes(), Base64.NO_WRAP);
    if (keyStore != null && mEncryptionKey != null) {
      mAuthorizationApiKey = encrypt(Base64.decode(authApiKey, Base64.NO_WRAP));
      if (mAuthorizationApiKey == null) {
        mAuthorizationApiKey = authApiKey;
      }
    } else  {
      mAuthorizationApiKey = authApiKey;
    }

    String authKey = Base64.encodeToString(authorizationKeyString.getBytes(), Base64.NO_WRAP);
    if (keyStore != null && mEncryptionKey != null) {
      mAuthorizationKey = encrypt(Base64.decode(authKey, Base64.NO_WRAP));
      if (mAuthorizationKey == null) {
        mAuthorizationKey = authKey;
      }
    } else  {
      mAuthorizationKey = authKey;
    }

    mEnvironment = environmentString;
  }

  private String encrypt(byte[] input) {
    Cipher c;
    try {
      c = Cipher.getInstance(AES_MODE);
      c.init(Cipher.ENCRYPT_MODE, getSecretKey());
      byte[] encodedBytes = c.doFinal(input);
      return Base64.encodeToString(encodedBytes, Base64.NO_WRAP);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  private byte[] decrypt(byte[] encrypted) {
    Cipher c;
    try {
      c = Cipher.getInstance(AES_MODE);
      c.init(Cipher.DECRYPT_MODE, getSecretKey());
      return c.doFinal(encrypted);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private Key getSecretKey() throws Exception {
    byte[] encryptedKey = Base64.decode(mEncryptionKey, Base64.NO_WRAP);
    byte[] key = rsaDecrypt(encryptedKey);
    return new SecretKeySpec(key, "AES");
  }

  private byte[] rsaEncrypt(byte[] secret) throws Exception{
    KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
    // Encrypt the text
    Cipher inputCipher = Cipher.getInstance(RSA_MODE, "AndroidKeyStoreBCWorkaround");
    inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inputCipher);
    cipherOutputStream.write(secret);
    cipherOutputStream.close();

    return outputStream.toByteArray();
  }

  private byte[] rsaDecrypt(byte[] encrypted) throws Exception {
    KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(KEY_ALIAS, null);
    Cipher output = Cipher.getInstance(RSA_MODE, "AndroidKeyStoreBCWorkaround");
    output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
    CipherInputStream cipherInputStream = new CipherInputStream(
        new ByteArrayInputStream(encrypted), output);
    ArrayList<Byte> values = new ArrayList<>();
    int nextByte;
    while ((nextByte = cipherInputStream.read()) != -1) {
      values.add((byte)nextByte);
    }

    byte[] bytes = new byte[values.size()];
    for(int i = 0; i < bytes.length; i++) {
      bytes[i] = values.get(i);
    }
    return bytes;
  }

  void loadKeyStore(Context context) {
    try {
      keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
      keyStore.load(null);

      // Generate the RSA key pairs
      if (!keyStore.containsAlias(KEY_ALIAS)) {
        // Generate a key pair for encryption
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 30);
        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
            .setAlias(KEY_ALIAS)
            .setSubject(new X500Principal("CN=" + KEY_ALIAS))
            .setSerialNumber(BigInteger.TEN)
            .setStartDate(start.getTime())
            .setEndDate(end.getTime())
            .build();
        KeyPairGenerator
            kpg = KeyPairGenerator.getInstance("RSA", ANDROID_KEYSTORE);
        kpg.initialize(spec);
        kpg.generateKeyPair();
      }

      //Generate aes key
      if (mEncryptionKey == null) {
        byte[] key = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);
        byte[] encryptedKey = rsaEncrypt(key);
        mEncryptionKey = Base64.encodeToString(encryptedKey, Base64.NO_WRAP);
      }

    } catch (Exception e) {
      mEncryptionKey = null;
      e.printStackTrace();
    }
  }

  /**
   * Returns an {@link Authorization} of the correct type for a given authorizationKeyString,
   * authorizationApiKeyString,environmentString.
   *
   * @param authorizationKeyString Given string to transform into an Authorization}
   * @return {@link Authorization}
   * @throws InvalidArgumentException This method will throw this exception type if the string
   * passed does not meet any of the criteria supplied for AuthorizationKey and AuthorizationApiKey.
   */

  public static Authorization fromKeys(
    String environmentString,
    String authorizationKeyString,
    Context context
  )
    throws InvalidArgumentException {
    return Authorization.fromKeys(
      environmentString,
      authorizationKeyString,
      null,
      context
    );
  }

  public static Authorization fromKeys(
    String environmentString,
    String authorizationKeyString,
    String authorizationApiKeyString,
    Context context
  )
    throws InvalidArgumentException {
    if (
      TextUtils.isEmpty(authorizationApiKeyString) ||
      authorizationApiKeyString == null
    ) {
      authorizationApiKeyString = authorizationKeyString;
    }
    return new Authorization(
      environmentString,
      authorizationKeyString,
      authorizationApiKeyString,
      context
    );
  }

  /**
   * @return the current  environment.
   */
  public String getEnvironment() {
    return mEnvironment;
  }

  /**
   * @return The authorization bearer string for authorizing requests.
   */
  public String getBearer() {
    if (keyStore == null) {
      return mAuthorizationKey;
    } else {
      return Base64.encodeToString(decrypt(Base64.decode(mAuthorizationKey, Base64.NO_WRAP)),
          Base64.DEFAULT);
    }
  }

  /**
   * @return The authorization bearer string for authorizing API requests.
   */
  public String getBearerApi() {
    if (keyStore == null) {
      return mAuthorizationApiKey;
    } else {
      return Base64.encodeToString(decrypt(Base64.decode(mAuthorizationApiKey, Base64.NO_WRAP)),
          Base64.NO_WRAP);
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(mAuthorizationKey);
    parcel.writeString(mAuthorizationApiKey);
    parcel.writeString(mEnvironment);
    parcel.writeString(mEncryptionKey);
  }

  public Authorization(Parcel in) {
    mAuthorizationKey = in.readString();
    mAuthorizationApiKey = in.readString();
    mEnvironment = in.readString();
    mEncryptionKey = in.readString();
  }

  public static final Creator<Authorization> CREATOR = new Creator<Authorization>() {

    public Authorization createFromParcel(Parcel source) {
      return new Authorization(source);
    }

    public Authorization[] newArray(int size) {
      return new Authorization[size];
    }
  };
}
