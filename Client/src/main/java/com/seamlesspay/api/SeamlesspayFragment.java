/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.api;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.seamlesspay.api.exceptions.InvalidArgumentException;
import com.seamlesspay.api.exceptions.SeamlesspayException;
import com.seamlesspay.api.interfaces.BaseChargeTokenCreatedListener;
import com.seamlesspay.api.interfaces.BaseVoidListener;
import com.seamlesspay.api.interfaces.PaymentMethodTokenCreatedListener;
import com.seamlesspay.api.interfaces.QueuedCallback;
import com.seamlesspay.api.interfaces.RefundTokenCreatedListener;
import com.seamlesspay.api.interfaces.SeamlesspayCancelListener;
import com.seamlesspay.api.interfaces.SeamlesspayErrorListener;
import com.seamlesspay.api.interfaces.SeamlesspayListener;
import com.seamlesspay.api.internal.BrowserSwitchFragment;
import com.seamlesspay.api.internal.SeamlesspayApiHttpClient;
import com.seamlesspay.api.models.BaseChargeToken;
import com.seamlesspay.api.models.Configuration;
import com.seamlesspay.api.models.PaymentMethodToken;
import com.seamlesspay.api.models.RefundToken;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.UUID;

import io.sentry.Sentry;
import io.sentry.SentryOptions;

/**
 * Core Seamlesspay class that handles network requests and managing callbacks.
 */
public class SeamlesspayFragment extends BrowserSwitchFragment {
  @Deprecated
  public static final String TAG = "SeamlesspayFragment";

  private static final String EXTRA_AUTHORIZATION_TOKEN =
    "com.seamlesspay.api.EXTRA_AUTHORIZATION_TOKEN";
  private static final String TAG_PREFIX = "SeamlesspayFragment";

  static final String EXTRA_CACHED_PAYMENT_METHOD_TOKENS =
    "com.seamlesspay.api.EXTRA_CACHED_PAYMENT_METHOD_TOKENS";

  static final String EXTRA_FETCHED_PAYMENT_METHOD_TOKENS =
    "com.seamlesspay.api.EXTRA_FETCHED_PAYMENT_METHOD_TOKENS";

  protected SeamlesspayApiHttpClient mApiHttpClient;
  protected SeamlesspayApiHttpClient mPanVaulHttpClient;

  private Authorization mAuthorization;
  private Configuration mConfiguration;
  private CrashReporter mCrashReporter;

  private final Queue<QueuedCallback> mCallbackQueue = new ArrayDeque<>();

  private BaseChargeTokenCreatedListener mBaseChargeTokenCreatedListener;
  private BaseVoidListener mBaseVoidListener;
  private PaymentMethodTokenCreatedListener mPaymentMethodTokenCreatedListener;
  private RefundTokenCreatedListener mRefundTokenCreatedListener;
  private SeamlesspayCancelListener mCancelListener;
  private SeamlesspayErrorListener mErrorListener;

  public SeamlesspayFragment() {}

  /**
   * Create a new instance of SeamlesspayFragment using the authorization
   * key or client token and add it to the AppCompatActivity's FragmentManager.
   * If a fragment has already been created with the provided authorization,
   * the existing fragment will be returned.
   *
   * @param activity The AppCompatActivity to add the SeamlesspayFragment to.
   * @param authorization The authorization key to use.
   * @return SeamlesspayFragment
   * @throws InvalidArgumentException If the tokenization key or client
   * token is not valid or cannot be parsed.
   */
  public static SeamlesspayFragment newInstance(
    AppCompatActivity activity,
    Authorization authorization
  )
    throws InvalidArgumentException {
    if (activity == null) {
      throw new InvalidArgumentException("Activity is null");
    }

    return newInstance(
      activity,
      activity.getSupportFragmentManager(),
      authorization
    );
  }

