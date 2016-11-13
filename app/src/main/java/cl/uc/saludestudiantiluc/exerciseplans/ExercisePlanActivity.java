package cl.uc.saludestudiantiluc.exerciseplans;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.common.SoundServiceActivity;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSound;
import cl.uc.saludestudiantiluc.services.download.DownloadService;
import me.relex.circleindicator.CircleIndicator;

public class ExercisePlanActivity extends SoundServiceActivity {

  public static final String TAG = ExercisePlanActivity.class.getSimpleName();

  private MediaPlayer mMediaPlayer;
  private ExerciseSound mExerciseSound;
  private boolean mIsPlaying;
  private int mLastPageSelected = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exercise_sound);
    Glide
        .with(this)
        .load(R.drawable.main_background)
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .centerCrop()
        .into((ImageView) findViewById(R.id.main_background_image));
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    mExerciseSound = extras.getParcelable(ExercisePlanMenu.EXERCISE_EXTRAS_INDEX);
    setNewSound(mExerciseSound);
    mIsPlaying = false;

    ImageButton playButton = (ImageButton) findViewById(R.id.playExerciseSoundButton);
    playButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mIsPlaying) {
          mMediaPlayer.pause();
          mIsPlaying = false;
        } else {
          mMediaPlayer.start();
          mIsPlaying = true;
        }
      }
    });
  }

  public void setNewSound(ExerciseSound exerciseSound) {
    if (mSoundService != null) {
      mSoundService.newSound(DownloadService.getStringDir(this, exerciseSound.getSoundRequest()),
          exerciseSound.name, isImmersiveMode(), 0);
    }
  }
}
