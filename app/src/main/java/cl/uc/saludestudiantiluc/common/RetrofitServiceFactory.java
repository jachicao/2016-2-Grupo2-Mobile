package cl.uc.saludestudiantiluc.common;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Helper class for building retrofit services.
 */

public final class RetrofitServiceFactory {

  public static <T> T createRetrofitService(final Class<T> clazz,
                                            final String endPoint,
                                            @NonNull Gson gson,
                                            @Nullable OkHttpClient client) {
    final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
        .baseUrl(endPoint)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson));

    if (client != null) {
      retrofitBuilder.client(client);
    }

    return retrofitBuilder.build().create(clazz);
  }

  public static <T> T createRetrofitService(final Class<T> clazz,
                                            final String endPoint,
                                            Gson gson) {
    return createRetrofitService(clazz, endPoint, gson, null);
  }

}
