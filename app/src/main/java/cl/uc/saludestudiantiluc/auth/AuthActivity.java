package cl.uc.saludestudiantiluc.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Arrays;
import java.util.List;

import cl.uc.saludestudiantiluc.MainActivity;
import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.auth.api.UserAuthApi;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AuthActivity extends BaseActivity {

  private static final String TAG = AuthActivity.class.getSimpleName();

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, AuthActivity.class);
  }

  public static final int AUTH_TYPE_OPTIONS = 0;
  public static final int AUTH_TYPE_CONNECTING = 1;
  public static final int AUTH_TYPE_LOGIN = 2;
  public static final int AUTH_TYPE_REGISTER = 3;
  public static final int AUTH_TYPE_DONE = 4;

  public static final boolean AUTO_FILL_DATA = true;
  private Retrofit mRetrofitInstance;
  private UserAuthApi mApiInstance;
  private int mLastType = -1;

  private ProgressBar mProgressBar;
  private boolean mFragmentShown = false;

  public void setNewFragment(int authType) {
    mLastType = authType;
    if (mProgressBar.getVisibility() == View.VISIBLE) {
      mProgressBar.setVisibility(View.GONE);
    }
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    switch (authType) {
      case AUTH_TYPE_OPTIONS:
        transaction.replace(R.id.auth_fragment_container, new OptionsFragment());
        break;
      case AUTH_TYPE_LOGIN:
        transaction.replace(R.id.auth_fragment_container, new LoginFragment());
        break;
      case AUTH_TYPE_REGISTER:
        transaction.replace(R.id.auth_fragment_container, new RegisterFragment());
        break;
      default:
        break;
    }
    if (mFragmentShown) {
      transaction.addToBackStack(null);
    }
    transaction.commit();
    mFragmentShown = true;
  }

  public Retrofit getRetrofitInstance() {
    return mRetrofitInstance;
  }

  public ProgressBar getProgressBar() {
    return mProgressBar;
  }

  public UserAuthApi getApiInstance() {
    return mApiInstance;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.auth_activity);

    // If the user is logged in, go to main activity. If he is not, then show the auth fragment.
    if (getUserRepository().isUserLoggedIn()) {
      onUserLoggedIn();
      return;
    }

    loadMainBackground();

    mRetrofitInstance = new Retrofit.Builder()
        .baseUrl(UserAuthApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    mApiInstance = mRetrofitInstance.create(UserAuthApi.class);

    mProgressBar = (ProgressBar) findViewById(R.id.auth_progress_bar);
    mProgressBar.setVisibility(View.GONE);

    setNewFragment(AUTH_TYPE_OPTIONS);
  }

  public void onUserLoggedIn() {
    getRelaxUcApplication().onUserLoggedIn();
    startActivity(MainActivity.getIntent(this));
    finish();
  }

  public boolean isEmailAndPasswordCorrect(TextInputEditText emailEditText, TextInputEditText passwordEditText) {
    emailEditText.setError(null);
    passwordEditText.setError(null);

    String email = emailEditText.getText().toString();
    if (TextUtils.isEmpty(email)) {
      emailEditText.setError(getString(R.string.auth_error_field_required));
      emailEditText.requestFocus();
      return false;
    } else if (!isEmailValid(email)) {
      emailEditText.setError(getString(R.string.auth_error_invalid_email));
      emailEditText.requestFocus();
      return false;
    }

    String password = passwordEditText.getText().toString();
    if (TextUtils.isEmpty(password)) {
      passwordEditText.setError(getString(R.string.auth_error_field_required));
      passwordEditText.requestFocus();
      return false;
    } else if (!isPasswordValid(password)) {
      passwordEditText.setError(getString(R.string.auth_error_invalid_password));
      passwordEditText.requestFocus();
      return false;
    }
    return true;
  }

  public boolean isPasswordConfirmationCorrect(TextInputEditText passwordEditText, TextInputEditText confirmPasswordEditText) {
    confirmPasswordEditText.setError(null);
    String passwordConfirmation = confirmPasswordEditText.getText().toString();

    if (TextUtils.isEmpty(passwordConfirmation)) {
      confirmPasswordEditText.setError(getString(R.string.auth_error_field_required));
      confirmPasswordEditText.requestFocus();
      return false;
    } else if (!isPasswordValid(passwordConfirmation)) {
      confirmPasswordEditText.setError(getString(R.string.auth_error_invalid_password));
      confirmPasswordEditText.requestFocus();
      return false;
    }
    String password = passwordEditText.getText().toString();
    if (!password.equals(passwordConfirmation)) {
      confirmPasswordEditText.setError(getString(R.string.auth_error_incorrect_password_confirmation));
      confirmPasswordEditText.requestFocus();
      return false;
    }
    return true;
  }

  private boolean isEmailValid(String email) {
    return email.contains("@") && email.endsWith("uc.cl");
  }

  private boolean isPasswordValid(String password) {
    return password.length() > 4;
  }

  @Override
  public void onBackPressed() {
    switch (mLastType) {
      case AUTH_TYPE_LOGIN:
        setNewFragment(AUTH_TYPE_OPTIONS);
        return;
      case AUTH_TYPE_REGISTER:
        setNewFragment(AUTH_TYPE_OPTIONS);
        return;
    }
    super.onBackPressed();
  }
}
