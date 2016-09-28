package cl.uc.saludestudiantiluc.sequences.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import cl.uc.saludestudiantiluc.sequences.Sequence;
import cl.uc.saludestudiantiluc.utils.JsonStoreUtils;
import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by lukas on 9/27/16.
 */

public class SequencesLocalDataStore implements SequencesDataStore {

  private static final String JSON_FILENAME = "sequences_cache.json";

  private Context mContext;
  private Gson mGson;

  public SequencesLocalDataStore(@NonNull Context context, @NonNull Gson gson) {
    mContext = context.getApplicationContext();
    mGson = gson;
  }

  @Override
  public Observable<List<Sequence>> getSequences() {
    return Observable.defer(new Func0<Observable<List<Sequence>>>() {
      @Override
      public Observable<List<Sequence>> call() {
        List<Sequence> sequences = null;
        try {
          String jsonSequences = JsonStoreUtils.readJsonStringFromFile(mContext.getCacheDir(),
              JSON_FILENAME);
          sequences = mGson.fromJson(jsonSequences,
              new TypeToken<List<Sequence>>() {}.getType());
        } catch (IOException e) {
          e.printStackTrace();
        }

        return Observable.just(sequences).subscribeOn(Schedulers.io());
      }
    });
  }

  void storeSequences(List<Sequence> sequences) {
    final String json = mGson.toJson(sequences);
    try {
      JsonStoreUtils.storeJsonToFile(mContext.getCacheDir(), JSON_FILENAME, json);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
