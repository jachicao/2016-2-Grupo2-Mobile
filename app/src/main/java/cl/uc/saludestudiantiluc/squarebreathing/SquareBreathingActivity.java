package cl.uc.saludestudiantiluc.squarebreathing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cl.uc.saludestudiantiluc.R;

public class SquareBreathingActivity extends AppCompatActivity {

  private static final String TAG = SquareBreathingActivity.class.getSimpleName();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_square_breathing);
  }

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, SquareBreathingActivity.class);
  }
}
