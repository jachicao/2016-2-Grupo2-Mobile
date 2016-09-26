package cl.uc.saludestudiantiluc.common.sounds;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common_design.BaseFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SoundSelectionFragment extends BaseFragment {

  private View mThisView;
  private RecyclerView mRecyclerView;
  private List<Sound> mImageries = new ArrayList<>();
  private boolean mListLoaded = false;
  private String mParent;
  private Call<List<Sound>> mCallInstance;
  public static final String MEDIA_ORIGIN = "Origin";
  public static final String IMAGERY_CONSTANT = "Imagery";
  public static final String AMBIENTAL_CONSTANT = "Ambiental";

  public SharedPreferences getThisSharedPreferences() {
    FragmentActivity activity = getActivity();
    if (activity != null) {
      SharedPreferences shared = activity.getSharedPreferences("sounds", Context.MODE_PRIVATE);
      if (shared != null) {
        return shared;
      }
    }
    return null;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mThisView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      Bundle extras = getArguments();
      if (extras == null) {
        mParent = null;
      } else {
        mParent = extras.getString(MEDIA_ORIGIN);
      }
    } else {
      mParent = (String) savedInstanceState.getSerializable(MEDIA_ORIGIN);
    }

    mRecyclerView = (RecyclerView) mThisView.findViewById(R.id.fragment_recycler_view);
    mRecyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);
    downloadJson();
    return mThisView;
  }

  private void downloadJson() {
    final Gson gson = new GsonBuilder()
        .create();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(SoundApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    SoundApi apiInstance = retrofit.create(SoundApi.class);
    if (mParent.equals(IMAGERY_CONSTANT)) {
      mCallInstance = apiInstance.getImagerySoundList();
    } else if (mParent.equals(AMBIENTAL_CONSTANT)) {
      mCallInstance = apiInstance.getAmbientalSoundList();
    }
    mCallInstance.enqueue(new Callback<List<Sound>>() {
      @Override
      public void onResponse(Call<List<Sound>> call, Response<List<Sound>> response) {
        if (response.isSuccessful()) {
          SharedPreferences shared = getThisSharedPreferences();
          if (shared != null) {
            SharedPreferences.Editor editor = shared.edit();
            editor.putString("json", gson.toJson(response.body()));
            editor.commit();
          }
        }
        loadImageries();
      }

      @Override
      public void onFailure(Call<List<Sound>> call, Throwable t) {
        Snackbar.make(mThisView.findViewById(R.id.fragment_recycler_view_coordinator_layout), getString(R.string.failed_download_json), Snackbar.LENGTH_SHORT).show();
        loadImageries();
      }
    });
  }

  private void loadImageries() {
    if (!mListLoaded) {
      SharedPreferences shared = getThisSharedPreferences();
      if (shared != null) {
        String json = getThisSharedPreferences().getString("json", null);
        if (json != null) {
          Gson gson = new Gson();
          List<Sound> imageries = gson.fromJson(json, new TypeToken<List<Sound>>() {
          }.getType());
          if (imageries != null) {
            mImageries = imageries;
          }
          RecyclerView.Adapter mAdapter = new SoundSelectionAdapter(mImageries, mParent);
          mRecyclerView.setAdapter(mAdapter);
          mListLoaded = true;
        }
      }
    }
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putString(MEDIA_ORIGIN, mParent);
    // Always call the superclass so it can save the view hierarchy state
    super.onSaveInstanceState(savedInstanceState);
  }


}