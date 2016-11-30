package cl.uc.saludestudiantiluc.exerciseplans;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import java.util.concurrent.TimeUnit;

import cl.uc.saludestudiantiluc.MainActivity;
import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.SoundServiceActivity;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSoundData;
import cl.uc.saludestudiantiluc.services.download.DownloadService;

public class ExercisePlanActivity extends SoundServiceActivity {

  public static final String TAG = ExercisePlanActivity.class.getSimpleName();
  public static final String PLAYING = "PlayPause";
  public static final String PLAN_ID = "plan_id";
  public static final String ORDER = "plan_order";

  private ExerciseSoundData mExerciseSound;
  private int mExercisePlanId;
  private boolean mIsPlaying;
  private int mOrder;
  private ImageButton mPlayButton;
  private ImageButton mStopButton;
  private ProgressWheel pw;
  private Handler mHandler = new Handler();
  private Runnable mRunnable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exercise_sound);
    loadMainBackground();

    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle("");
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });

    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    mExerciseSound = extras.getParcelable(ExercisePlanMenu.EXERCISE_EXTRAS_SOUND);
    mExercisePlanId = extras.getInt(ExercisePlanMenu.EXERCISE_EXTRAS_PLAN);
    mOrder = extras.getInt(ExercisePlanMenu.EXERCISE_EXTRAS_ORDER);
    mIsPlaying = true;
    if (savedInstanceState != null) {
      mExerciseSound = savedInstanceState.getParcelable(TAG);
      mIsPlaying = savedInstanceState.getBoolean(PLAYING);
      mExercisePlanId = savedInstanceState.getInt(PLAN_ID);
      mOrder = savedInstanceState.getInt(ORDER);
    }
    pw = (ProgressWheel) findViewById(R.id.pw_spinner);
    pw.stopSpinning();
    pw.setProgress(0);

    mRunnable = new Runnable() {
      @Override
      public void run() {
        pw.setProgress(getSoundService().getMediaPlayer().getCurrentPosition() * 360 / getSoundService().getMediaPlayer().getDuration());
        int current = getSoundService().getMediaPlayer().getDuration() - getSoundService().getMediaPlayer().getCurrentPosition();
        pw.setText(String.format("%d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(current),
            TimeUnit.MILLISECONDS.toSeconds(current) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(current))
        ));
        if (current < 1500){
          postListenedExercise();
        }
        mHandler.postDelayed(this, 1000);
      }
    };
    mPlayButton = (ImageButton) findViewById(R.id.playExerciseSoundButton);
    mStopButton = (ImageButton) findViewById(R.id.stopExerciseSoundButton);
    if (mIsPlaying) {
      mPlayButton.setImageResource(R.drawable.ic_pause_black_24dp);
    } else {
      mPlayButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

    mPlayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mIsPlaying) {
          getSoundService().pauseSound();
          mPlayButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
          mIsPlaying = false;
        } else {
          getSoundService().startSound();
          mPlayButton.setImageResource(R.drawable.ic_pause_black_24dp);
          mIsPlaying = true;
        }
      }
    });

    mStopButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (getSoundService() != null) {
          mIsPlaying = false;
          getSoundService().getMediaPlayer().pause();
          getSoundService().getMediaPlayer().seekTo(0);
          getSoundService().pauseSound();
          mPlayButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
      }
    });
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    if (mExerciseSound != null) {
      outState.putParcelable(TAG, mExerciseSound);
      outState.putBoolean(PLAYING, mIsPlaying);
      outState.putInt(PLAN_ID, mExercisePlanId);
      outState.putInt(ORDER, mOrder);
    }
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mHandler.removeCallbacks(mRunnable);
  }

  @Override
  protected void onDestroy() {
    super.onPause();
    mHandler.removeCallbacks(mRunnable);
  }

  @Override
  public void onServiceConnected(ComponentName name, IBinder service) {
    super.onServiceConnected(name, service);
    if (getSoundService() != null) {
      setMediaPlayerSound(DownloadService.getStringDir(this, mExerciseSound.getSoundRequest()), mExerciseSound.getName(), true, 0, false);
      if (mIsPlaying) {
        mPlayButton.setImageResource(R.drawable.ic_pause_black_24dp);
      } else {
        mPlayButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        getSoundService().pauseSound();
      }
      mHandler.postDelayed(mRunnable, 1000);
    }
  }

  public void postListenedExercise() {
    mHandler.removeCallbacks(mRunnable);
    getPostService().sendExerciseListened(this, mExercisePlanId, mOrder);
    getSoundService().getMediaPlayer().pause();
    getSoundService().getMediaPlayer().seekTo(0);
    getSoundService().pauseSound();
    mIsPlaying = false;
    Intent intent = new Intent(ExercisePlanActivity.this, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
  }


}
