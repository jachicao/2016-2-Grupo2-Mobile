package cl.uc.saludestudiantiluc.sequences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common_design.BaseFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SequencesListFragment extends BaseFragment {

  private boolean mListLoaded = false;

  private boolean mTryingToLoadSequence = false;

  private List<Sequence> mSequences = new ArrayList<>();
  private RecyclerView mRecyclerView;

  private SharedPreferences getThisSharedPreferences() {
    if (isAdded()) {
      FragmentActivity activity = getActivity();
      if (activity != null) {
        return activity.getSharedPreferences(getString(R.string.sequences_shared_preferences), Context.MODE_PRIVATE);
      }
    }
    return null;
  }
  private View mThisView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mThisView = inflater.inflate(R.layout.sequences_fragment_list, container, false);
    mRecyclerView = (RecyclerView) mThisView.findViewById(R.id.sequencesRecyclerView);
    mRecyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);
    BitmapManager.setFilesDir(getActivity().getFilesDir());
    BitmapManager.setContext(getActivity().getApplicationContext());
    downloadJson();

    return mThisView;
  }


  private void downloadJson() {

    Call<List<Sequence>> callInstance = SequencesApiInstance.getInstance().getSequences();
    callInstance.enqueue(new Callback<List<Sequence>>() {
      @Override
      public void onResponse(Call<List<Sequence>> call, Response<List<Sequence>> response) {
        if (response.isSuccessful()) {
          Gson gson = new Gson();
          SharedPreferences shared = getThisSharedPreferences();
          if (shared != null) {
            SharedPreferences.Editor editor = shared.edit();
            editor.putString(getString(R.string.sequences_shared_preferences_json), gson.toJson(response.body()));
            editor.commit();
          }
        }
        loadSequences();
      }

      @Override
      public void onFailure(Call<List<Sequence>> call, Throwable t) {
        Snackbar.make(mThisView.findViewById(R.id.sequencesListCoordinator), getString(R.string.failed_download_json), Snackbar.LENGTH_SHORT).show();
        loadSequences();
      }
    });
  }
  private void loadSequences() {
    if (!mListLoaded) {
      SharedPreferences shared = getThisSharedPreferences();
      if (shared != null) {
        String json = shared.getString(getString(R.string.sequences_shared_preferences_json), null);
        if (json != null) {
          Gson gson = new Gson();
          List<Sequence> sequences = gson.fromJson(json, new TypeToken<List<Sequence>>() {
          }.getType());
          if (sequences != null) {
            mSequences = sequences;
          }
          RecyclerView.Adapter mAdapter = new ListAdapter(mSequences, new CardViewListener() {
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
  }

  public void loadSequence(Sequence sequence) {
    if (!mTryingToLoadSequence && sequence != null) {
      mTryingToLoadSequence = true;
      dismiss();
      Intent intent = new Intent(getActivity(), ImagesActivity.class);
      intent.putExtra(getString(R.string.sequences_parcelable_name), sequence);
      startActivity(intent);
      mTryingToLoadSequence = false;
    }
  }
}
