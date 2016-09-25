package cl.uc.saludestudiantiluc.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseFragment;

/**
 * Created by jchicao on 9/25/16.
 */

public class SignInFragment extends BaseFragment {
  private View mThisView;
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mThisView = inflater.inflate(R.layout.auth_activity, container, false);
    return mThisView;
  }
}
