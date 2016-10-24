package cl.uc.saludestudiantiluc.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;

public class ProfileActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.profile_activity);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.profile);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });

    loadMainBackground();
  }
  public static Intent getIntent(Activity activity) {
    return new Intent(activity, ProfileActivity.class);
  }
}
