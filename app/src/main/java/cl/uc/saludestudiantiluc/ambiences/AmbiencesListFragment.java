package cl.uc.saludestudiantiluc.ambiences;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.ambiences.data.AmbiencesRepository;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.common.models.Media;
import cl.uc.saludestudiantiluc.common.MediaListFragment;
import cl.uc.saludestudiantiluc.services.download.DownloadService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by jchicao on 10/20/16.
 */

public class AmbiencesListFragment extends MediaListFragment {

  public static final String TAG = AmbiencesListFragment.class.getSimpleName();
  public static final String AMBIENCE_EXTRAS_LIST = "AmbienceList";
  public static final String AMBIENCE_EXTRAS_INDEX = "AmbienceIndex";
  private List<Ambience> mAmbiences = new ArrayList<>();
  private AmbiencesRepository mAmbiencesRepository;

  public static Fragment newInstance() {
    return new AmbiencesListFragment();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mAmbiencesRepository = getBaseActivity().getRelaxUcApplication()
        .getAmbiencesRepository();
    getListFromRepository();
  }

  private void getListFromRepository() {
    mAmbiencesRepository
        .getAmbiences()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Ambience>>() {
          @Override
          public void onCompleted() {
            Log.d(TAG, "onCompleted");
          }

          @Override
          public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
            showSnackbarMessage(getString(R.string.failed_download_json));
          }

          @Override
          public void onNext(List<Ambience> ambiences) {
            Log.d(TAG, "onNext");
            mAmbiences.clear();
            mAmbiences.addAll(ambiences);
            getAdapter().notifyDataSetChanged();
          }
        });
  }

  public void loadActivity(Ambience ambience) {
    int index = -1;
    ArrayList<Ambience> downloadedList = new ArrayList<>();
    for(Ambience amb : mAmbiences) {
      if (amb.equals(ambience)) {
        index = downloadedList.size();
      }
      if (DownloadService.containsFiles(getContext(), amb.getFilesRequest())) {
        downloadedList.add(amb);
      }
    }
      if (downloadedList.size() > 0) {
      if (index > -1 && downloadedList.contains(ambience)) {
        Intent intent = new Intent(getActivity(), AmbienceActivity.class);
        intent.putExtra(AMBIENCE_EXTRAS_INDEX, index);
        intent.putParcelableArrayListExtra(AMBIENCE_EXTRAS_LIST, downloadedList);
        startActivity(intent);
      } else {
        showSnackbarMessage(getString(R.string.file_not_downloaded));
      }
    } else {
      showSnackbarMessage(getString(R.string.zero_files_downloaded));
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setAdapter(new AmbiencesListAdapter(this));
  }

  public List<Ambience> getDetailedList() {
    return mAmbiences;
  }

  @Override
  public List<Media> getModelList() {
    return new ArrayList<Media>(getDetailedList());
  }
}
