package cl.uc.saludestudiantiluc.services.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.birbit.android.jobqueue.JobManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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

  static boolean containsFile(Context context, FileRequest fileRequest) {
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
  private HashSet<String> mFilePaths = new HashSet<>();
  private ArrayList<FileRequest> mFileRequests = new ArrayList<>();
  private LocalBroadcastManager mLocalBroadcastManager;

  public DownloadService(Context context) {
    mBroadcastReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) {
          return;
        }
        String action = intent.getAction();
        //Log.v(TAG, "Action - " + action);
        switch (action) {
          case DownloadJob.DOWNLOAD_JOB:
            String path = intent.getStringExtra(DownloadJob.DOWNLOAD_JOB_PATH);
            if (mFilePaths.contains(path)) {
              boolean onFileReady = intent.getBooleanExtra(DownloadJob.DOWNLOAD_JOB_ON_FILE_READY, false);
              if (onFileReady) {
                Log.v(TAG, "onFileReady - " + path);
                for (FileRequest fileRequest : mFileRequests) {
                  if (getFileDir(context, fileRequest).getAbsolutePath().equals(path)) {
                    fileRequest.onFileReady(new File(path));
                  }
                }
                mFilePaths.remove(path);
              } else {
                long percentage = intent.getLongExtra(DownloadJob.DOWNLOAD_JOB_ON_UPDATE_PERCENTAGE, -1);
                if (percentage > -1) {
                  for (FileRequest fileRequest : mFileRequests) {
                    if (getFileDir(context, fileRequest).getAbsolutePath().equals(path)) {
                      fileRequest.onProgressUpdate(percentage);
                    }
                  }
                }
              }
            }
            break;
        }
      }
    };
    mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
    mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, new IntentFilter(DownloadJob.DOWNLOAD_JOB));
  }

  public void requestIntoImageView(final Context context, final ImageView imageView, final FileRequest fileRequest) {
    fileRequest.addFileListener(new FileListener() {
      @Override
      public void onFileReady(File file) {
        Glide.with(context).load(file).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);
      }

      @Override
      public void onProgressUpdate(long percentage) {

      }
    });
    requestFile(context, fileRequest);
  }

  public void requestFiles(Context context, final FilesRequest filesRequest) {
    final ArrayList<FileRequest> requestArrayList = filesRequest.getFileRequests();
    final int size = filesRequest.getFileRequests().size();
    final ArrayList<File> files = new ArrayList<>();
    final HashMap<FileRequest, Long> percentages = new HashMap<>();
    for (final FileRequest fileRequest : requestArrayList) {
      percentages.put(fileRequest, 0L);
      fileRequest.addFileListener(new FileListener() {
        @Override
        public void onFileReady(File file) {
          files.add(file);
          if (files.size() == size) {
            filesRequest.onFilesReady(files);
          }
        }

        @Override
        public void onProgressUpdate(long percentage) {
          percentages.put(fileRequest, percentage);
          long totalPercentages = 0L;
          for (Map.Entry<FileRequest, Long> entry : percentages.entrySet()) {
            totalPercentages += entry.getValue();
          }
          filesRequest.onProgressUpdate(totalPercentages / size);
        }
      });
      //filesRequest.onProgressUpdate(0L);
      requestFile(context, fileRequest);
    }
  }

  void requestFile(Context context, FileRequest fileRequest) {
    File file = getFileDir(context, fileRequest);
    if (file.exists()) {
      //file is inside internal storage, so we trigger onFileReady
      fileRequest.onFileReady(file);
    } else {
      //file must be downloaded
      downloadFile(context, fileRequest);
    }
  }

  private void downloadFile(Context context, FileRequest fileRequest) {
    String path = getFileDir(context, fileRequest).getAbsolutePath();
    RelaxUcApplication relaxUcApplication = (RelaxUcApplication) context.getApplicationContext();
    if (relaxUcApplication != null) {
      JobManager jobManager = relaxUcApplication.getJobManager();
      String url = fileRequest.getUrl();
      String filePath = getFileDir(context, fileRequest).getAbsolutePath();
      if (url != null && filePath != null) {
        if (mFilePaths.add(path)) {
          jobManager.addJobInBackground(new DownloadJob(url, filePath));
        }
        mFileRequests.add(fileRequest);
      }
    }
  }

  public void onDestroy() {
    if (mLocalBroadcastManager != null) {
      mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }
  }
}
