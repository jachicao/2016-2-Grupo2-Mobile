package cl.uc.saludestudiantiluc.exerciseplans;

import java.util.Date;

import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSoundData;

/**
 * Created by camilo on 06-11-16.
 */

public class ExerciseChild {
  private boolean mUnlocked;
  private ExerciseSoundData mSoundData;

  public ExerciseChild(boolean solved, ExerciseSoundData soundData) {
    mUnlocked = solved;
    mSoundData = soundData;
  }


  public boolean isUnlocked() {
    return mUnlocked;
  }

  public ExerciseSoundData getSoundData() {
    return mSoundData;
  }
}
