package cl.uc.saludestudiantiluc.sequences.data;

import java.util.List;

import cl.uc.saludestudiantiluc.sequences.Sequence;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by jchicao on 15-09-16.
 */
public interface SequencesApi {
  String BASE_URL = "https://raw.githubusercontent.com/jachicao/Testing/master/";

  @GET("sequences.json")
  Observable<List<Sequence>> getSequences();

  @GET("")
  Call<ResponseBody> getImageBytes(@Url String url);
}