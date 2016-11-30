package cl.uc.saludestudiantiluc.services.post;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;

import cl.uc.saludestudiantiluc.RelaxUcApplication;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.common.models.Media;
import cl.uc.saludestudiantiluc.imageries.models.Imagery;
import cl.uc.saludestudiantiluc.sequences.models.Sequence;
import cl.uc.saludestudiantiluc.services.post.jobs.EvaluationJob;
import cl.uc.saludestudiantiluc.services.post.jobs.ExerciseJob;
import cl.uc.saludestudiantiluc.services.post.jobs.StatisticJob;

/**
 * Created by jchicao on 10/29/16.
 */

public class PostService {

  private static final String TAG = PostService.class.getSimpleName();


  public void sendStatistic(Context context, Imagery imagery) {
    sendStatistic(context, imagery, StatisticJob.STATISTIC_JOB_TYPE_IMAGERY);
  }

  public void sendStatistic(Context context, Ambience ambience) {
    sendStatistic(context, ambience, StatisticJob.STATISTIC_JOB_TYPE_AMBIENCE);
  }


  public void sendStatistic(Context context, Sequence sequence) {
    sendStatistic(context, sequence, StatisticJob.STATISTIC_JOB_TYPE_SEQUENCE);
  }

  private void sendStatistic(Context context, Media media, int type) {
    RelaxUcApplication relaxUcApplication = (RelaxUcApplication) context.getApplicationContext();
    if (relaxUcApplication != null) {
      JobManager jobManager = relaxUcApplication.getJobManager();
      jobManager.addJobInBackground(new StatisticJob(media.getId(), type));

    }
  }

  public void sendEvaluation(Context context, int score, int type) {
    RelaxUcApplication relaxUcApplication = (RelaxUcApplication) context.getApplicationContext();
    if (relaxUcApplication != null) {
      JobManager jobManager = relaxUcApplication.getJobManager();
      jobManager.addJobInBackground(new EvaluationJob(score, type));

    }
  }

  public void sendExerciseListened(Context context, int planId, int exerciseId) {
    RelaxUcApplication relaxUcApplication = (RelaxUcApplication) context.getApplicationContext();
    if (relaxUcApplication != null) {
      JobManager jobManager = relaxUcApplication.getJobManager();
      jobManager.addJobInBackground(new ExerciseJob(planId, exerciseId));

    }
  }
}
