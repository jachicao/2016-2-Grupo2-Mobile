package cl.uc.saludestudiantiluc;

import android.app.Application;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.google.gson.Gson;

import java.io.IOException;

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
import cl.uc.saludestudiantiluc.services.post.api.StatisticApi;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lukas on 9/20/16.
 */

public class RelaxUcApplication extends Application {

  private static final String TAG = RelaxUcApplication.class.getSimpleName();
  public static final String INTERCEPTOR_LOG_OUT = "InterceptorLogOut";

  private Gson mGson;

  private UserRepository mUserRepository;
  private SequencesRepository mSequencesRepository;
  private ImageryRepository mImageryRepository;
  private AmbiencesRepository mAmbiencesRepository;
  private OkHttpClient mOkHttpClient;
  private JobManager mJobManager;
  private StatisticApi mStatisticApiService;

  @Override
  public void onCreate() {
    super.onCreate();
    mGson = new Gson();
    mJobManager = new JobManager(new Configuration.Builder(this).build());

    mUserRepository = new UserLocalDataRepository(this);

    // TODO(lukas): should add authenticator.
    setupOkHttpClient();

    Log.d("APP", "on create");
  }

  /**
   * Call this method when the user has a new server token. This will invalidate the previous
   * token and set the new token according to what it is stored in the {@link UserRepository}.
   */

  private void setupOkHttpClient() {
    mOkHttpClient = new OkHttpClient().newBuilder()
        .addInterceptor(new Interceptor() {
          @Override
          public Response intercept(Chain chain) throws IOException {
            // TODO(lukas): we should check the host here!.
            Request request = chain.request().newBuilder()
                .header("access-token", mUserRepository.getUserAccessToken())
                .header("client", mUserRepository.getUserAccessTokenClient())
                .header("uid", mUserRepository.getUid())
                .build();
            return chain.proceed(request);
          }
        })
        .addInterceptor(new Interceptor() {
          @Override
          public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            int code = response.code();
            if (code == 401) {
              LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(INTERCEPTOR_LOG_OUT));
            }
            return response;
          }
        })
        .build();

    mSequencesRepository = createSequencesRepository();
    mImageryRepository = createSoundsRepository();
    mAmbiencesRepository = createAmbiencesRepository();

    mStatisticApiService = RetrofitServiceFactory.createRetrofitService(StatisticApi.class, StatisticApi.BASE_URL, mGson, mOkHttpClient);
  }

  public void onUserLoggedIn() {
    setupOkHttpClient();
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
        SequencesApi.BASE_URL, mGson, mOkHttpClient);
    SequencesRemoteDataStore remoteDataStore = new SequencesRemoteDataStore(sequencesApi);
    return new SequencesDataRepository(localDataStore, remoteDataStore);
  }

  private ImageryRepository createSoundsRepository() {
    ImageryLocalDataStore localDataStore = new ImageryLocalDataStore(this, mGson);
    ImageryApi imageryApi = RetrofitServiceFactory.createRetrofitService(ImageryApi.class,
        ImageryApi.BASE_URL, mGson, mOkHttpClient);
    ImageryRemoteDataStore remoteDataStore = new ImageryRemoteDataStore(imageryApi);
    return new ImageryDataRepository(localDataStore, remoteDataStore);
  }

  private AmbiencesRepository createAmbiencesRepository() {
    AmbiencesLocalDataStore localDataStore = new AmbiencesLocalDataStore(this, mGson);
    AmbienceApi api = RetrofitServiceFactory.createRetrofitService(AmbienceApi.class,
        AmbienceApi.BASE_URL, mGson, mOkHttpClient);
    AmbiencesRemoteDataStore remoteDataStore = new AmbiencesRemoteDataStore(api);
    return new AmbiencesDataRepository(localDataStore, remoteDataStore);
  }

  public OkHttpClient getOkHttpClient() {
    return mOkHttpClient;
  }

  public JobManager getJobManager() {
    return mJobManager;
  }

  public StatisticApi getStatisticApiService() {
    return mStatisticApiService;
  }

  public Gson getGson() {
    return getGson();
  }

}
