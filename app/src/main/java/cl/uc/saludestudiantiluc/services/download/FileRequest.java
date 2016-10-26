package cl.uc.saludestudiantiluc.services.download;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jchicao on 10/16/16.
 */

public class FileRequest implements FileListener {
  private String mBaseUrl;
  private String mStringPath;
  private ArrayList<FileListener> mListeners = new ArrayList<>();

  public FileRequest(String baseUrl, String stringPath) {
    mBaseUrl = baseUrl;
    mStringPath = stringPath;
  }

  public void addListener(FileListener listener) {
    mListeners.add(listener);
  }

  public String getRelativePath() {
    return mStringPath;
  }

  public String getUrl() {
    return mBaseUrl + mStringPath;
  }

  @Override
  public void onFileReady(File file) {
    for (FileListener fileListener : mListeners) {
      fileListener.onFileReady(file);
    }
  }
}
