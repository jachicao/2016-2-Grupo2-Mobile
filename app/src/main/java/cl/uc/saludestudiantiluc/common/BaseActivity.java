package cl.uc.saludestudiantiluc.common;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.RelaxUcApplication;
import cl.uc.saludestudiantiluc.auth.UserRepository;

/**
 * Created by lukas on 9/21/16.
 */

public class BaseActivity extends AppCompatActivity {

  private static final String TAG = BaseActivity.class.getSimpleName();

  private RelaxUcApplication mRelaxUcApplication;
  private Toolbar mToolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mRelaxUcApplication = (RelaxUcApplication) getApplication();
  }

  @Override
  public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().setStatusBarColor(getResources().getColor(R.color.black));
    }
    setSupportActionBar(mToolbar);
  }

  public Toolbar getToolbar() {
    return mToolbar;
  }

  public RelaxUcApplication getRelaxUcApplication() {
    return mRelaxUcApplication;
  }

  public UserRepository getUserRepository() {
    return mRelaxUcApplication.getUserRepository();
  }

}
