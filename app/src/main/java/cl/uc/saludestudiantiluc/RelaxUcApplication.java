package cl.uc.saludestudiantiluc;

import android.app.Application;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.google.gson.Gson;

import cl.uc.saludestudiantiluc.ambiences.api.AmbienceApi;
import cl.uc.saludestudiantiluc.ambiences.data.AmbiencesDataRepository;
import cl.uc.saludestudiantiluc.ambiences.data.AmbiencesLocalDataStore;
import cl.uc.saludestudiantiluc.ambiences.data.AmbiencesRemoteDataStore;
import cl.uc.saludestudiantiluc.ambiences.data.AmbiencesRepository;
import cl.uc.saludestudiantiluc.auth.data.UserLocalDataRepository;
import cl.uc.saludestudiantiluc.auth.data.UserRepository;
import cl.uc.saludestudiantiluc.common.RetrofitServiceFactory;
import cl.uc.saludestudiantiluc.imageries.api.ImageryApi;
import cl.uc.saludestudiantiluc.imageries.data.ImageryDataRepository;
import cl.uc.saludestudiantiluc.imageries.data.ImageryLocalDataStore;
import cl.uc.saludestudiantiluc.imageries.data.ImageryRemoteDataStore;
import cl.uc.saludestudiantiluc.imageries.data.ImageryRepository;
import cl.uc.saludestudiantiluc.sequences.api.SequencesApi;
import cl.uc.saludestudiantiluc.sequences.data.SequencesDataRepository;
import cl.uc.saludestudiantiluc.sequences.data.SequencesLocalDataStore;
import cl.uc.saludestudiantiluc.sequences.data.SequencesRemoteDataStore;
import cl.uc.saludestudiantiluc.sequences.data.SequencesRepository;
import okhttp3.OkHttpClient;

/**
 * Created by lukas on 9/20/16.
 */

public class RelaxUcApplication extends Application {

  private Gson mGson;

  private UserRepository mUserRepository;
  private SequencesRepository mSequencesRepository;
  private ImageryRepository mImageryRepository;
  private AmbiencesRepository mAmbiencesRepository;
  private OkHttpClient mOkHttpClient;
  private JobManager mJobManager;


  @Override
  public void onCreate() {
    super.onCreate();
    mGson = new Gson();

    mUserRepository = new UserLocalDataRepository(this);
    mSequencesRepository = createSequencesRepository();
    mImageryRepository = createSoundsRepository();
    mAmbiencesRepository = createAmbiencesRepository();
    mOkHttpClient = new OkHttpClient();
    mJobManager = new JobManager(new Configuration.Builder(this).build());
    Log.d("APP", "on create");
  }

  public UserRepository getUserRepository() {
    return mUserRepository;
  }

  public SequencesRepository getSequencesRepository() {
    return mSequencesRepository;
  }

  public ImageryRepository getSoundsRepository() {
    return mImageryRepository;
  }

  public AmbiencesRepository getAmbiencesRepository() {
    return mAmbiencesRepository;
  }


  private SequencesRepository createSequencesRepository() {
    SequencesLocalDataStore localDataStore = new SequencesLocalDataStore(this, mGson);
    SequencesApi sequencesApi = RetrofitServiceFactory.createRetrofitService(SequencesApi.class,
        SequencesApi.BASE_URL, mGson);
    SequencesRemoteDataStore remoteDataStore = new SequencesRemoteDataStore(sequencesApi);
    return new SequencesDataRepository(localDataStore, remoteDataStore);
  }

  private ImageryRepository createSoundsRepository() {
    ImageryLocalDataStore localDataStore = new ImageryLocalDataStore(this, mGson);
    ImageryApi imageryApi = RetrofitServiceFactory.createRetrofitService(ImageryApi.class,
        ImageryApi.BASE_URL, mGson);
    ImageryRemoteDataStore remoteDataStore = new ImageryRemoteDataStore(imageryApi);
    return new ImageryDataRepository(localDataStore, remoteDataStore);
  }

  private AmbiencesRepository createAmbiencesRepository() {
    AmbiencesLocalDataStore localDataStore = new AmbiencesLocalDataStore(this, mGson);
    AmbienceApi api = RetrofitServiceFactory.createRetrofitService(AmbienceApi.class, AmbienceApi.BASE_URL, mGson);
    AmbiencesRemoteDataStore remoteDataStore = new AmbiencesRemoteDataStore(api);
    return new AmbiencesDataRepository(localDataStore, remoteDataStore);
  }

  public OkHttpClient getOkHttpClient() {
    return mOkHttpClient;
  }

  public JobManager getJobManager() {
    return mJobManager;
  }

}
