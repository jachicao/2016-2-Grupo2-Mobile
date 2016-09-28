package cl.uc.saludestudiantiluc.sequences.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import cl.uc.saludestudiantiluc.sequences.Sequence;
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
        File cacheFile = new File(mContext.getCacheDir(), JSON_FILENAME);
        List<Sequence> sequences = null;
        try {
          FileInputStream fileInputStream = new FileInputStream(cacheFile);
          BufferedReader bufferedReader = new BufferedReader(
              new InputStreamReader(fileInputStream));
          String line = "";
          StringBuilder jsonBuilder = new StringBuilder();
          while ((line = bufferedReader.readLine()) != null) {
            jsonBuilder.append(line);
          }
          fileInputStream.close();
          sequences = mGson.fromJson(jsonBuilder.toString(),
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

    File cacheFile = new File(mContext.getCacheDir(), JSON_FILENAME);

    try {
      FileOutputStream outputStream = new FileOutputStream(cacheFile);
      outputStream.write(json.getBytes());
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
