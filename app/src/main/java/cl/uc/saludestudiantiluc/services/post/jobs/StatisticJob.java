package cl.uc.saludestudiantiluc.services.post.jobs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import cl.uc.saludestudiantiluc.RelaxUcApplication;
import cl.uc.saludestudiantiluc.services.post.PostService;
import cl.uc.saludestudiantiluc.services.post.api.StatisticApi;
import cl.uc.saludestudiantiluc.services.post.models.Statistic;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by jchicao on 10/29/16.
 */

public class StatisticJob extends Job {

  private static final String TAG = StatisticJob.class.getSimpleName();

  public static final String STATISTIC_JOB               = "StatisticJob";
  public static final String STATISTIC_JOB_ID            = "StatisticJobId";
  public static final String STATISTIC_JOB_TYPE          = "StatisticJobType";
  public static final String STATISTIC_JOB_TYPE_IMAGERY  = "StatisticJobTypeImagery";
  public static final String STATISTIC_JOB_TYPE_AMBIENCE = "StatisticJobTypeAmbience";
  public static final String STATISTIC_JOB_TYPE_SEQUENCE = "StatisticJobTypeSequence";

  private int mId;
  private String mType;

  public StatisticJob(int id, String type) {
    super(new Params(0).requireNetwork().persist());
    mId = id;
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
    StatisticApi statisticApi = relaxUcApplication.getStatisticApiService();
    if (statisticApi == null) {
      throw new Exception("null ApiService");
    }
    Call<Statistic> statisticCall = null;
    switch (mType) {
      case STATISTIC_JOB_TYPE_IMAGERY:
        statisticCall = statisticApi.sendImageryLog(mId);
        break;
      case STATISTIC_JOB_TYPE_AMBIENCE:
        statisticCall = statisticApi.sendAmbienceLog(mId);
        break;
      case STATISTIC_JOB_TYPE_SEQUENCE:
        statisticCall = statisticApi.sendSequenceLog(mId);
        break;
      default:
        break;
    }
    if (statisticCall == null) {
      throw new Exception("null Call");
    }
    Response<Statistic> statisticResponse = statisticCall.execute();
    if (!statisticResponse.isSuccessful()) {
      throw new Exception("Unsuccessful");
    }
    Log.v(TAG, "Log sent - Type: " + mType +  " - Id: " + mId + " - Success: " + statisticResponse.body().success);
    if (statisticResponse.body().success) {
      Intent intent = new Intent(PostService.POST_SERVICE_INTENT_FILTER);
      intent.putExtra(STATISTIC_JOB, true);
      intent.putExtra(STATISTIC_JOB_ID, mId);
      intent.putExtra(STATISTIC_JOB_TYPE, mType);
      LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
  }

  @Override
  protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return RetryConstraint.RETRY;
  }
}
