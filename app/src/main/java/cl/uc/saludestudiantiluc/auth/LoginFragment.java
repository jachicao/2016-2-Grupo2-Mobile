package cl.uc.saludestudiantiluc.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.IOException;
import java.lang.annotation.Annotation;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.auth.models.ErrorResponse;
import cl.uc.saludestudiantiluc.auth.models.LoginResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by jchicao on 11/1/16.
 */

public class LoginFragment extends AuthFragment {

  private static final String TAG = LoginFragment.class.getSimpleName();
  private boolean mAttemptingToLogin = false;
  private View mThisView;
  private TextInputEditText mEmailEditText;
  private TextInputEditText mPasswordEditText;
  private CheckBox mRememberMeCheckBox;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    mThisView = inflater.inflate(R.layout.auth_login_fragment, container, false);

    mEmailEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_email_editText);
    mPasswordEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_password);
    mRememberMeCheckBox = (CheckBox) mThisView.findViewById(R.id.auth_remember_me);
    mPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          attemptLogin();
          return true;
        }
        return false;
      }
    });
    Button loginButton = (Button) mThisView.findViewById(R.id.auth_login_button);
    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        attemptLogin();
      }
    });
    return mThisView;
  }


  private void attemptLogin() {
    if (mAttemptingToLogin) {
      return;
    }
    mEmailEditText.setError(null);
    mPasswordEditText.setError(null);
    if (getAuthActivity().isEmailAndPasswordCorrect(mEmailEditText, mPasswordEditText)) {

      String email = mEmailEditText.getText().toString();
      String password = mPasswordEditText.getText().toString();

      mThisView.setVisibility(View.INVISIBLE);
      getAuthActivity().getProgressBar().setVisibility(View.VISIBLE);

      Call<LoginResponse> callInstance = getAuthActivity().getApiInstance().logIn(email, password);
      mAttemptingToLogin = true;
      callInstance.enqueue(new Callback<LoginResponse>() {
        @Override
        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
          mAttemptingToLogin = false;
          LoginResponse loginResponse = response.body();
          getAuthActivity().getProgressBar().setVisibility(View.GONE);
          if (response.isSuccessful()) {
            final String accessToken = response.headers().get("access-token");
            final String accessTokenClient = response.headers().get("client");
            final String uid = response.headers().get("uid");
            getAuthActivity().getUserRepository().storeAccessToken(accessToken);
            getAuthActivity().getUserRepository().storeAccessTokenClient(accessTokenClient);
            getAuthActivity().getUserRepository().storeUid(uid);
            getAuthActivity().getUserRepository().storeUserEmail(loginResponse.getEmail());
            getAuthActivity().getUserRepository().storeName(loginResponse.getName());
            getAuthActivity().getUserRepository().storeAcademicType(loginResponse.getAcademicType());
            Log.d(TAG, loginResponse.getEmail());
            getAuthActivity().onUserLoggedIn();
          } else {
            Converter<ResponseBody, ErrorResponse> errorConverter =
                getAuthActivity().getRetrofitInstance().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
            boolean unknownError = true;
            try {
              ErrorResponse error = errorConverter.convert(response.errorBody());
              if (error != null && error.errors != null && error.errors.size() > 0) {
                getBaseActivity().showToastMessage(error.errors.get(0));
                mThisView.setVisibility(View.VISIBLE);
                unknownError = false;
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
            if (unknownError) {
              getBaseActivity().showToastMessage(getString(R.string.unsuccessful_error));
              getAuthActivity().setNewFragment(AuthActivity.AUTH_TYPE_OPTIONS);
            }
          }
        }

        @Override
        public void onFailure(Call<LoginResponse> call, Throwable t) {
          mAttemptingToLogin = false;
          Log.e(TAG, t.getMessage());
          getBaseActivity().showToastMessage(getString(R.string.unsuccessful_error));
          getAuthActivity().setNewFragment(AuthActivity.AUTH_TYPE_OPTIONS);
        }
      });
    }
  }
}
