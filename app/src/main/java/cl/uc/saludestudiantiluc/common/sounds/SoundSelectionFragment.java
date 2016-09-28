package cl.uc.saludestudiantiluc.common.sounds;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import android.support.v4.app.FragmentActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.common.BaseFragment;
import cl.uc.saludestudiantiluc.common.sounds.data.SoundsRepository;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class SoundSelectionFragment extends BaseFragment {


  public static final String TAG = SoundSelectionFragment.class.getSimpleName();

  public static final String MEDIA_ORIGIN = "Origin";
  public static final String IMAGERY_CONSTANT = "Imagery";
  public static final String AMBIENTAL_CONSTANT = "Ambiental";

  private View mThisView;
  private RecyclerView mRecyclerView;
  private List<Sound> mImageries = new ArrayList<>();
  private String mParent;

  private SoundSelectionAdapter mAdapter;

  private SoundsRepository mSoundsRepository;

  public static SoundSelectionFragment newInstance(String mediaOrigin) {
    SoundSelectionFragment fragment = new SoundSelectionFragment();
    Bundle bundle = new Bundle();
    bundle.putString(MEDIA_ORIGIN, mediaOrigin);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mSoundsRepository = ((BaseActivity) getActivity()).getRelaxUcApplication()
        .getSoundsRepository();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
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
    mAdapter = new SoundSelectionAdapter(mImageries, mParent);
    mRecyclerView.setAdapter(mAdapter);
    downloadJson();
    return mThisView;
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putString(MEDIA_ORIGIN, mParent);
    // Always call the superclass so it can save the view hierarchy state
    super.onSaveInstanceState(savedInstanceState);
  }

  private void downloadJson() {
    Observable<List<Sound>> observable = mParent.equals(IMAGERY_CONSTANT)
        ? mSoundsRepository.getImagerySoundList() : mSoundsRepository.getAmbientalSoundList();
    observable.observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Sound>>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {
            Snackbar.make(mThisView.findViewById(R.id.fragment_recycler_view_coordinator_layout),
                getString(R.string.failed_download_json), Snackbar.LENGTH_SHORT).show();
          }

          @Override
          public void onNext(List<Sound> sounds) {
            mImageries.clear();
            mImageries.addAll(sounds);
            mAdapter.notifyDataSetChanged();
          }
        });
  }


}