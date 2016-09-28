package cl.uc.saludestudiantiluc.common;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Helper class for building retrofit services.
 */

public final class RetrofitServiceFactory {

  public static <T> T createRetrofitService(final Class<T> clazz,
                                            final String endPoint,
                                            Gson gson) {
    final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(endPoint)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
    return retrofit.create(clazz);
  }

}
