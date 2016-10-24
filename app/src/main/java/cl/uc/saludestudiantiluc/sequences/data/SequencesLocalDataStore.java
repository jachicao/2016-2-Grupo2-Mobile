package cl.uc.saludestudiantiluc.sequences.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cl.uc.saludestudiantiluc.common.JsonLocalDataStore;
import cl.uc.saludestudiantiluc.sequences.models.Sequence;
import rx.Observable;

/**
 * Created by lukas on 9/27/16.
 */

public class SequencesLocalDataStore extends JsonLocalDataStore implements SequencesDataStore {

  private static final String JSON_FILENAME = "sequences_cache.json";

  public SequencesLocalDataStore(@NonNull Context context, @NonNull Gson gson) {
    super(context, gson);
  }

  @Override
  public Observable<List<Sequence>> getSequences() {
    return getObjectFromStore(JSON_FILENAME, new TypeToken<List<Sequence>>(){});
  }

  void storeSequences(List<Sequence> sequences) {
    storeObject(sequences, JSON_FILENAME);
  }

}
