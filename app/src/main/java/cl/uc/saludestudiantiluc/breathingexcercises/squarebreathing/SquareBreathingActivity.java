package cl.uc.saludestudiantiluc.breathingexcercises.squarebreathing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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

  @Override
  protected void loadMainBackground() {
    Glide
        .with(this)
        .load(R.drawable.excercises_background)
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .centerCrop()
        .into((ImageView) findViewById(R.id.main_background_image));
  }

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, SquareBreathingActivity.class);
  }
}
