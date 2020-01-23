package com.seamlesspay.api;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;

import com.seamlesspay.api.exceptions.SeamlesspayException;
import com.seamlesspay.api.exceptions.InvalidArgumentException;
import com.seamlesspay.api.interfaces.SeamlesspayCancelListener;
import com.seamlesspay.api.interfaces.SeamlesspayErrorListener;
import com.seamlesspay.api.interfaces.SeamlesspayListener;

import com.seamlesspay.api.interfaces.PaymentMethodNonceCreatedListener;
import com.seamlesspay.api.interfaces.BaseChargeNonceCreatedListener;
import com.seamlesspay.api.interfaces.QueuedCallback;

import com.seamlesspay.api.internal.BrowserSwitchFragment;
import com.seamlesspay.api.internal.SeamlesspayApiHttpClient;

import com.seamlesspay.api.models.BaseChargeNonce;
import com.seamlesspay.api.models.Configuration;
import com.seamlesspay.api.models.PaymentMethodNonce;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * Core Seamlesspay class that handles network requests and managing callbacks.
 */
public class SeamlesspayFragment extends BrowserSwitchFragment {

    @Deprecated
    public static final String TAG = "SeamlesspayFragment";
    private static final String EXTRA_AUTHORIZATION_TOKEN = "com.seamlesspay.api.EXTRA_AUTHORIZATION_TOKEN";
    private static final String TAG_PREFIX = "SeamlesspayFragment";


    @VisibleForTesting
    static final String EXTRA_CACHED_PAYMENT_METHOD_NONCES = "com.seamlesspay.api.EXTRA_CACHED_PAYMENT_METHOD_NONCES";
    @VisibleForTesting
    static final String EXTRA_FETCHED_PAYMENT_METHOD_NONCES = "com.seamlesspay.api.EXTRA_FETCHED_PAYMENT_METHOD_NONCES";


    protected SeamlesspayApiHttpClient mPanVaulHttpClient;
    protected SeamlesspayApiHttpClient mApiHttpClient;

    private CrashReporter mCrashReporter;
    private Authorization mAuthorization;
    private Configuration mConfiguration;
    private final Queue<QueuedCallback> mCallbackQueue = new ArrayDeque<>();

    private SeamlesspayCancelListener mCancelListener;
    private PaymentMethodNonceCreatedListener mPaymentMethodNonceCreatedListener;
    private BaseChargeNonceCreatedListener mBaseChargeNonceCreatedListener;
    private SeamlesspayErrorListener mErrorListener;

    public SeamlesspayFragment() {}

    /**
     * Create a new instance of {@link SeamlesspayFragment} using the authorization key or client token and add it to the
     * {@link AppCompatActivity}'s {@link FragmentManager}. If a fragment has already been created with the provided
     * authorization, the existing fragment will be returned.
     *
     * @param activity The {@link AppCompatActivity} to add the {@link SeamlesspayFragment} to.
     * @param authorization The authorization key to use.
     * @return {@link SeamlesspayFragment}
     * @throws InvalidArgumentException If the tokenization key or client token is not valid or cannot be
     *         parsed.
     */
    public static SeamlesspayFragment newInstance(AppCompatActivity activity, Authorization authorization) throws InvalidArgumentException {
        if (activity == null) {
            throw new InvalidArgumentException("Activity is null");
        }

        return newInstance(activity, activity.getSupportFragmentManager(), authorization);
    }

    /**
     * Create a new instance of {@link SeamlesspayFragment} using the tokenization key or client token and add it to the
     * {@link Fragment}'s child {@link FragmentManager}. If a fragment has already been created with the provided
     * authorization, the existing fragment will be returned.
     *
     * @param fragment The {@link Fragment} to add the {@link SeamlesspayFragment} to.
     * @param authorization The authorization key  to use.
     * @return {@link SeamlesspayFragment}
     * @throws InvalidArgumentException If the tokenization key or client token is not valid or cannot be
     *         parsed.
     */
    public static SeamlesspayFragment newInstance(Fragment fragment, Authorization authorization) throws InvalidArgumentException {
        if (fragment == null) {
            throw new InvalidArgumentException("Fragment is null");
        }

        return newInstance(fragment.getContext(), fragment.getChildFragmentManager(), authorization);
    }

