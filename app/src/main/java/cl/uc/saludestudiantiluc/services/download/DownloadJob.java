package cl.uc.saludestudiantiluc.services.download;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import java.io.File;
import java.io.IOException;

import cl.uc.saludestudiantiluc.RelaxUcApplication;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by jchicao on 10/18/16.
 */

class DownloadJob extends Job {
  private static final String TAG = DownloadJob.class.getSimpleName();
  static final String DOWNLOAD_JOB_DONE = "DownloadJobDone";
  static final String DOWNLOAD_JOB_MESSAGE_PATH = "DownloadJobMessagePath";
  private String mUrl;
  private String mFilePath;

  DownloadJob(String url, String filePath) {
    super(new Params(0).requireNetwork().persist());
    mUrl = url;
    mFilePath = filePath;
  }

  @Override
  public void onAdded() {
  }

  @Override
  public void onRun() throws Throwable {
    try {
      RelaxUcApplication relaxUcApplication = (RelaxUcApplication) getApplicationContext();
      if (relaxUcApplication != null) {
        File file = new File(mFilePath);
        Response response = relaxUcApplication.getOkHttpClient().newCall(new Request.Builder().url(mUrl).build()).execute();
        if (!response.isSuccessful()) {
          throw new Exception("error");
        }
        createFiles(file);
        BufferedSink sink = Okio.buffer(Okio.sink(file));
        sink.writeAll(response.body().source());
        sink.close();
        Intent intent = new Intent(DOWNLOAD_JOB_DONE);
        intent.putExtra(DOWNLOAD_JOB_MESSAGE_PATH, mFilePath);
        //Log.v(TAG, "Downloaded - " + file);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
      }
    } catch (IOException e) {
      throw e;
    }
  }

  @Override
  protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return RetryConstraint.RETRY;
  }

  private void createFiles(File file) {
    String[] split = file.toString().split(File.separator);
    String filePath = File.separator;
    for (int i = 0; i < split.length; i++) {
      filePath += split[i];
      File path = new File(filePath);
      if (split.length - 1 == i) {
        if (!path.exists()) {
          try {
            if (!path.createNewFile()) {
              Log.v(TAG, "createNewFile error");
              return;
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      } else {
        if (!path.exists()) {
          if (!path.mkdir()) {
            Log.v(TAG, "mkdir error " + path);
            return;
          }
        }
      }
      filePath += File.separator;
    }
  }
}