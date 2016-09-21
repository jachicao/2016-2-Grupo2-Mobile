package cl.uc.saludestudiantiluc.squarebreathing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.TranslucentActivity;

public class SquareBreathingActivity extends TranslucentActivity {

  private static final String TAG = SquareBreathingActivity.class.getSimpleName();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_square_breathing);

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
  }

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, SquareBreathingActivity.class);
  }
}
