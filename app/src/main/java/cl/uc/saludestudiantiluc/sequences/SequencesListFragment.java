package cl.uc.saludestudiantiluc.sequences;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.common.BaseFragment;
import cl.uc.saludestudiantiluc.sequences.data.SequencesRepository;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class SequencesListFragment extends BaseFragment {
  
  public static final String TAG = SequencesListFragment.class.getSimpleName();

  private boolean mTryingToLoadSequence = false;

  private List<Sequence> mSequences = new ArrayList<>();
  private RecyclerView mRecyclerView;
  private SequencesRepository mSequencesRepository;
  private ListAdapter mAdapter;

  private View mThisView;

  public static SequencesListFragment newInstance() {
    return new SequencesListFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mSequencesRepository = ((BaseActivity) getActivity()).getRelaxUcApplication()
        .getSequencesRepository();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    mThisView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
    mRecyclerView = (RecyclerView) mThisView.findViewById(R.id.fragment_recycler_view);
    mRecyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);
    mAdapter = new ListAdapter(mSequences, new ListAdapter.CardViewListener() {
      @Override
      public void onClick(Sequence sequence) {
        loadSequence(sequence);
      }
    });
    mRecyclerView.setAdapter(mAdapter);
    BitmapManager.setFilesDir(getActivity().getFilesDir());
    BitmapManager.setContext(getActivity().getApplicationContext());
    downloadJson();

    return mThisView;
  }


  private void downloadJson() {
    mSequencesRepository.getSequences()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Sequence>>() {
          @Override
          public void onCompleted() {
            Log.d(TAG, "on completed");
          }

          @Override
          public void onError(Throwable e) {
            Log.e(TAG, "error: ", e);
            Snackbar.make(mThisView.findViewById(R.id.fragment_recycler_view_coordinator_layout),
                getString(R.string.failed_download_json), Snackbar.LENGTH_SHORT).show();
          }

          @Override
          public void onNext(List<Sequence> sequences) {
            // Because there are few sequences, just clear the list and add the new ones.
            mSequences.clear();
            mSequences.addAll(sequences);
            mAdapter.notifyDataSetChanged();
          }
        });
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
