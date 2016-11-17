package cl.uc.saludestudiantiluc.breathingexcercises.guidedbreathing;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;

public class GuidedBreathingActivity extends BaseActivity {

  private static final TimeInterpolator INHALE_INTERPOLATOR = new DecelerateInterpolator();
  private static final TimeInterpolator EXHALE_INTERPOLATOR = new AccelerateInterpolator();

  private static final int INHALE_TIME_IN_MILLIS = 3000;
  private static final int EXHALE_TIME_IN_MILLIS = 6000;

  private float mInhaleScaleX;
  private float mInhaleScaleY;

  private int mBalloonHeight;
  private int mBalloonWidth;

  private View mBalloonView;
  private TextView mActionTextView;

  private Runnable mExhaleRunnable = new Runnable() {
    @Override
    public void run() {
      startExhaleAnimation();
    }
  };

  private Runnable mInhaleRunnable = new Runnable() {
    @Override
    public void run() {
      startInhaleAnimation();
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_guided_breathing);
    mBalloonView = findViewById(R.id.guided_breathing_icon);
    mActionTextView = (TextView) findViewById(R.id.guided_breathing_action_text);

    final Resources resources = getResources();
    int balloonMaxWidth = resources.getDimensionPixelSize(R.dimen.balloon_max_width);
    int balloonMaxHeight = resources.getDimensionPixelSize(R.dimen.balloon_max_height);
    mBalloonHeight = resources.getDimensionPixelSize(R.dimen.balloon_height);
    mBalloonWidth = resources.getDimensionPixelSize(R.dimen.balloon_width);

    mInhaleScaleX = ((float) balloonMaxWidth) / mBalloonWidth;
    mInhaleScaleY = ((float) balloonMaxHeight) / mBalloonHeight;

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

  @Override
  protected void onResume() {
    super.onResume();
    startInhaleAnimation();
  }

  @Override
  protected void onPause() {
    mBalloonView.clearAnimation();
    super.onPause();
  }

  private void startInhaleAnimation() {
    mBalloonView.animate()
        .scaleX(mInhaleScaleX)
        .scaleY(mInhaleScaleY)
        .setInterpolator(INHALE_INTERPOLATOR)
        .setDuration(INHALE_TIME_IN_MILLIS)
        .withStartAction(new Runnable() {
          @Override
          public void run() {
            mActionTextView.setText(R.string.inhale);
          }
        })
        .withEndAction(mExhaleRunnable)
        .start();
  }

  private void startExhaleAnimation() {
    mBalloonView.animate()
        .scaleX(1.0f)
        .scaleY(1.0f)
        .setInterpolator(EXHALE_INTERPOLATOR)
        .setDuration(EXHALE_TIME_IN_MILLIS)
        .withStartAction(new Runnable() {
          @Override
          public void run() {
            mActionTextView.setText(R.string.exhale);
          }
        })
        .withEndAction(mInhaleRunnable)
        .start();
  }

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, GuidedBreathingActivity.class);
  }
}
