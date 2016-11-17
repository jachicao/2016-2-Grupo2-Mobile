package cl.uc.saludestudiantiluc.services.post.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import cl.uc.saludestudiantiluc.RelaxUcApplication;
import cl.uc.saludestudiantiluc.services.post.api.EvaluationApi;
import cl.uc.saludestudiantiluc.services.post.models.Evaluation;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Junior on 16-11-2016.
 */

public class EvaluationJob extends Job {

  private static final String TAG = StatisticJob.class.getSimpleName();

  public static final String EVALUATION_JOB_TYPE_GAD7 = "EvaluationJobTypeGAD7";
  public static final String EVALUATION_JOB_TYPE_STRESS = "EvaluationJobTypeStress";
  public static final String EVALUATION_JOB_TYPE_SLEEP = "EvaluationJobTypeSleep";

  private int mScore;
  private String mType;

  // id == id del contenido ; Type = tipo de evaluation
  public EvaluationJob(int score, String type) {
    super(new Params(0).requireNetwork().persist());
    mScore = score;
    mType = type;
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
    EvaluationApi evaluationApi = relaxUcApplication.getEvaluationApiService();
    if (evaluationApi == null) {
      throw new Exception("null ApiService");
    }
    Call<Evaluation> evaluationCall = null;
    switch (mType) {
      case EVALUATION_JOB_TYPE_GAD7:
        evaluationCall = evaluationApi.sendGAD7(mScore);
        break;
      case EVALUATION_JOB_TYPE_STRESS:
        evaluationCall = evaluationApi.sendStress(mScore);
        break;
      case EVALUATION_JOB_TYPE_SLEEP:
        evaluationCall = evaluationApi.sendStress(mScore);
        break;
      default:
        break;
    }
    if (evaluationCall == null) {
      throw new Exception("null Call");
    }
    Response<Evaluation> evaluationResponse = evaluationCall.execute();
    if (!evaluationResponse.isSuccessful()) {
      throw new Exception("Unsuccessful");
    }

    Log.v(TAG, "Evaluation sent - Type: " + mType +  " - Id: " + mScore + " - Success: " +
        evaluationResponse.body().success);

  }

  @Override
  protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return RetryConstraint.RETRY;
  }
}
