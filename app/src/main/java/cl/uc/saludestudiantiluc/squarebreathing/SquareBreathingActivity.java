package cl.uc.saludestudiantiluc.squarebreathing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;

public class SquareBreathingActivity extends BaseActivity {

  private static final String TAG = SquareBreathingActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_square_breathing_excercise);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.square_breathing);
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
    return new Intent(activity, SquareBreathingActivity.class);
  }
}
