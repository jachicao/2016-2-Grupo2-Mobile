package cl.uc.saludestudiantiluc.services.post.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import cl.uc.saludestudiantiluc.RelaxUcApplication;
import cl.uc.saludestudiantiluc.exerciseplans.api.ExerciseProgramApi;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by camilo on 29-11-16.
 */

public class ExerciseJob extends Job {

  private static final String TAG = ExerciseJob.class.getSimpleName();
  private int mPlanId;
  private int mExerciseId;

  public ExerciseJob(int planId, int exerciseId) {
    super(new Params(0).requireNetwork().persist());
    mPlanId = planId;
    mExerciseId = exerciseId;
  }

  @Override
  public void onAdded() {

  }

  @Override
  public void onRun() throws Throwable {
    RelaxUcApplication relaxUcApplication = (RelaxUcApplication) getApplicationContext();
    if (relaxUcApplication == null) {
      throw new Exception("null Application");
    }
    ExerciseProgramApi exerciseProgramApi = relaxUcApplication.getExerciseSoundService();
    if (exerciseProgramApi == null) {
      throw new Exception("null ApiService");
    }
    Call<Void> exerciseCall = exerciseProgramApi.updateCurrentSound(mPlanId, mExerciseId);
    Response<Void> response = exerciseCall.execute();
    if (!response.isSuccessful()) {
      Log.e(TAG, "unsuccessful");
      return;
    }
    Log.v(TAG, "Updated Exercise sound: "
        + mExerciseId +  " - from Exercise Plan: " + mPlanId);
  }

  @Override
  protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return RetryConstraint.RETRY;
  }
}
