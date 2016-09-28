package cl.uc.saludestudiantiluc;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;

import cl.uc.saludestudiantiluc.auth.UserLocalDataRepository;
import cl.uc.saludestudiantiluc.auth.UserRepository;
import cl.uc.saludestudiantiluc.common.RetrofitServiceFactory;
import cl.uc.saludestudiantiluc.sequences.Sequence;
import cl.uc.saludestudiantiluc.sequences.data.SequencesApi;
import cl.uc.saludestudiantiluc.sequences.data.SequencesDataRepository;
import cl.uc.saludestudiantiluc.sequences.data.SequencesLocalDataStore;
import cl.uc.saludestudiantiluc.sequences.data.SequencesRemoteDataStore;
import cl.uc.saludestudiantiluc.sequences.data.SequencesRepository;

/**
 * Created by lukas on 9/20/16.
 */

public class RelaxUcApplication extends Application {

  private Gson mGson;

  private UserRepository mUserRepository;
  private SequencesRepository mSequencesRepository;

  @Override
  public void onCreate() {
    super.onCreate();
    mGson = new Gson();

    mUserRepository = new UserLocalDataRepository(this);
    mSequencesRepository = createSequencesRepository();

    Log.d("APP", "on create");
  }

  public UserRepository getUserRepository() {
    return mUserRepository;
  }

  public SequencesRepository getSequencesRepository() {
    return mSequencesRepository;
  }

  private SequencesDataRepository createSequencesRepository() {
    SequencesLocalDataStore localDataStore = new SequencesLocalDataStore(this, mGson);
    SequencesApi sequencesApi = RetrofitServiceFactory.createRetrofitService(SequencesApi.class,
        SequencesApi.BASE_URL, mGson);
    SequencesRemoteDataStore remoteDataStore = new SequencesRemoteDataStore(sequencesApi);
    return new SequencesDataRepository(localDataStore, remoteDataStore);
  }
}
