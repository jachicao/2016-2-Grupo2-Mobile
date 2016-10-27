package cl.uc.saludestudiantiluc.services.download;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jchicao on 10/16/16.
 */

public class FileRequest implements FileListener {

  private String mUrl;
  private String mStringPath;
  private ArrayList<FileListener> mListeners = new ArrayList<>();

  public FileRequest(String url, String stringPath) {
    mUrl = url;
    mStringPath = stringPath;
  }

  void addFileListener(FileListener listener) {
    mListeners.add(listener);
  }

  String getRelativePath() {
    return mStringPath;
  }

  String getUrl() {
    return mUrl;
  }

  @Override
  public void onFileReady(File file) {
    for (FileListener fileListener : mListeners) {
      fileListener.onFileReady(file);
    }
  }

  @Override
  public void onProgressUpdate(long percentage) {
    for (FileListener fileListener : mListeners) {
      fileListener.onProgressUpdate(percentage);
    }
  }
}
