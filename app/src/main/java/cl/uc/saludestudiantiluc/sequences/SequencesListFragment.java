package cl.uc.saludestudiantiluc.sequences;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.MediaListFragment;
import cl.uc.saludestudiantiluc.common.models.Media;
import cl.uc.saludestudiantiluc.sequences.data.SequencesRepository;
import cl.uc.saludestudiantiluc.sequences.models.Sequence;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class SequencesListFragment extends MediaListFragment {
  
  public static final String TAG = SequencesListFragment.class.getSimpleName();
  public static final String SEQUENCE_EXTRAS = "Sequence";

  private List<Sequence> mSequences = new ArrayList<>();
  private SequencesRepository mSequencesRepository;

  public static Fragment newInstance() {
    return new SequencesListFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setAdapter(new SequencesListAdapter(this));
  }

  public List<Sequence> getDetailedList() {
    return mSequences;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mSequencesRepository = getBaseActivity().getRelaxUcApplication()
        .getSequencesRepository();
    getSequences();
  }

  private void getSequences() {
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
            showSnackbarMessage(getString(R.string.failed_download_json));
          }

          @Override
          public void onNext(List<Sequence> sequences) {
            // Because there are few sequences, just clear the list and addRequest the new ones.
            mSequences.clear();
            mSequences.addAll(sequences);
            getAdapter().notifyDataSetChanged();
          }
        });
  }

  public void loadActivity(Sequence sequence) {
    if (sequence != null) {
      Intent intent = new Intent(getActivity(), ImagesActivity.class);
      intent.putExtra(SEQUENCE_EXTRAS, sequence);
      startActivity(intent);
    }
  }
  @Override
  public List<Media> getModelList() {
    return new ArrayList<Media>(getDetailedList());
  }
}
