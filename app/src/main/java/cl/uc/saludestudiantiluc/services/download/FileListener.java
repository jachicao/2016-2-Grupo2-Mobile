package cl.uc.saludestudiantiluc.services.download;

import java.io.File;

/**
 * Created by jchicao on 10/16/16.
 */

public interface FileListener {
  void onFileReady(File file);
  void onProgressUpdate(long percentage);
}
