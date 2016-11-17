package cl.uc.saludestudiantiluc.imageries;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.common.MediaListFragment;
import cl.uc.saludestudiantiluc.common.models.Media;
import cl.uc.saludestudiantiluc.imageries.models.Imagery;
import cl.uc.saludestudiantiluc.imageries.data.ImageryRepository;
import cl.uc.saludestudiantiluc.services.download.DownloadService;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class ImageriesListFragment extends MediaListFragment {

  public static final String IMAGERIES_EXTRAS_LIST = "AmbienceList";
  public static final String IMAGERIES_EXTRAS_INDEX = "AmbienceIndex";
  public static final String TAG = ImageriesListFragment.class.getSimpleName();

  private List<Imagery> mImageries = new ArrayList<>();

  private ImageryRepository mImageryRepository;

  public static Fragment newInstance() {
    return new ImageriesListFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setAdapter(new ImageriesListAdapter(this));
  }


  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mImageryRepository = ((BaseActivity) getActivity()).getRelaxUcApplication()
        .getSoundsRepository();
    getListFromRepository();
  }

  private void getListFromRepository() {
    Observable<List<Imagery>> observable = mImageryRepository.getImagerySoundList();
    observable.observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Subscriber<List<Imagery>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
          showSnackbarMessage(getString(R.string.failed_download_json));
        }

        @Override
        public void onNext(List<Imagery> imageries) {
          mImageries.clear();
          mImageries.addAll(imageries);
          getAdapter().notifyDataSetChanged();
        }
      });
  }

  public List<Imagery> getDetailedList() {
    return mImageries;
  }

  @Override
  public List<Media> getModelList() {
    return new ArrayList<Media>(getDetailedList());
  }

  public void loadActivity(Imagery imagery) {
    int index = -1;
    ArrayList<Imagery> downloadedList = new ArrayList<>();
    for(Imagery ima : mImageries) {
      if (ima.equals(imagery)) {
        index = downloadedList.size();
      }
      if (DownloadService.containsFiles(getContext(), ima.getFilesRequest())) {
        downloadedList.add(ima);
      }
    }
    if (downloadedList.size() > 0) {
      if (index > -1 && downloadedList.contains(imagery)) {
        Intent intent = new Intent(getActivity(), ImageryActivity.class);
        intent.putExtra(IMAGERIES_EXTRAS_INDEX, index);
        intent.putParcelableArrayListExtra(IMAGERIES_EXTRAS_LIST, downloadedList);
        startActivity(intent);
      } else {
        showSnackbarMessage(getString(R.string.file_not_downloaded));
      }
    } else {
      showSnackbarMessage(getString(R.string.zero_files_downloaded));
    }
  }
}