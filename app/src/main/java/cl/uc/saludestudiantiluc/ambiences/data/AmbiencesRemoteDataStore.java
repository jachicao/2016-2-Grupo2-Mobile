package cl.uc.saludestudiantiluc.ambiences.data;

import java.util.List;

import cl.uc.saludestudiantiluc.ambiences.api.AmbienceApi;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import rx.Observable;

/**
 * Created by jchicao on 10/20/16.
 */

public class AmbiencesRemoteDataStore implements AmbiencesDataStore {
  private AmbienceApi mApi;
  public AmbiencesRemoteDataStore(AmbienceApi api) {
    mApi = api;
  }

  @Override
  public Observable<List<Ambience>> getAmbiences() {
    return mApi.getAmbiences();
  }
}
