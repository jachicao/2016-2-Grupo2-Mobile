package cl.uc.saludestudiantiluc.services.download;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jchicao on 10/22/16.
 */

public interface FilesListener {
  void onFilesReady(ArrayList<File> files);
  void onProgressUpdate(long percentage);
}
