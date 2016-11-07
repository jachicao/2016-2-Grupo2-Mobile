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
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by jchicao on 10/18/16.
 */

class DownloadJob extends Job {

  private static final String TAG = DownloadJob.class.getSimpleName();
  static final String DOWNLOAD_JOB = "DownloadJob";
  static final String DOWNLOAD_JOB_PATH = "DownloadJobPath";
  static final String DOWNLOAD_JOB_ON_FILE_READY = "DownloadJobOnFileReady";
  static final String DOWNLOAD_JOB_ON_UPDATE_PERCENTAGE = "DownloadJobOnUpdatePercentage";

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
        OkHttpClient.Builder builder =
            relaxUcApplication.getOkHttpClient().newBuilder();
        OkHttpClient okHttpClient = builder
            .addNetworkInterceptor(new Interceptor() {
              @Override
              public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                CustomResponseBody customResponseBody
                    = new CustomResponseBody(originalResponse.body(), new ProgressListener() {
                  @Override
                  public void onUpdate(long percentage) {
                    Intent intent = new Intent(DOWNLOAD_JOB);
                    intent.putExtra(DOWNLOAD_JOB_PATH, mFilePath);
                    intent.putExtra(DOWNLOAD_JOB_ON_UPDATE_PERCENTAGE, percentage);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                  }
                });
                return originalResponse.newBuilder()
                    .body(customResponseBody)
                    .build();
              }
            }).build();
        Request request = new Request.Builder().url(mUrl).build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
          throw new Exception("error");
        }
        createFiles(file);
        BufferedSink sink = Okio.buffer(Okio.sink(file));
        sink.writeAll(response.body().source());
        sink.close();
        response.close();
        Intent intent = new Intent(DOWNLOAD_JOB);
        intent.putExtra(DOWNLOAD_JOB_PATH, mFilePath);
        intent.putExtra(DOWNLOAD_JOB_ON_FILE_READY, true);
        //Log.v(TAG, "Downloaded - " + file);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
      }
    } catch (IOException error) {
      throw error;
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
          } catch (IOException error) {
            error.printStackTrace();
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