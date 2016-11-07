package cl.uc.saludestudiantiluc.auth;

import cl.uc.saludestudiantiluc.common.BaseFragment;

/**
 * Created by jchicao on 11/1/16.
 */

public class AuthFragment extends BaseFragment {
  public AuthActivity getAuthActivity() {
    return (AuthActivity) getActivity();
  }
}
