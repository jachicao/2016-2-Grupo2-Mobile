package cl.uc.saludestudiantiluc.services.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageView;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.callback.JobManagerCallback;
import com.birbit.android.jobqueue.config.Configuration;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cl.uc.saludestudiantiluc.RelaxUcApplication;

/**
 * Created by jchicao on 10/19/16.
 */

public class DownloadService {

  private static final String TAG = DownloadService.class.getSimpleName();

  public static String getStringDir(Context context, FileRequest fileRequest) {
    return getFileDir(context, fileRequest).getAbsolutePath();
  }

  static File getFileDir(Context context, FileRequest fileRequest) {
    return new File(context.getFilesDir() + File.separator + fileRequest.getRelativePath()).getAbsoluteFile();
  }

  private static boolean containsFile(Context context, FileRequest fileRequest) {
    return getFileDir(context, fileRequest).exists();
  }

  public static boolean containsFiles(Context context, FilesRequest filesRequest) {
    for (FileRequest fileRequest : filesRequest.getFileRequests()) {
      if (!containsFile(context, fileRequest)) {
        return false;
      }
    }
    return true;
  }

  private BroadcastReceiver mBroadcastReceiver;
  private HashMap<String, FileRequest> mFileRequests = new HashMap<>();
  private LocalBroadcastManager mLocalBroadcastManager;

  public DownloadService(Context context) {
    mBroadcastReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) {
          return;
        }
        String action = intent.getAction();
        switch (action) {
          case DownloadJob.DOWNLOAD_JOB_DONE:
            String path = intent.getStringExtra(DownloadJob.DOWNLOAD_JOB_MESSAGE_PATH);
            Log.v(TAG, "Downloaded - " + path);
            if (mFileRequests.containsKey(path)) {
              mFileRequests.get(path).onFileReady(new File(path));
              mFileRequests.remove(path);
            }
            break;
        }
      }
    };
    mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
    mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, new IntentFilter(DownloadJob.DOWNLOAD_JOB_DONE));
  }

  public void requestIntoImageView(final Context context, final ImageView imageView, final FileRequest fileRequest) {
    fileRequest.addListener(new FileListener() {
      @Override
      public void onFileReady(File file) {
        Glide.with(context).load(file).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);
      }
    });
    requestFile(context, fileRequest);
  }

  public void requestFiles(Context context, final FilesRequest filesRequest) {
    final ArrayList<FileRequest> requestArrayList = filesRequest.getFileRequests();
    final int size = filesRequest.getFileRequests().size();
    final ArrayList<File> files = new ArrayList<>();
    for (FileRequest fileRequest : requestArrayList) {
      fileRequest.addListener(new FileListener() {
        @Override
        public void onFileReady(File file) {
          files.add(file);
          if (files.size() == size) {
            filesRequest.onFilesReady(files);
          }
        }
      });
      requestFile(context, fileRequest);
    }
  }

  public void requestFile(Context context, FileRequest fileRequest) {
    File file = getFileDir(context, fileRequest);
    if (file.exists()) {
      //file is inside internal storage, so we trigger onFileReady
      fileRequest.onFileReady(file);
    } else {
      //file must be downloaded
      downloadFile(context, fileRequest);
    }
  }

  private void downloadFile(Context context, final FileRequest fileRequest) {
    String path = getFileDir(context, fileRequest).getAbsolutePath();
    RelaxUcApplication relaxUcApplication = (RelaxUcApplication) context.getApplicationContext();
    if (relaxUcApplication != null) {
      JobManager jobManager = relaxUcApplication.getJobManager();
      if (!mFileRequests.containsKey(path)) {
        mFileRequests.put(path, fileRequest);
        jobManager.addJobInBackground(new DownloadJob(fileRequest.getUrl(), getFileDir(context, fileRequest).getAbsolutePath()));
      }
    }
  }

  public void onDestroy() {
    if (mLocalBroadcastManager != null) {
      mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }
  }
}
