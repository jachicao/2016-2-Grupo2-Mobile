package cl.uc.saludestudiantiluc.auth;

/**
 * Created by jchicao on 9/25/16.
 */


public interface AuthListener {
  void onSignedIn(DataResponse dataResponse);
}