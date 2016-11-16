package cl.uc.saludestudiantiluc.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cl.uc.saludestudiantiluc.R;

/**
 * Created by jchicao on 11/1/16.
 */

public class OptionsFragment extends AuthFragment {

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.auth_options_fragment, container, false);
    view.findViewById(R.id.auth_login_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getAuthActivity().setNewFragment(AuthActivity.AUTH_TYPE_LOGIN);
      }
    });
    view.findViewById(R.id.auth_register_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getAuthActivity().setNewFragment(AuthActivity.AUTH_TYPE_REGISTER);
      }
    });
    return view;
  }
}
