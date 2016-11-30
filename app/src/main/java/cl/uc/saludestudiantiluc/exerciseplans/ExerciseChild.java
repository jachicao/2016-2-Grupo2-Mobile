package cl.uc.saludestudiantiluc.exerciseplans;

import java.util.Date;

import cl.uc.saludestudiantiluc.exerciseplans.models.ExercisePlan;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSound;
import cl.uc.saludestudiantiluc.exerciseplans.models.ExerciseSoundData;

/**
 * Created by camilo on 06-11-16.
 */

public class ExerciseChild {
  private ExerciseSound mSoundData;
  private ExercisePlan mExercisePlan;

  public ExerciseChild(ExerciseSound soundData, ExercisePlan plan) {
    mSoundData = soundData;
    mExercisePlan = plan;
  }


  public ExerciseSoundData getSoundData() {
    return mSoundData.getExerciseSoundData();
  }

  public int getOrder() {
    return mSoundData.getOrder();
  }

  public ExercisePlan getPlan() {
    return mExercisePlan;
  }
}
