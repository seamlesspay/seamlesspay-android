/**
 * Copyright (c) Seamless Payments, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.seamlesspay.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import androidx.annotation.CallSuper;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import com.seamlesspay.api.Authorization;
import com.seamlesspay.api.SeamlesspayFragment;
import com.seamlesspay.api.exceptions.InvalidArgumentException;
import com.seamlesspay.api.interfaces.BaseChargeTokenCreatedListener;
import com.seamlesspay.api.interfaces.PaymentMethodTokenCreatedListener;
import com.seamlesspay.api.interfaces.SeamlesspayCancelListener;
import com.seamlesspay.api.interfaces.SeamlesspayErrorListener;
import com.seamlesspay.api.models.BaseChargeToken;
import com.seamlesspay.api.models.PaymentMethodToken;
import com.seamlesspay.demo.R;

@SuppressWarnings("deprecation")
public abstract class BaseActivity
  extends AppCompatActivity
  implements
    OnRequestPermissionsResultCallback,
    PaymentMethodTokenCreatedListener,
    BaseChargeTokenCreatedListener,
    SeamlesspayCancelListener,
    SeamlesspayErrorListener,
    ActionBar.OnNavigationListener {
  private static final String EXTRA_AUTHORIZATION =
    "com.seamlesspay.demo.EXTRA_AUTHORIZATION";

  protected Authorization mAuthorization;
  protected SeamlesspayFragment mSeamlesspayFragment;

  private boolean mActionBarSetup;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    setProgressBarIndeterminateVisibility(true);

    if (savedInstanceState != null) {
      mAuthorization = savedInstanceState.getParcelable(EXTRA_AUTHORIZATION);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (!mActionBarSetup) {
      setupActionBar();
      mActionBarSetup = true;
    }

    performReset();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    if (mAuthorization != null) {
      outState.putParcelable(EXTRA_AUTHORIZATION, mAuthorization);
    }
  }

  @CallSuper
  @Override
  public void onBaseChargeTokenCreated(BaseChargeToken baseChargeToken) {
    setProgressBarIndeterminateVisibility(true);

    Log.d(
      getClass().getSimpleName(),
      "Charge Token received: " + baseChargeToken.getChargeId()
    );
  }

  @CallSuper
  @Override
  public void onPaymentMethodTokenCreated(
    PaymentMethodToken paymentMethodToken
  ) {
    setProgressBarIndeterminateVisibility(true);

    Log.d(
      getClass().getSimpleName(),
      "Payment Method Token received: " + paymentMethodToken.getTxnType()
    );
  }

  @CallSuper
  @Override
  public void onCancel(int requestCode) {
    setProgressBarIndeterminateVisibility(false);

    Log.d(getClass().getSimpleName(), "Cancel received: " + requestCode);
  }

  @CallSuper
  @Override
  public void onError(Exception error) {
    setProgressBarIndeterminateVisibility(false);

    Log.d(
      getClass().getSimpleName(),
      "Error received (" + error.getClass() + "): " + error.getMessage()
    );
    Log.d(getClass().getSimpleName(), error.toString());

    showDialog("Error received:\n" + error.getMessage());
  }

  private void performReset() {
    setProgressBarIndeterminateVisibility(true);

    try {
      mAuthorization =
        Authorization.fromKeys(
          Settings.getEnvironmentName(this),
          Settings.getEnvironmentTokenizationKey(this)
        );
    } catch (InvalidArgumentException ex) {
      showDialog("An error occurred (" + ex.getMessage());
    }

    if (mSeamlesspayFragment != null) {
      getSupportFragmentManager()
        .beginTransaction()
        .remove(mSeamlesspayFragment)
        .commit();

      mSeamlesspayFragment = null;
    }

    setProgressBarIndeterminateVisibility(false);
    onAuthorizationFetched();
  }

  protected abstract void reset();

  protected abstract void onAuthorizationFetched();

  protected void showDialog(String message) {
    new AlertDialog.Builder(this)
      .setMessage(message)
      .setPositiveButton(
        android.R.string.ok,
        new OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        }
      )
      .show();
  }

  protected void setUpAsBack() {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

  @SuppressWarnings("ConstantConditions")
  private void setupActionBar() {
    ActionBar actionBar = getSupportActionBar();

    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
      this,
      R.array.environments,
      android.R.layout.simple_spinner_dropdown_item
    );

    actionBar.setListNavigationCallbacks(adapter, this);
    actionBar.setSelectedNavigationItem(Settings.getEnvironment(this));
  }

  @Override
  public boolean onNavigationItemSelected(int itemPosition, long itemId) {
    if (Settings.getEnvironment(this) != itemPosition) {
      Settings.setEnvironment(this, itemPosition);

      performReset();
    }

    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      case R.id.reset:
        reset();
        return true;
      default:
        return false;
    }
  }
}