  /**
   * Create a new instance of SeamlesspayFragment using the tokenization key
   * or client token and add it to the Fragment's child  FragmentManager.
   * If a fragment has already been created with the provided authorization,
   * the existing fragment will be returned.
   *
   * @param fragment The Fragment to add the SeamlesspayFragment to.
   * @param authorization The authorization key  to use.
   * @return SeamlesspayFragment
   * @throws InvalidArgumentException If the tokenization key or client
   * token is not valid or cannot be parsed.
   */
  public static SeamlesspayFragment newInstance(
    Fragment fragment,
    Authorization authorization
  )
    throws InvalidArgumentException {
    if (fragment == null) {
      throw new InvalidArgumentException("Fragment is null");
    }

    return newInstance(
      fragment.getContext(),
      fragment.getChildFragmentManager(),
      authorization
    );
  }

  private static SeamlesspayFragment newInstance(
    Context context,
    FragmentManager fragmentManager,
    Authorization authorization
  )
    throws InvalidArgumentException {
    if (context == null) {
      throw new InvalidArgumentException("Context is null");
    }

    if (fragmentManager == null) {
      throw new InvalidArgumentException("FragmentManager is null");
    }

    if (authorization == null) {
      throw new InvalidArgumentException("Authorization Key is null.");
    }

    String tag =
      TAG_PREFIX +
      "." +
      UUID.nameUUIDFromBytes(authorization.toString().getBytes());

    if (fragmentManager.findFragmentByTag(tag) != null) {
      return (SeamlesspayFragment) fragmentManager.findFragmentByTag(tag);
    }

    SeamlesspayFragment seamlesspayFragment = new SeamlesspayFragment();

    Bundle bundle = new Bundle();

    bundle.putParcelable(EXTRA_AUTHORIZATION_TOKEN, authorization);
    seamlesspayFragment.setArguments(bundle);

    try {
      if (VERSION.SDK_INT >= VERSION_CODES.N) {
        try {
          fragmentManager
            .beginTransaction()
            .add(seamlesspayFragment, tag)
            .commitNow();
        } catch (IllegalStateException | NullPointerException e) {
          fragmentManager
            .beginTransaction()
            .add(seamlesspayFragment, tag)
            .commit();
          try {
            fragmentManager.executePendingTransactions();
          } catch (IllegalStateException ignored) {}
        }
      } else {
        fragmentManager
          .beginTransaction()
          .add(seamlesspayFragment, tag)
          .commit();
        try {
          fragmentManager.executePendingTransactions();
        } catch (IllegalStateException ignored) {}
      }
    } catch (IllegalStateException e) {
      throw new InvalidArgumentException(e.getMessage());
    }

    seamlesspayFragment.mContext = context.getApplicationContext();

    return seamlesspayFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setRetainInstance(true);

    if (mContext == null) {
      mContext = getActivity().getApplicationContext();
    }
    setUpSentry();
    mCrashReporter = CrashReporter.setup(this);
    mAuthorization = getArguments().getParcelable(EXTRA_AUTHORIZATION_TOKEN);
    mConfiguration =
      Configuration.fromEnvironment(mAuthorization.getEnvironment());

    if (mApiHttpClient == null) {
      mApiHttpClient =
        new SeamlesspayApiHttpClient(
          mConfiguration.getApiUrl(),
          mAuthorization.getBearerApi()
        );
    }

    if (mPanVaulHttpClient == null) {
      mPanVaulHttpClient =
        new SeamlesspayApiHttpClient(
          mConfiguration.getPanVaultUrl(),
          mAuthorization.getBearer()
        );
    }
  }

  private void setUpSentry() {
    SentryOptions options = new SentryOptions();
    options.setDsn("https://f3ca34981162465cabb2783483179ae9@o4504125304209408.ingest.sentry.io/4504140012584960");
    Sentry.init(options);
  }

