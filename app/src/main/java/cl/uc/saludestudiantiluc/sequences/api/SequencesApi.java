package cl.uc.saludestudiantiluc.sequences.api;

import java.util.List;

import cl.uc.saludestudiantiluc.BuildConfig;
import cl.uc.saludestudiantiluc.sequences.models.Sequence;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by jchicao on 15-09-16.
 */
public interface SequencesApi {

  String BASE_URL = BuildConfig.HOST + "/api/v1/content/";

  @GET("infographics")
  Observable<List<Sequence>> getSequences();
}