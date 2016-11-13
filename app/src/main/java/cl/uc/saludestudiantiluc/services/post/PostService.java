package cl.uc.saludestudiantiluc.services.post;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;

import java.util.HashSet;

import cl.uc.saludestudiantiluc.RelaxUcApplication;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.common.models.Media;
import cl.uc.saludestudiantiluc.imageries.models.Imagery;
import cl.uc.saludestudiantiluc.sequences.models.Sequence;
import cl.uc.saludestudiantiluc.services.post.jobs.StatisticJob;

/**
 * Created by jchicao on 10/29/16.
 */

public class PostService {

  public static final String POST_SERVICE_INTENT_FILTER = "PostJobIntentFilter";
  private static final String TAG = PostService.class.getSimpleName();
  private static final String HASH_SET_FORMAT = "%d - %s";

  private BroadcastReceiver mBroadcastReceiver;
  private LocalBroadcastManager mLocalBroadcastManager;
  private HashSet<String> mStatisticsSent = new HashSet<>();

  public PostService(Context context) {
    mBroadcastReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) {
          return;
        }
        String action = intent.getAction();
        switch (action) {
          case POST_SERVICE_INTENT_FILTER:
            if (intent.getBooleanExtra(StatisticJob.STATISTIC_JOB, false)) {
              int id = intent.getIntExtra(StatisticJob.STATISTIC_JOB_ID, -1);
              String type = intent.getStringExtra(StatisticJob.STATISTIC_JOB_TYPE);
              mStatisticsSent.remove(String.format(HASH_SET_FORMAT, id, type));
            }
            break;
        }
      }
    };
    mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
    mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, new IntentFilter(POST_SERVICE_INTENT_FILTER));
  }


  public void sendStatistic(Context context, Imagery imagery) {
    sendStatistic(context, imagery, StatisticJob.STATISTIC_JOB_TYPE_IMAGERY);
  }

  public void sendStatistic(Context context, Ambience ambience) {
    sendStatistic(context, ambience, StatisticJob.STATISTIC_JOB_TYPE_AMBIENCE);
  }


  public void sendStatistic(Context context, Sequence sequence) {
    sendStatistic(context, sequence, StatisticJob.STATISTIC_JOB_TYPE_SEQUENCE);
  }

  private void sendStatistic(Context context, Media media, String type) {
    RelaxUcApplication relaxUcApplication = (RelaxUcApplication) context.getApplicationContext();
    if (relaxUcApplication != null) {
      if (mStatisticsSent.add(String.format(HASH_SET_FORMAT, media.getId(), type))) {
        JobManager jobManager = relaxUcApplication.getJobManager();
        jobManager.addJobInBackground(new StatisticJob(media.getId(), type));
      }
    }
  }

  public void onDestroy() {
    if (mLocalBroadcastManager != null) {
      mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }
  }
}
