package cl.uc.saludestudiantiluc.services.download;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jchicao on 10/22/16.
 */

public class FilesRequest implements FilesListener {

  private ArrayList<FilesListener> mListeners = new ArrayList<>();
  private ArrayList<FileRequest> mFileRequests = new ArrayList<>();

  ArrayList<FileRequest> getFileRequests() {
    return mFileRequests;
  }

  public void addRequest(FileRequest fileRequest) {
    mFileRequests.add(fileRequest);
  }

  public void addFilesListener(FilesListener listener) {
    mListeners.add(listener);
  }

  @Override
  public void onFilesReady(ArrayList<File> files) {
    for (FilesListener filesListener : mListeners) {
      filesListener.onFilesReady(files);
    }
  }

  @Override
  public void onProgressUpdate(long percentage) {
    for (FilesListener filesListener : mListeners) {
      filesListener.onProgressUpdate(percentage);
    }
  }
}
