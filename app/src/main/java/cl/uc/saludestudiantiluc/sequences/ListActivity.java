package cl.uc.saludestudiantiluc.sequences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListActivity extends AppCompatActivity {

  private boolean mListLoaded = false;

  private boolean mTryingToLoadSequence = false;

  private List<Sequence> mSequences = new ArrayList<>();
  private RecyclerView mRecyclerView;
  private RecyclerView.Adapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;

  private SharedPreferences getThisSharedPreferences() {
    return getSharedPreferences(getString(R.string.sequences_shared_preferences), Context.MODE_PRIVATE);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sequences_activity_list);
    mRecyclerView = (RecyclerView) findViewById(R.id.sequencesRecyclerView);
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLayoutManager);
    BitmapManager.setFilesDir(getFilesDir());
    BitmapManager.setContext(getApplicationContext());
    downloadJson();
  }


  private void downloadJson() {

    Call<List<Sequence>> callInstance = SequencesApiInstance.getInstance().getSequences();
    callInstance.enqueue(new Callback<List<Sequence>>() {
      @Override
      public void onResponse(Call<List<Sequence>> call, Response<List<Sequence>> response) {
        if (response.isSuccessful()) {
          Gson gson = new Gson();
          SharedPreferences.Editor editor = getThisSharedPreferences().edit();
          editor.putString(getString(R.string.sequences_shared_preferences_json), gson.toJson(response.body()));
          editor.commit();
        }
        loadSequences();
      }

      @Override
      public void onFailure(Call<List<Sequence>> call, Throwable t) {
        Snackbar.make(findViewById(R.id.sequencesListCoordinator), getString(R.string.sequences_failed_download_json), Snackbar.LENGTH_SHORT).show();
        loadSequences();
      }
    });
  }
  private void loadSequences() {
    if (!mListLoaded) {
      String json = getThisSharedPreferences().getString(getString(R.string.sequences_shared_preferences_json), null);
      if (json != null) {
        Gson gson = new Gson();
        List<Sequence> sequences = gson.fromJson(json, new TypeToken<List<Sequence>>() {
        }.getType());
        if (sequences != null) {
          mSequences = sequences;
        }
        mAdapter = new ListAdapter(mSequences, new CardViewListener() {
          @Override
          public void onClick(Sequence sequence) {
            loadSequence(sequence);
          }
        });
        mRecyclerView.setAdapter(mAdapter);
        mListLoaded = true;
      }
    }
  }

  public void loadSequence(Sequence sequence) {
    if (!mTryingToLoadSequence && sequence != null) {
      mTryingToLoadSequence = true;
      Intent intent = new Intent(this, ImagesActivity.class);
      intent.putExtra(getString(R.string.sequences_parcelable_name), sequence);
      startActivity(intent);
      mTryingToLoadSequence = false;
    }
  }
}
