package cl.uc.saludestudiantiluc.settings;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.evaluations.HomeEvaluation;


public class SettingsActivity extends BaseActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setToolBar
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.evaluetion_results);
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