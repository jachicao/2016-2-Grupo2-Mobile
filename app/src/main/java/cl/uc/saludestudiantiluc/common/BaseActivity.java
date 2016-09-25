package cl.uc.saludestudiantiluc.common;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cl.uc.saludestudiantiluc.R;

/**
 * Created by lukas on 9/21/16.
 */

public class BaseActivity extends AppCompatActivity {

  private Toolbar mToolbar;

  @Override
  public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);
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
}
