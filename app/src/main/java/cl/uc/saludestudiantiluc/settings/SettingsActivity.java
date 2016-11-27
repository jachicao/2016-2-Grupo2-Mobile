package cl.uc.saludestudiantiluc.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;


public class SettingsActivity extends BaseActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setToolBar
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.evaluation_results);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }


    FragmentTransaction ft = getFragmentManager().beginTransaction();
    ft.add(android.R.id.content, new SettingsFragment());
    ft.commit();
  }



  public static Intent getIntent(Activity activity) {
    return new Intent(activity, SettingsActivity.class);
  }

}