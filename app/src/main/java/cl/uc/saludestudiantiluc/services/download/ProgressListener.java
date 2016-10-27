package cl.uc.saludestudiantiluc.services.download;

/**
 * Created by jchicao on 10/26/16.
 */

interface ProgressListener {
  void onUpdate(long percentage);
}