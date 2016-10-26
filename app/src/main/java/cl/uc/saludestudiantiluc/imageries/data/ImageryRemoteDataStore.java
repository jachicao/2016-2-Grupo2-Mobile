package cl.uc.saludestudiantiluc.imageries.data;

import java.util.List;

import cl.uc.saludestudiantiluc.imageries.models.Imagery;
import cl.uc.saludestudiantiluc.imageries.api.ImageryApi;
import rx.Observable;

/**
 * Created by lukas on 9/28/16.
 */

public class ImageryRemoteDataStore implements ImageryDataStore {

  private ImageryApi mImageryApi;

  public ImageryRemoteDataStore(ImageryApi imageryApi) {
    mImageryApi = imageryApi;
  }

  @Override
  public Observable<List<Imagery>> getImagerySoundList() {
    return mImageryApi.getImagerySoundList();
  }
}
