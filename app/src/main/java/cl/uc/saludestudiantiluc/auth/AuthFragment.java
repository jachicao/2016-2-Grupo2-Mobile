package cl.uc.saludestudiantiluc.auth;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AuthFragment extends BaseFragment {
  private AuthListener mListener;
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      Activity activity = (Activity) context;
      mListener = (AuthListener) activity;
    } catch (ClassCastException e) {

    }
  }

  public static final boolean DEVELOPER_MODE = true;
  // UI references.
  private View mThisView;
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

  static final int AUTH_TYPE_OPTIONS = 0;
  static final int AUTH_TYPE_CONNECTING = 1;
  static final int AUTH_TYPE_LOGIN = 2;
  static final int AUTH_TYPE_REGISTER = 3;
  static final int AUTH_TYPE_DONE = 4;

  private SharedPreferences getThisSharedPreferences() {
    return getActivity().getSharedPreferences(getString(R.string.auth_shared_preferences), Context.MODE_PRIVATE);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mThisView = inflater.inflate(R.layout.auth_fragment, container, false);
    mCardView = mThisView.findViewById(R.id.auth_card_view);
    mEmailLayout = mThisView.findViewById(R.id.auth_email_layout);
    mEmailEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_email_editText);
    mPasswordLayout = mThisView.findViewById(R.id.auth_password_layout);
    mPasswordEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_password);
    mProgressBar = mThisView.findViewById(R.id.auth_progress_bar);
    mPasswordConfirmEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_register_password_confirmation);
    mPasswordConfirmLayout = mThisView.findViewById(R.id.auth_register_password_confirmation_layout);
    mLoginButton = (Button) mThisView.findViewById(R.id.auth_sign_in_button);
    mRegisterButton = (Button) mThisView.findViewById(R.id.auth_register_button);
    mRememberMeCheckBox = (CheckBox) mThisView.findViewById(R.id.auth_remember_me);

    //Set everything as gone
    mCardView.setVisibility(View.GONE);
    mEmailLayout.setVisibility(View.GONE);
    mPasswordLayout.setVisibility(View.GONE);
    mPasswordConfirmLayout.setVisibility(View.GONE);
    mLoginButton.setVisibility(View.GONE);
    mRegisterButton.setVisibility(View.GONE);
    mProgressBar.setVisibility(View.GONE);
    mRememberMeCheckBox.setVisibility(View.GONE);

    if (DEVELOPER_MODE) {
      mEmailEditText.setText("email@uc.cl");
      mPasswordEditText.setText("password");
      mPasswordConfirmEditText.setText("password");
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
    String rememberMe = getThisSharedPreferences().getString(getString(R.string.auth_shared_preferences_remember_me), null);
    if (rememberMe != null && rememberMe.equals("true")) {
      HeaderResponse previousHeaderResponse = HeaderResponse.getPreviousInstance(getActivity(), getThisSharedPreferences());
      if (previousHeaderResponse != null) {
        startMainActivity();
        return mThisView;
      }
    }
    show(AUTH_TYPE_OPTIONS);
    return mThisView;
  }


  private void startMainActivity() {
    if (mListener != null) {
      mListener.onSignedIn(DataResponse.getPreviousInstance(getActivity(), getThisSharedPreferences()));
    }
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
          res.setHeaderResponse(response.headers());
          if (res.data.errors.full_messages.size() > 0) {
            String error = "";
            for (String e : res.data.errors.full_messages) {
              error += e;
            }
            show(AUTH_TYPE_OPTIONS);
            Snackbar.make(mThisView.findViewById(R.id.auth_coordinator_layout), error, Snackbar.LENGTH_LONG).show();
          } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
          Snackbar.make(mThisView.findViewById(R.id.auth_coordinator_layout), t.getMessage(), Snackbar.LENGTH_LONG).show();
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
          res.setHeaderResponse(response.headers());
          if (res.errors.size() > 0) {
            String error = "";
            for (String e : res.errors) {
              error += e;
            }
            Snackbar.make(mThisView.findViewById(R.id.auth_coordinator_layout), error, Snackbar.LENGTH_LONG).show();
            show(AUTH_TYPE_OPTIONS);
          } else {
            SharedPreferences.Editor editor = getThisSharedPreferences().edit();
            res.header.store(getActivity(), editor);
            res.data.store(getActivity(), editor);
            editor.putString(getString(R.string.auth_shared_preferences_remember_me), mRememberMeCheckBox.isChecked() ? "true" : "false");
            editor.commit();
            show(AUTH_TYPE_DONE);
            startMainActivity();
          }
        }

        @Override
        public void onFailure(Call<LoginResponse> call, Throwable t) {
          mAttemptingToLogin = false;
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

  @Override
  public void onBackPressed() {
    if (mLastTypeSet == AUTH_TYPE_REGISTER || mLastTypeSet == AUTH_TYPE_LOGIN) {
      show(AUTH_TYPE_OPTIONS);
    }
  }
}