  @TargetApi(VERSION_CODES.M)
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    onAttach(getActivity());
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
  }

  @Override
  public void onResume() {
    super.onResume();

    if (getActivity() instanceof SeamlesspayListener) {
      addListener((SeamlesspayListener) getActivity());
    }

    flushCallbacks();
  }

  @Override
  public void onPause() {
    super.onPause();

    if (getActivity() instanceof SeamlesspayListener) {
      removeListener((SeamlesspayListener) getActivity());
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onStop() {
    super.onStop();
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    mCrashReporter.tearDown();
  }

  @Override
  public void startActivityForResult(Intent intent, int requestCode) {
    if (!isAdded()) {
      postCallback(
        new SeamlesspayException(
          "SeamlesspayFragment is not attached to an Activity. Please ensure it " +
          "is attached and try again."
        )
      );
    } else {
      super.startActivityForResult(intent, requestCode);
    }
  }

  @Override
  public String getReturnUrlScheme() {
    return (
      getApplicationContext()
        .getPackageName()
        .toLowerCase(Locale.ROOT)
        .replace("_", "") +
      ".seamlesspay"
    );
  }

  @Override
  public void onBrowserSwitchResult(
    int requestCode,
    BrowserSwitchResult browserSwitchResult,
    Uri uri
  ) {
    String type = "";
    Intent intent = new Intent();
    int resultCode = AppCompatActivity.RESULT_FIRST_USER;

    if (browserSwitchResult == BrowserSwitchResult.OK) {
      resultCode = AppCompatActivity.RESULT_OK;
    } else if (browserSwitchResult == BrowserSwitchResult.CANCELED) {
      resultCode = AppCompatActivity.RESULT_CANCELED;
    } else if (browserSwitchResult == BrowserSwitchResult.ERROR) {
      browserSwitchResult
        .getErrorMessage()
        .startsWith("No installed activities");
    }

    onActivityResult(requestCode, resultCode, intent.setData(uri));
  }

  @Override
  public void onActivityResult(
    final int requestCode,
    int resultCode,
    Intent data
  ) {
    if (resultCode == AppCompatActivity.RESULT_CANCELED) {
      postCancelCallback(requestCode);
    }
  }

  /**
   * Adds a listener.
   *
   * @param listener the listener to add.
   */
  public <T extends SeamlesspayListener> void addListener(T listener) {
    if (listener instanceof SeamlesspayCancelListener) {
      mCancelListener = (SeamlesspayCancelListener) listener;
    }

    if (listener instanceof PaymentMethodTokenCreatedListener) {
      mPaymentMethodTokenCreatedListener =
        (PaymentMethodTokenCreatedListener) listener;
    }

    if (listener instanceof RefundTokenCreatedListener) {
      mRefundTokenCreatedListener =
          (RefundTokenCreatedListener) listener;
    }

    if (listener instanceof BaseChargeTokenCreatedListener) {
      mBaseChargeTokenCreatedListener =
        (BaseChargeTokenCreatedListener) listener;
    }

    if (listener instanceof BaseVoidListener) {
      mBaseVoidListener =
          (BaseVoidListener) listener;
    }

    if (listener instanceof SeamlesspayErrorListener) {
      mErrorListener = (SeamlesspayErrorListener) listener;
    }

    flushCallbacks();
  }

  /**
   * Removes a previously added listener.
   *
   * @param listener the listener to remove.
   */
  public <T extends SeamlesspayListener> void removeListener(T listener) {
    if (listener instanceof SeamlesspayCancelListener) {
      mCancelListener = null;
    }

    if (listener instanceof PaymentMethodTokenCreatedListener) {
      mPaymentMethodTokenCreatedListener = null;
    }

    if (listener instanceof RefundTokenCreatedListener) {
      mRefundTokenCreatedListener = null;
    }

    if (listener instanceof BaseChargeTokenCreatedListener) {
      mBaseChargeTokenCreatedListener = null;
    }

    if (listener instanceof BaseVoidListener) {
      mBaseVoidListener = null;
    }

    if (listener instanceof SeamlesspayErrorListener) {
      mErrorListener = null;
    }
  }

  /**
   * @return ArrayList SeamlesspayListener  of the currently attached listeners
   */
  public List<SeamlesspayListener> getListeners() {
    List<SeamlesspayListener> listeners = new ArrayList<>();

    if (mCancelListener != null) {
      listeners.add(mCancelListener);
    }

    if (mPaymentMethodTokenCreatedListener != null) {
      listeners.add(mPaymentMethodTokenCreatedListener);
    }

    if (mRefundTokenCreatedListener != null) {
      listeners.add(mRefundTokenCreatedListener);
    }

    if (mBaseChargeTokenCreatedListener != null) {
      listeners.add(mBaseChargeTokenCreatedListener);
    }

    if(mBaseVoidListener != null) {
      listeners.add(mBaseVoidListener);
    }

    if (mErrorListener != null) {
      listeners.add(mErrorListener);
    }

    return listeners;
  }

  protected void postCancelCallback(final int requestCode) {
    postOrQueueCallback(
      new QueuedCallback() {

        @Override
        public boolean shouldRun() {
          return mCancelListener != null;
        }

        @Override
        public void run() {
          mCancelListener.onCancel(requestCode);
        }
      }
    );
  }

  protected void postCallback(final PaymentMethodToken paymentMethodToken) {
    postOrQueueCallback(
      new QueuedCallback() {

        @Override
        public boolean shouldRun() {
          return mPaymentMethodTokenCreatedListener != null;
        }

        @Override
        public void run() {
          mPaymentMethodTokenCreatedListener.onPaymentMethodTokenCreated(
            paymentMethodToken
          );
        }
      }
    );
  }

  protected void postCallback(final RefundToken refundToken) {
    postOrQueueCallback(
        new QueuedCallback() {

          @Override
          public boolean shouldRun() {
            return mRefundTokenCreatedListener != null;
          }

          @Override
          public void run() {
            mRefundTokenCreatedListener.onRefundTokenCreated(
                refundToken
            );
          }
        }
    );
  }

  protected void postCallback(final BaseChargeToken baseChargeToken) {
    postOrQueueCallback(
      new QueuedCallback() {

        @Override
        public boolean shouldRun() {
          return mBaseChargeTokenCreatedListener != null;
        }

        @Override
        public void run() {
          mBaseChargeTokenCreatedListener.onBaseChargeTokenCreated(
            baseChargeToken
          );
        }
      }
    );
  }

  protected void postCallback() {
    postOrQueueCallback(
        new QueuedCallback() {

          @Override
          public boolean shouldRun() {
            return mBaseVoidListener != null;
          }

          @Override
          public void run() {
            mBaseVoidListener.onChargeVoided();
          }
        }
    );
  }

  protected void postCallback(final Exception error) {
    postOrQueueCallback(
      new QueuedCallback() {

        @Override
        public boolean shouldRun() {
          return mErrorListener != null;
        }

        @Override
        public void run() {
          mErrorListener.onError(error);
        }
      }
    );
  }

  protected void postOrQueueCallback(QueuedCallback callback) {
    if (!callback.shouldRun()) {
      synchronized (mCallbackQueue) {
        mCallbackQueue.add(callback);
      }
    } else {
      callback.run();
    }
  }

  protected void flushCallbacks() {
    synchronized (mCallbackQueue) {
      Queue<QueuedCallback> queue = new ArrayDeque<>(mCallbackQueue);

      for (QueuedCallback callback : queue) {
        if (callback.shouldRun()) {
          callback.run();
          mCallbackQueue.remove(callback);
        }
      }
    }
  }

  protected Authorization getAuthorization() {
    return mAuthorization;
  }

  protected Context getApplicationContext() {
    return mContext;
  }

  protected SeamlesspayApiHttpClient getApiHttpClient() {
    return mApiHttpClient;
  }

  protected SeamlesspayApiHttpClient getPanVaulHttpClient() {
    return mPanVaulHttpClient;
  }
}
