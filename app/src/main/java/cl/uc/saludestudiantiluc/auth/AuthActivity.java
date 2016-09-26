package cl.uc.saludestudiantiluc.auth;

import android.os.Bundle;

import cl.uc.saludestudiantiluc.MainActivity;
import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;

public class AuthActivity extends BaseActivity implements AuthFragment.AuthListener {

  private static final String TAG = AuthActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // If the user is logged in, go to main activity. If he is not, then show the auth fragment.
    if (getUserRepository().isUserLoggedIn()) {
      startActivity(MainActivity.getIntent(this));
      finish();
    }

    setContentView(R.layout.activity_auth);
    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.fragment_container, AuthFragment.newInstance())
        .commit();
  }


  @Override
  public void onUserLoggedIn() {
    startActivity(MainActivity.getIntent(this));
    finish();
  }
}
