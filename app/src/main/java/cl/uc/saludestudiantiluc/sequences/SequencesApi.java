package cl.uc.saludestudiantiluc.sequences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by jchicao on 15-09-16.
 */
public interface SequencesApi {
  String BASE_URL = "https://raw.githubusercontent.com/jachicao/Testing/master/";

  @GET("sequences.json")
  Call<List<Sequence>> getSequences();

  @GET("")
  Call<ResponseBody> getImageBytes(@Url String url);
}

class SequencesApiInstance{
  private static SequencesApi sSequenceApi;
  public static SequencesApi getInstance(){
    if (sSequenceApi == null){
      final Gson gson = new GsonBuilder()
          .create();
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(SequencesApi.BASE_URL)
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build();
      sSequenceApi = retrofit.create(SequencesApi.class);
    }
    return sSequenceApi;
  }
}