    private static SeamlesspayFragment newInstance(Context context,
                                                 FragmentManager fragmentManager,
                                                 Authorization authorization) throws InvalidArgumentException {
        if (context == null) {
            throw new InvalidArgumentException("Context is null");
        }

        if (fragmentManager == null) {
            throw new InvalidArgumentException("FragmentManager is null");
        }

        if (authorization == null) {
            throw new InvalidArgumentException("Authorization Key is null.");
        }

        String tag = TAG_PREFIX + "." + UUID.nameUUIDFromBytes(authorization.toString().getBytes());

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
                    fragmentManager.beginTransaction().add(seamlesspayFragment, tag).commitNow();
                } catch (IllegalStateException | NullPointerException e) {
                    fragmentManager.beginTransaction().add(seamlesspayFragment, tag).commit();
                    try {
                        fragmentManager.executePendingTransactions();
                    } catch (IllegalStateException ignored) {}
                }
            } else {
                fragmentManager.beginTransaction().add(seamlesspayFragment, tag).commit();
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

        mCrashReporter = CrashReporter.setup(this);
        mAuthorization = getArguments().getParcelable(EXTRA_AUTHORIZATION_TOKEN);

        mConfiguration = Configuration.fromEnvironment(mAuthorization.getEnvironment());

        if (mApiHttpClient == null) {
            mApiHttpClient = new SeamlesspayApiHttpClient(mConfiguration.getApiUrl(), mAuthorization.getBearerApi());
        }

        if (mPanVaulHttpClient == null) {
            mPanVaulHttpClient = new SeamlesspayApiHttpClient(mConfiguration.getPanVaultUrl(), mAuthorization.getBearer());
        }
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
            postCallback(new SeamlesspayException("SeamlesspayFragment is not attached to an Activity. Please ensure it " +
                    "is attached and try again."));
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public String getReturnUrlScheme() {
        return getApplicationContext().getPackageName().toLowerCase(Locale.ROOT)
                .replace("_", "") + ".seamlesspay";
    }

    @Override
    public void onBrowserSwitchResult(int requestCode, BrowserSwitchResult browserSwitchResult, @Nullable Uri uri) {
        String type = "";
        Intent intent = new Intent();

        int resultCode = AppCompatActivity.RESULT_FIRST_USER;
        if (browserSwitchResult == BrowserSwitchResult.OK) {
            resultCode = AppCompatActivity.RESULT_OK;

        } else if (browserSwitchResult == BrowserSwitchResult.CANCELED) {
            resultCode = AppCompatActivity.RESULT_CANCELED;

        } else if (browserSwitchResult == BrowserSwitchResult.ERROR) {
          browserSwitchResult.getErrorMessage().startsWith("No installed activities");
        }
        onActivityResult(requestCode, resultCode, intent.setData(uri));
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {

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

        if (listener instanceof PaymentMethodNonceCreatedListener) {
            mPaymentMethodNonceCreatedListener = (PaymentMethodNonceCreatedListener) listener;
        }

        if (listener instanceof BaseChargeNonceCreatedListener) {
            mBaseChargeNonceCreatedListener = (BaseChargeNonceCreatedListener) listener;
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

        if (listener instanceof PaymentMethodNonceCreatedListener) {
            mPaymentMethodNonceCreatedListener = null;
        }

        if (listener instanceof BaseChargeNonceCreatedListener) {
            mBaseChargeNonceCreatedListener = null;
        }

        if (listener instanceof SeamlesspayErrorListener) {
            mErrorListener = null;
        }

    }

    /**
     * @return {@link ArrayList< SeamlesspayListener >} of the currently attached listeners
     */
    public List<SeamlesspayListener> getListeners() {
        List<SeamlesspayListener> listeners = new ArrayList<>();

        if (mCancelListener != null) {
            listeners.add(mCancelListener);
        }

        if (mPaymentMethodNonceCreatedListener != null) {
            listeners.add(mPaymentMethodNonceCreatedListener);
        }

        if (mBaseChargeNonceCreatedListener != null) {
            listeners.add(mBaseChargeNonceCreatedListener);
        }

        if (mErrorListener != null) {
            listeners.add(mErrorListener);
        }

        return listeners;
    }

    protected void postCancelCallback(final int requestCode) {
        postOrQueueCallback(new QueuedCallback() {
            @Override
            public boolean shouldRun() {
                return mCancelListener != null;
            }

            @Override
            public void run() {
                mCancelListener.onCancel(requestCode);
            }
        });
    }

    protected void postCallback(final PaymentMethodNonce paymentMethodNonce) {

        postOrQueueCallback(new QueuedCallback() {
            @Override
            public boolean shouldRun() {
                return mPaymentMethodNonceCreatedListener != null;
            }

            @Override
            public void run() {
                mPaymentMethodNonceCreatedListener.onPaymentMethodNonceCreated(paymentMethodNonce);
            }
        });
    }

    protected void postCallback(final BaseChargeNonce baseChargeNonce) {

        postOrQueueCallback(new QueuedCallback() {
            @Override
            public boolean shouldRun() {
                return mBaseChargeNonceCreatedListener != null;
            }

            @Override
            public void run() {
                mBaseChargeNonceCreatedListener.onBaseChargeNonceCreated(baseChargeNonce);
            }
        });
    }

    protected void postCallback(final Exception error) {
        postOrQueueCallback(new QueuedCallback() {
            @Override
            public boolean shouldRun() {
                return mErrorListener != null;
            }

            @Override
            public void run() {
                mErrorListener.onError(error);
            }
        });
    }

    @VisibleForTesting
    protected void postOrQueueCallback(QueuedCallback callback) {
        if (!callback.shouldRun()) {
            synchronized (mCallbackQueue) {
                mCallbackQueue.add(callback);
            }
        } else {
            callback.run();
        }
    }

    @VisibleForTesting
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
