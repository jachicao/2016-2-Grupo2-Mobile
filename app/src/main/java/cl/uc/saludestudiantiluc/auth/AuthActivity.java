package cl.uc.saludestudiantiluc.auth;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import cl.uc.saludestudiantiluc.MainActivity;
import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.auth.api.UserAuthApi;
import cl.uc.saludestudiantiluc.auth.data.UserRepository;
import cl.uc.saludestudiantiluc.auth.models.LoginResponse;
import cl.uc.saludestudiantiluc.auth.models.RegisterResponse;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AuthActivity extends BaseActivity {

  private static final String TAG = AuthActivity.class.getSimpleName();

  private static final int AUTH_TYPE_OPTIONS = 0;
  private static final int AUTH_TYPE_CONNECTING = 1;
  private static final int AUTH_TYPE_LOGIN = 2;
  private static final int AUTH_TYPE_REGISTER = 3;
  private static final int AUTH_TYPE_DONE = 4;

  public static final boolean AUTO_FILL_DATA = true;
  private View mCardView;
  private UserAuthApi mApiInstance;
  private View mEmailLayout;
  private TextInputEditText mEmailEditText;
  private View mPasswordLayout;
  private TextInputEditText mPasswordEditText;
  private View mPasswordConfirmLayout;
  private TextInputEditText mPasswordConfirmEditText;
  private View mProgressBar;
  private Button mLoginButton;
  private CheckBox mRememberMeCheckBox;
  private Button mRegisterButton;
  private boolean mAttemptingToRegister = false;
  private boolean mAttemptingToLogin = false;
  private int mLastTypeSet = AUTH_TYPE_OPTIONS;

  private UserRepository mUserRepository;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.auth_activity);

    mUserRepository = getUserRepository();

    // If the user is logged in, go to main activity. If he is not, then show the auth fragment.
    if (mUserRepository.isUserLoggedIn()) {
      onUserLoggedIn();
      return;
    }

    loadMainBackground();

    mCardView = findViewById(R.id.auth_card_view);
    mEmailLayout = findViewById(R.id.auth_email_layout);
    mEmailEditText = (TextInputEditText) findViewById(R.id.auth_email_editText);
    mPasswordLayout = findViewById(R.id.auth_password_layout);
    mPasswordEditText = (TextInputEditText) findViewById(R.id.auth_password);
    mProgressBar = findViewById(R.id.auth_progress_bar);
    mPasswordConfirmEditText = (TextInputEditText) findViewById(
        R.id.auth_register_password_confirmation);
    mPasswordConfirmLayout = findViewById(
        R.id.auth_register_password_confirmation_layout);
    mLoginButton = (Button) findViewById(R.id.auth_sign_in_button);
    mRegisterButton = (Button) findViewById(R.id.auth_register_button);
    mRememberMeCheckBox = (CheckBox) findViewById(R.id.auth_remember_me);

    //Set everything as gone
    mCardView.setVisibility(View.GONE);
    mEmailLayout.setVisibility(View.GONE);
    mPasswordLayout.setVisibility(View.GONE);
    mPasswordConfirmLayout.setVisibility(View.GONE);
    mLoginButton.setVisibility(View.GONE);
    mRegisterButton.setVisibility(View.GONE);
    mProgressBar.setVisibility(View.GONE);
    mRememberMeCheckBox.setVisibility(View.GONE);
    if (AUTO_FILL_DATA) {
      mEmailEditText.setText("lezorich@uc.cl");
      mPasswordEditText.setText("123456");
      mPasswordConfirmEditText.setText("123456");
    }

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(UserAuthApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    mApiInstance = retrofit.create(UserAuthApi.class);

    mLoginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mLastTypeSet == AUTH_TYPE_OPTIONS) {
          show(AUTH_TYPE_LOGIN);
        } else {
          attemptLogin();
        }
      }
    });
    mRegisterButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mLastTypeSet == AUTH_TYPE_OPTIONS) {
          show(AUTH_TYPE_REGISTER);
        } else {
          attemptRegister();
        }
      }
    });
    mPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          if (mLastTypeSet == AUTH_TYPE_LOGIN) {
            attemptLogin();
          }
          return true;
        }
        return false;
      }
    });
    mPasswordConfirmEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          if (mLastTypeSet == AUTH_TYPE_REGISTER) {
            attemptRegister();
          }
          return true;
        }
        return false;
      }
    });
    show(AUTH_TYPE_OPTIONS);
  }

  public void onUserLoggedIn() {
    startActivity(MainActivity.getIntent(this));
    finish();
  }
  public static Intent getIntent(Activity activity) {
    return new Intent(activity, AuthActivity.class);
  }

  private boolean isEmailAndPasswordCorrect() {
    mEmailEditText.setError(null);
    mPasswordEditText.setError(null);
    mPasswordConfirmEditText.setError(null);

    String password = mPasswordEditText.getText().toString();

    if (TextUtils.isEmpty(password)) {
      mPasswordEditText.setError(getString(R.string.auth_error_field_required));
      mPasswordEditText.requestFocus();
      return false;
    } else if (!isPasswordValid(password)) {
      mPasswordEditText.setError(getString(R.string.auth_error_invalid_password));
      mPasswordEditText.requestFocus();
      return false;
    }
    String email = mEmailEditText.getText().toString();
    if (TextUtils.isEmpty(email)) {
      mEmailEditText.setError(getString(R.string.auth_error_field_required));
      mEmailEditText.requestFocus();
      return false;
    } else if (!isEmailValid(email)) {
      mEmailEditText.setError(getString(R.string.auth_error_invalid_email));
      mEmailEditText.requestFocus();
      return false;
    }
    return true;
  }

  private boolean isPasswordConfirmationCorrect() {
    mPasswordConfirmEditText.setError(null);
    String passwordConfirmation = mPasswordConfirmEditText.getText().toString();

    if (TextUtils.isEmpty(passwordConfirmation)) {
      mPasswordConfirmEditText.setError(getString(R.string.auth_error_field_required));
      mPasswordConfirmEditText.requestFocus();
      return false;
    } else if (!isPasswordValid(passwordConfirmation)) {
      mPasswordConfirmEditText.setError(getString(R.string.auth_error_invalid_password));
      mPasswordConfirmEditText.requestFocus();
      return false;
    }
    String password = mPasswordEditText.getText().toString();
    if (!password.equals(passwordConfirmation)) {
      mPasswordConfirmEditText.setError(getString(R.string.auth_error_incorrect_password_confirmation));
      mPasswordConfirmEditText.requestFocus();
      return false;
    }
    return true;
  }

  private void attemptRegister() {
    if (mAttemptingToRegister) {
      return;
    }
    if (isPasswordConfirmationCorrect()) {
      mAttemptingToRegister = true;

      final String email = mEmailEditText.getText().toString();
      String password = mPasswordEditText.getText().toString();

      show(AUTH_TYPE_CONNECTING);
      Call<RegisterResponse> callInstance = mApiInstance.register(email, password, password);
      callInstance.enqueue(new Callback<RegisterResponse>() {
        @Override
        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
          mAttemptingToRegister = false;
          RegisterResponse res = response.body();
          if (!response.isSuccessful()) {
            // TODO: Show errors
            show(AUTH_TYPE_OPTIONS);
            Snackbar.make(findViewById(R.id.auth_coordinator_layout), "Hubo un error", Snackbar.LENGTH_LONG).show();
          } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this);
            builder.setMessage(getString(R.string.auth_confirmation_email))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.auth_confirmation_ok), new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    show(AUTH_TYPE_OPTIONS);
                  }
                });
            AlertDialog alert = builder.create();
            alert.show();
          }
        }

        @Override
        public void onFailure(Call<RegisterResponse> call, Throwable t) {
          mAttemptingToRegister = false;
          Snackbar.make(findViewById(R.id.auth_coordinator_layout), t.getMessage(), Snackbar.LENGTH_LONG).show();
          show(AUTH_TYPE_OPTIONS);
        }
      });
    }
  }

  private void attemptLogin() {
    if (mAttemptingToLogin) {
      return;
    }
    if (isEmailAndPasswordCorrect()) {
      mAttemptingToLogin = true;

      String email = mEmailEditText.getText().toString();
      String password = mPasswordEditText.getText().toString();

      show(AUTH_TYPE_CONNECTING);
      Call<LoginResponse> callInstance = mApiInstance.logIn(email, password);
      callInstance.enqueue(new Callback<LoginResponse>() {
        @Override
        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
          mAttemptingToLogin = false;
          LoginResponse res = response.body();
          if (res.errors.size() > 0) {
            String error = "";
            for (String e : res.errors) {
              error += e;
            }
            Snackbar.make(findViewById(R.id.auth_coordinator_layout), error,
                Snackbar.LENGTH_LONG).show();
            show(AUTH_TYPE_OPTIONS);
          } else {
            final String accessToken = response.headers().get("access-token");
            final String accessTokenClient = response.headers().get("client");
            final String uid = response.headers().get("uid");
            mUserRepository.storeAccessToken(accessToken);
            mUserRepository.storeAccessTokenClient(accessTokenClient);
            mUserRepository.storeUid(uid);
            mUserRepository.storeUserEmail(res.getEmail());
            mUserRepository.storeUserName("Lukas Zorich");
            Log.d(TAG, res.getEmail());
            getRelaxUcApplication().invalidateUserCredentials();
            onUserLoggedIn();
          }
        }

        @Override
        public void onFailure(Call<LoginResponse> call, Throwable t) {
          mAttemptingToLogin = false;
          Snackbar.make(findViewById(R.id.auth_coordinator_layout), t.getMessage(),
              Snackbar.LENGTH_LONG).show();
          show(AUTH_TYPE_OPTIONS);
        }
      });
    }
  }

  private boolean isEmailValid(String email) {
    return email.contains("@") && email.endsWith("uc.cl");
  }

  private boolean isPasswordValid(String password) {
    return password.length() > 4;
  }

  private void show(int authType) {
    mLastTypeSet = authType;
    switch (authType) {
      case AUTH_TYPE_OPTIONS:
        mCardView.setVisibility(View.VISIBLE);
        mEmailLayout.setVisibility(View.GONE);
        mPasswordLayout.setVisibility(View.GONE);
        mPasswordConfirmLayout.setVisibility(View.GONE);
        mLoginButton.setVisibility(View.VISIBLE);
        mRegisterButton.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mRememberMeCheckBox.setVisibility(View.GONE);
        break;
      case AUTH_TYPE_CONNECTING:
        mCardView.setVisibility(View.GONE);
        mEmailLayout.setVisibility(View.GONE);
        mPasswordLayout.setVisibility(View.GONE);
        mPasswordConfirmLayout.setVisibility(View.GONE);
        mLoginButton.setVisibility(View.GONE);
        mRegisterButton.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mRememberMeCheckBox.setVisibility(View.GONE);
        break;
      case AUTH_TYPE_LOGIN:
        mPasswordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mPasswordConfirmEditText.setImeOptions(EditorInfo.IME_NULL);
        mCardView.setVisibility(View.VISIBLE);
        mEmailLayout.setVisibility(View.VISIBLE);
        mPasswordLayout.setVisibility(View.VISIBLE);
        mPasswordConfirmLayout.setVisibility(View.GONE);
        mLoginButton.setVisibility(View.VISIBLE);
        mRegisterButton.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mRememberMeCheckBox.setVisibility(View.VISIBLE);
        break;
      case AUTH_TYPE_REGISTER:
        mPasswordEditText.setImeOptions(EditorInfo.IME_NULL);
        mPasswordConfirmEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mCardView.setVisibility(View.VISIBLE);
        mEmailLayout.setVisibility(View.VISIBLE);
        mPasswordLayout.setVisibility(View.VISIBLE);
        mPasswordConfirmLayout.setVisibility(View.VISIBLE);
        mLoginButton.setVisibility(View.GONE);
        mRegisterButton.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mRememberMeCheckBox.setVisibility(View.GONE);
        break;
      case AUTH_TYPE_DONE:
        mCardView.setVisibility(View.GONE);
        mEmailLayout.setVisibility(View.GONE);
        mPasswordLayout.setVisibility(View.GONE);
        mPasswordConfirmLayout.setVisibility(View.GONE);
        mLoginButton.setVisibility(View.GONE);
        mRegisterButton.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mRememberMeCheckBox.setVisibility(View.GONE);
        break;
      default:
        break;
    }
  }
}
