package cl.uc.saludestudiantiluc.breathingexcercises.squarebreathing;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import cl.uc.saludestudiantiluc.R;

/**
 * Created by lukas on 9/11/16.
 */
public class SquareBreathingView extends FrameLayout {

  private static final String TAG = SquareBreathingActivity.class.getSimpleName();

  // Four actions with four steps
  private static final int VIEWS_PER_ACTION = 4;

  private static final int NUMBER_OF_ACTIONS = 4;
  private static final int NUMBER_OF_CIRCLES = VIEWS_PER_ACTION * NUMBER_OF_ACTIONS;

  private static final int INHALE_LAST_VIEW_INDEX = VIEWS_PER_ACTION - 1;
  private static final int HOLD_AIR_LAST_VIEW_INDEX = VIEWS_PER_ACTION * 2 - 1;
  private static final int EXHALE_LAST_VIEW_INDEX = VIEWS_PER_ACTION * 3 - 1;

  private static final long HIGHLIGHT_COLOUR_ANIMATION_TIME_IN_MILLIS = 6000;

  // One step per second
  private static final long STEP_TIME_IN_MILLIS = 1000;

  private final int mHighlightColour;
  private final int mCircleColour;

  private View[] mInhaleViews;
  private View[] mHoldAirViews;
  private View[] mExhaleViews;
  private View[] mHoldWithoutViews;
  private TextView mActionTextView;

  private int mCurrentCircle;

  // Is responsible for changing the circle colour
  private Handler mHandler;
  private Runnable mHighlightCircleRunnable = new Runnable() {
    @Override
    public void run() {
      if (mCurrentCircle >= NUMBER_OF_CIRCLES) {
        mCurrentCircle = 0;
      }

      changeActionText();
      highlightCircle();
      mCurrentCircle++;
      mHandler.postDelayed(this, STEP_TIME_IN_MILLIS);
    }
  };

  public SquareBreathingView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SquareBreathingView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    mHighlightColour = context.getResources().getColor(R.color.squareBreathingHighlight);
    mCircleColour = context.getResources().getColor(android.R.color.white);

    LayoutInflater.from(context).inflate(R.layout.exercise_square_breathing_view, this, true);

    mInhaleViews = new View[VIEWS_PER_ACTION];
    mHoldAirViews = new View[VIEWS_PER_ACTION];
    mExhaleViews = new View[VIEWS_PER_ACTION];
    mHoldWithoutViews = new View[VIEWS_PER_ACTION];

    mInhaleViews[0] = findViewById(R.id.square_breathing_inhale_first);
    mInhaleViews[1] = findViewById(R.id.square_breathing_inhale_second);
    mInhaleViews[2] = findViewById(R.id.square_breathing_inhale_third);
    mInhaleViews[3] = findViewById(R.id.square_breathing_inhale_fourth);

    mExhaleViews[0] = findViewById(R.id.square_breathing_exhale_first);
    mExhaleViews[1] = findViewById(R.id.square_breathing_exhale_second);
    mExhaleViews[2] = findViewById(R.id.square_breathing_exhale_third);
    mExhaleViews[3] = findViewById(R.id.square_breathing_exhale_fourth);

    mHoldAirViews[0] = findViewById(R.id.square_breathing_hold_air_first);
    mHoldAirViews[1] = findViewById(R.id.square_breathing_hold_air_second);
    mHoldAirViews[2] = findViewById(R.id.square_breathing_hold_air_third);
    mHoldAirViews[3] = findViewById(R.id.square_breathing_hold_air_fourth);

    mHoldWithoutViews[0] = findViewById(R.id.square_breathing_hold_without_first);
    mHoldWithoutViews[1] = findViewById(R.id.square_breathing_hold_without_second);
    mHoldWithoutViews[2] = findViewById(R.id.square_breathing_hold_without_third);
    mHoldWithoutViews[3] = findViewById(R.id.square_breathing_hold_without_fourth);

    mActionTextView = (TextView) findViewById(R.id.square_breathing_action_textview);

    mHandler = new Handler();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public SquareBreathingView(Context context, AttributeSet attrs, int defStyleAttr,
                             int defStyleRes) {
    // Ignore defStyleRes
    this(context, attrs, defStyleAttr);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);

    stopAnimation();
    restoreInitialState();
    startAnimation();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    stopAnimation();
  }

  private void startAnimation() {
    mHandler.post(mHighlightCircleRunnable);
  }

  private void stopAnimation() {
    mHandler.removeCallbacksAndMessages(null);
  }

  private void restoreInitialState() {
    resetColors(mInhaleViews);
    resetColors(mExhaleViews);
    resetColors(mHoldAirViews);
    resetColors(mHoldWithoutViews);
    mCurrentCircle = 0;
  }

  private void resetColors(View[] views) {
    for (View view : views) {
      view.getBackground().setColorFilter(mCircleColour, PorterDuff.Mode.SRC_ATOP);
    }
  }

  private void changeActionText() {
    if (mCurrentCircle <= INHALE_LAST_VIEW_INDEX) {
      mActionTextView.setText(R.string.inhale);
    } else if (mCurrentCircle <= HOLD_AIR_LAST_VIEW_INDEX) {
      mActionTextView.setText(R.string.square_breathing_hold);
    } else if (mCurrentCircle <= EXHALE_LAST_VIEW_INDEX) {
      mActionTextView.setText(R.string.exhale);
    } else {
      mActionTextView.setText(R.string.square_breathing_hold);
    }
  }

  private void highlightCircle() {
    View circleView;
    if (mCurrentCircle > EXHALE_LAST_VIEW_INDEX) {
      circleView = mHoldWithoutViews[mCurrentCircle % VIEWS_PER_ACTION];
    } else if (mCurrentCircle > HOLD_AIR_LAST_VIEW_INDEX) {
      circleView = mExhaleViews[mCurrentCircle % VIEWS_PER_ACTION];
    } else if (mCurrentCircle > INHALE_LAST_VIEW_INDEX) {
      circleView = mHoldAirViews[mCurrentCircle % VIEWS_PER_ACTION];
    } else {
      circleView = mInhaleViews[mCurrentCircle % VIEWS_PER_ACTION];
    }
    highlightCircle(circleView);
  }

  private void highlightCircle(View circleView) {
    circleView.getBackground()
        .setColorFilter(mHighlightColour, PorterDuff.Mode.SRC_ATOP);
    ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), mHighlightColour,
        mCircleColour);
    final GradientDrawable background = (GradientDrawable) circleView.getBackground();
    colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        background.setColorFilter((Integer) valueAnimator.getAnimatedValue(),
            PorterDuff.Mode.SRC_ATOP);
      }
    });
    colorAnimator.setDuration(HIGHLIGHT_COLOUR_ANIMATION_TIME_IN_MILLIS);
    colorAnimator.start();
  }
}
