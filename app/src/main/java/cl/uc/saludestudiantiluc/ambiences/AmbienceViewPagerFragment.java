package cl.uc.saludestudiantiluc.ambiences;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.services.download.DownloadService;
import cl.uc.saludestudiantiluc.utils.TouchDetector;
import cl.uc.saludestudiantiluc.utils.TouchListener;
import cl.uc.saludestudiantiluc.utils.VideoPlayer;

/**
 * Created by jchicao on 10/21/16.
 */

public class AmbienceViewPagerFragment extends Fragment {
  public static final String EXTRAS_AMBIENCE = "Ambience";
  public static final String EXTRAS_INDEX = "AmbienceIndex";
  private static final String TAG = AmbienceViewPagerFragment.class.getName();

  public static AmbienceViewPagerFragment newInstance(int index, Ambience ambience) {
    AmbienceViewPagerFragment fragment = new AmbienceViewPagerFragment();
    Bundle args = new Bundle();
    args.putParcelable(EXTRAS_AMBIENCE, ambience);
    args.putInt(EXTRAS_INDEX, index);
    fragment.setArguments(args);
    return fragment;
  }

  private AmbienceActivity getThisActivity() {
    return (AmbienceActivity) getActivity();
  }

  private int mIndex = 0;
  private View mThisView;
  private Ambience mAmbience;
  private VideoPlayer mVideoPlayer;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mThisView = inflater.inflate(
        R.layout.ambience_view_pager_fragment, container, false);
    mAmbience = getArguments().getParcelable(EXTRAS_AMBIENCE);
    mIndex = getArguments().getInt(EXTRAS_INDEX);
    if (mAmbience != null) {
      String videoPath = DownloadService.getStringDir(getContext(), mAmbience.getVideoRequest());
      TextureView textureView = (TextureView) mThisView.findViewById(R.id.ambience_view_pager_fragment_texture);
      mVideoPlayer = new VideoPlayer(textureView, videoPath);
      mVideoPlayer.setMediaPlayerPosition(savedInstanceState);
      mVideoPlayer.setTouchListener(new TouchListener() {
        @Override
        public void onTouch(int type) {
          switch (type) {
            case TouchDetector.ON_SINGLE_TAP_CONFIRMED:
              getThisActivity().onSingleTap();
              break;
          }
        }
      });
      mVideoPlayer.addOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
          getThisActivity().addVideoMediaPlayer(mIndex, mp);
        }
      });
      mVideoPlayer.initialize();
    }
    return mThisView;
  }

  @Override
  public void onDestroyView() {
    if (mVideoPlayer != null) {
      mVideoPlayer.onDestroy();
      mVideoPlayer = null;
      getThisActivity().removeVideoMediaPlayer(mIndex);
    }
    Log.v(TAG, "onDestroyView");
    super.onDestroyView();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (mVideoPlayer != null) {
      mVideoPlayer.onSaveInstanceState(outState);
    }
  }
}
