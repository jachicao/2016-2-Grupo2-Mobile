package cl.uc.saludestudiantiluc.settings;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import cl.uc.saludestudiantiluc.evaluations.HomeEvaluation;


public class SettingsActivity extends ActionBarActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    ft.add(android.R.id.content, new SettingsFragment());
    ft.commit();
  }

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, SettingsActivity.class);
  }

}