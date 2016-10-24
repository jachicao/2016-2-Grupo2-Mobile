package cl.uc.saludestudiantiluc.services.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ImageView;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jchicao on 10/19/16.
 */

public class DownloadService {

  private static final String TAG = DownloadService.class.getSimpleName();
  public static File getFileDir(String filePath) {
    return new File(sContext.getFilesDir() + File.separator + filePath).getAbsoluteFile();
  }

  public static File getFileDir(FileRequest fileRequest) {
    return new File(sContext.getFilesDir() + File.separator + fileRequest.getRelativePath()).getAbsoluteFile();
  }

  public static boolean containsFile(FileRequest fileRequest) {
    return getFileDir(fileRequest).exists();
  }

  public static boolean containsFiles(FilesRequest filesRequest) {
    for(FileRequest fileRequest : filesRequest.getFileRequests()) {
      if (!containsFile(fileRequest)) {
        return false;
      }
    }
    return true;
  }
  private static Context sContext;
  private JobManager mJobManager;
  private BroadcastReceiver mBroadcastReceiver;
  private HashMap<String, FileRequest> mFileRequests = new HashMap<>();

  public DownloadService(Context context) {
    sContext = context;
    mJobManager = new JobManager(new Configuration.Builder(sContext).build());
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
            //Log.v(TAG, "Downloaded - " + path);
            if (mFileRequests.containsKey(path)) {
              mFileRequests.get(path).onFileReady();
              mFileRequests.remove(path);
            }
            break;
        }
      }
    };
    LocalBroadcastManager.getInstance(sContext).registerReceiver(mBroadcastReceiver, new IntentFilter(DownloadJob.DOWNLOAD_JOB_DONE));
  }

  public void requestIntoImageView(final ImageView imageView, final FileRequest fileRequest) {
    fileRequest.addListener(new FileListener() {
      @Override
      public void onFileReady() {
        Glide.with(sContext).load(fileRequest.getAbsoluteFile()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);
      }
    });
    requestFile(fileRequest);
  }

  public void requestFiles(final FilesRequest filesRequest) {
    final ArrayList<FileRequest> requestArrayList = filesRequest.getFileRequests();
    final int[] counter = {0};
    for(FileRequest fileRequest : requestArrayList) {
      fileRequest.addListener(new FileListener() {
        @Override
        public void onFileReady() {
          counter[0]++;
          if (requestArrayList.size() == counter[0]) {
            filesRequest.onFilesReady();
          }
        }
      });
      requestFile(fileRequest);
    }
  }

  public void requestFile(FileRequest fileRequest) {
    if (fileRequest.getAbsoluteFile().exists()) {
      //file is inside internal storage, so we trigger onFileReady
      fileRequest.onFileReady();
    } else {
      //file must be downloaded
      downloadFile(fileRequest);
    }
  }

  private void downloadFile(final FileRequest fileRequest) {
    String path = getFileDir(fileRequest).getAbsolutePath();
    if (!mFileRequests.containsKey(path)) {
      mFileRequests.put(path, fileRequest);
      mJobManager.addJobInBackground(new DownloadJob(fileRequest.getUrl(), getFileDir(fileRequest).getAbsolutePath()));
    }
  }

  public void onDestroy() {
    //mJobManager.destroyService();
    LocalBroadcastManager.getInstance(sContext).unregisterReceiver(mBroadcastReceiver);
  }
}
