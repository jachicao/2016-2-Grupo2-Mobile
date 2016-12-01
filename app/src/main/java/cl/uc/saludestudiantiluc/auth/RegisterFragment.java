package cl.uc.saludestudiantiluc.auth;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

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
  private TextInputEditText mRutEditText;
  private TextInputEditText mAgeEditText;
  private TextInputEditText mNameEditText;
  private Spinner mAcademicType;
  private Spinner mSexSpinner;
  private Spinner mCareerSpinner;
  private TextInputEditText mYearEditText;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mThisView = inflater.inflate(R.layout.auth_register_fragment, container, false);
    mEmailEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_email_editText);
    mPasswordEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_password);
    mPasswordConfirmEditText = (TextInputEditText) mThisView.findViewById(
        R.id.auth_register_password_confirmation);
    mRutEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_register_rut);
    mAgeEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_register_age);
    mNameEditText = (TextInputEditText) mThisView.findViewById(R.id.auth_register_name);

    mAcademicType = (Spinner) mThisView.findViewById(R.id.auth_register_academic_type);
    ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getContext(),
        R.array.auth_register_academic_type_array, android.R.layout.simple_spinner_item);
    typeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    mAcademicType.setAdapter(typeAdapter);

    mCareerSpinner = (Spinner) mThisView.findViewById(R.id.auth_register_career);
    ArrayAdapter<CharSequence> careerAdapter = ArrayAdapter.createFromResource(getContext(),
        R.array.auth_register_career_array, android.R.layout.simple_spinner_item);
    careerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    mCareerSpinner.setAdapter(careerAdapter);

    mSexSpinner = (Spinner) mThisView.findViewById(R.id.auth_register_sex);
    ArrayAdapter<CharSequence> sexAdapter = ArrayAdapter.createFromResource(getContext(),
        R.array.auth_register_sex_array, android.R.layout.simple_spinner_item);
    sexAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    mSexSpinner.setAdapter(sexAdapter);

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
    mEmailEditText.setError(null);
    mPasswordEditText.setError(null);
    mPasswordConfirmEditText.setError(null);
    mRutEditText.setError(null);
    mAgeEditText.setError(null);
    mYearEditText.setError(null);
    mNameEditText.setError(null);
    if (getAuthActivity().isEmailAndPasswordCorrect(mEmailEditText, mPasswordEditText) && getAuthActivity().isPasswordConfirmationCorrect(mPasswordEditText, mPasswordConfirmEditText)) {

      String email = mEmailEditText.getText().toString();

      String password = mPasswordEditText.getText().toString();

      String name = mNameEditText.getText().toString();
      if (TextUtils.isEmpty(name)) {
        mNameEditText.setError(getString(R.string.auth_error_field_required));
        mNameEditText.requestFocus();
        return;
      }

      String rut = mRutEditText.getText().toString();
      if (TextUtils.isEmpty(rut)) {
        mRutEditText.setError(getString(R.string.auth_error_field_required));
        mRutEditText.requestFocus();
        return;
      }
      if (!getAuthActivity().isRutValid(rut)) {
        mRutEditText.setError(getString(R.string.auth_register_rut_error));
        mRutEditText.requestFocus();
        return;
      }

      String ageString = mAgeEditText.getText().toString();

      if (TextUtils.isEmpty(ageString)) {
        mAgeEditText.setError(getString(R.string.auth_error_field_required));
        mAgeEditText.requestFocus();
        return;
      }

      int age = Integer.parseInt(ageString);

      if (mAcademicType.getSelectedItemPosition() == 0) {
        getAuthActivity().showToastMessage(getString(R.string.auth_register_academic_type_error));
        return;
      }

      String type = mAcademicType.getSelectedItem().toString();

      if (mSexSpinner.getSelectedItemPosition() == 0) {
        getAuthActivity().showToastMessage(getString(R.string.auth_register_sex_error));
        return;
      }

      String sex = mSexSpinner.getSelectedItem().toString();

      if (mCareerSpinner.getSelectedItemPosition() == 0) {
        getAuthActivity().showToastMessage(getString(R.string.auth_register_career_error));
        return;
      }

      String career = mCareerSpinner.getSelectedItem().toString();

      String yearString = mYearEditText.getText().toString();
      if (TextUtils.isEmpty(yearString)) {
        mYearEditText.setError(getString(R.string.auth_error_field_required));
        mYearEditText.requestFocus();
        return;
      }

      int year = Integer.parseInt(yearString);

      mThisView.setVisibility(View.INVISIBLE);
      getAuthActivity().getProgressBar().setVisibility(View.VISIBLE);

      Call<RegisterResponse> callInstance = getAuthActivity().getApiInstance().register(email, password, password, name, rut, age, type, sex, career, year);
      mAttemptingToRegister = true;
      callInstance.enqueue(new Callback<RegisterResponse>() {
        @Override
        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
          mAttemptingToRegister = false;
          getAuthActivity().getProgressBar().setVisibility(View.GONE);
          if (response.isSuccessful()) {
            new AlertDialog.Builder(getAuthActivity()).setMessage(getString(R.string.auth_confirmation_email))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.auth_confirmation_ok), new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    getAuthActivity().setNewFragment(AuthActivity.AUTH_TYPE_OPTIONS);
                  }
                }).create().show();
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
