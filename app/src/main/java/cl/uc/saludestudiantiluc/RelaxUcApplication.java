package cl.uc.saludestudiantiluc;

import android.app.Application;
import android.util.Log;

import cl.uc.saludestudiantiluc.auth.UserLocalDataRepository;
import cl.uc.saludestudiantiluc.auth.UserRepository;

/**
 * Created by lukas on 9/20/16.
 */

public class RelaxUcApplication extends Application {

  private UserRepository mUserRepository;

  @Override
  public void onCreate() {
    super.onCreate();
    mUserRepository = new UserLocalDataRepository(this);
    Log.d("APP", "on create");
  }

  public UserRepository getUserRepository() {
    return mUserRepository;
  }
}
