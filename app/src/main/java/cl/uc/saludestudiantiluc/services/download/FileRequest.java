package cl.uc.saludestudiantiluc.services.download;

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

  public File getAbsoluteFile() {
    return DownloadService.getFileDir(mStringPath);
  }

  public String getUrl() {
    return mBaseUrl + mStringPath;
  }

  @Override
  public void onFileReady() {
    for(FileListener listener : mListeners) {
      listener.onFileReady();
    }
  }
}
