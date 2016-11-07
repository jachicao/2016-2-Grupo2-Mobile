package cl.uc.saludestudiantiluc.auth;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.annotation.Annotation;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.auth.models.ErrorRegisterResponse;
import cl.uc.saludestudiantiluc.auth.models.RegisterResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;


/**
 * Created by jchicao on 11/1/16.
 */

public class RegisterFragment extends AuthFragment {
  private static final String TAG = RegisterFragment.class.getSimpleName();


  private boolean mAttemptingToRegister = false;
  private View mThisView;
  private TextInputEditText mEmailEditText;
  private TextInputEditText mPasswordEditText;
  private TextInputEditText mPasswordConfirmEditText;
  private TextInputEditText mAgeEditText;
  private Spinner mTypeSpinner;
  private Spinner mSexSpinner;
  private TextInputEditText mCareerEditText;
  private TextInputEditText mYearEditText;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mThisView = inflater.inflate(R.layout.auth_register_fragment, container, false);
    mEmailEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_email_editText);
    mPasswordEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_password);
    mPasswordConfirmEditText = (TextInputEditText) mThisView.findViewById(
        R.id.auth_register_password_confirmation);
    mAgeEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_register_age);

    mTypeSpinner = (Spinner) mThisView.findViewById(R.id.auth_register_type);
    ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getContext(),
        R.array.auth_register_type_array, android.R.layout.simple_spinner_item);
    typeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    mTypeSpinner.setAdapter(typeAdapter);


    mSexSpinner = (Spinner) mThisView.findViewById(R.id.auth_register_sex);
    ArrayAdapter<CharSequence> sexAdapter = ArrayAdapter.createFromResource(getContext(),
        R.array.auth_register_sex_array, android.R.layout.simple_spinner_item);
    sexAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    mSexSpinner.setAdapter(sexAdapter);

    mCareerEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_register_career);

    mYearEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_register_year);

    mThisView.findViewById(R.id.auth_register_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        attemptRegister();
      }
    });

    mYearEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          attemptRegister();
          return true;
        }
        return false;
      }
    });
    if (AuthActivity.AUTO_FILL_DATA) {
      mEmailEditText.setText("lezorich@uc.cl");
      mPasswordEditText.setText("123456");
      mPasswordConfirmEditText.setText("123456");
    }
    return mThisView;
  }

  private void attemptRegister() {
    if (mAttemptingToRegister) {
      return;
    }
    if (getAuthActivity().isEmailAndPasswordCorrect(mEmailEditText, mPasswordEditText) && getAuthActivity().isPasswordConfirmationCorrect(mPasswordEditText, mPasswordConfirmEditText)) {
      String email = mEmailEditText.getText().toString();
      String password = mPasswordEditText.getText().toString();
      String ageString = mAgeEditText.getText().toString();

      if (TextUtils.isEmpty(ageString)) {
        mAgeEditText.setError(getString(R.string.auth_error_field_required));
        mAgeEditText.requestFocus();
        return;
      }

      int age = Integer.parseInt(ageString);

      if (mTypeSpinner.getSelectedItemPosition() == 0) {
        getAuthActivity().showToastMessage(getString(R.string.auth_register_type_error));
        return;
      }

      String type = mTypeSpinner.getSelectedItem().toString();

      if (mSexSpinner.getSelectedItemPosition() == 0) {
        getAuthActivity().showToastMessage(getString(R.string.auth_register_sex_error));
        return;
      }

      String sex = mSexSpinner.getSelectedItem().toString();

      String career = mCareerEditText.getText().toString();
      if (TextUtils.isEmpty(career)) {
        mCareerEditText.setError(getString(R.string.auth_error_field_required));
        mCareerEditText.requestFocus();
        return;
      }

      String yearString = mYearEditText.getText().toString();
      if (TextUtils.isEmpty(yearString)) {
        mYearEditText.setError(getString(R.string.auth_error_field_required));
        mYearEditText.requestFocus();
        return;
      }

      int year = Integer.parseInt(yearString);

      mThisView.setVisibility(View.INVISIBLE);
      getAuthActivity().getProgressBar().setVisibility(View.VISIBLE);

      Call<RegisterResponse> callInstance = getAuthActivity().getApiInstance().register(email, password, password, age, type, sex, career, year);
      mAttemptingToRegister = true;
      callInstance.enqueue(new Callback<RegisterResponse>() {
        @Override
        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
          mAttemptingToRegister = false;
          getAuthActivity().getProgressBar().setVisibility(View.GONE);
          if (response.isSuccessful()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getAuthActivity());
            builder.setMessage(getString(R.string.auth_confirmation_email))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.auth_confirmation_ok), new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    getAuthActivity().setNewFragment(AuthActivity.AUTH_TYPE_OPTIONS);
                  }
                });
            AlertDialog alert = builder.create();
            alert.show();
          } else {
            Converter<ResponseBody, ErrorRegisterResponse> errorConverter =
                getAuthActivity().getRetrofitInstance().responseBodyConverter(ErrorRegisterResponse.class, new Annotation[0]);
            boolean unknownError = true;
            try {
              ErrorRegisterResponse error = errorConverter.convert(response.errorBody());
              if (error != null && error.errors != null && error.errors.full_messages != null && error.errors.full_messages.size() > 0) {
                // TODO: Show spanish errors
                getBaseActivity().showToastMessage(error.errors.full_messages.get(0));
                mThisView.setVisibility(View.VISIBLE);
                unknownError = false;
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
            Log.e(TAG, response.message());
            if (unknownError) {
              getBaseActivity().showToastMessage(getString(R.string.unsuccessful_error));
              getAuthActivity().setNewFragment(AuthActivity.AUTH_TYPE_OPTIONS);
            }
          }
        }

        @Override
        public void onFailure(Call<RegisterResponse> call, Throwable t) {
          mAttemptingToRegister = false;
          Log.e(TAG, t.getMessage());
          getBaseActivity().showToastMessage(getString(R.string.unsuccessful_error));
          getAuthActivity().setNewFragment(AuthActivity.AUTH_TYPE_OPTIONS);
        }
      });
    }
  }
}
