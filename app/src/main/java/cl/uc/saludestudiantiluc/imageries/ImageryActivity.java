package cl.uc.saludestudiantiluc.imageries;

import android.content.ComponentName;
import android.os.Handler;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.TextureView;
import android.widget.MediaController;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.SoundServiceActivity;
import cl.uc.saludestudiantiluc.imageries.models.Imagery;
import cl.uc.saludestudiantiluc.services.download.DownloadService;
import cl.uc.saludestudiantiluc.services.sound.SoundService;
import cl.uc.saludestudiantiluc.utils.TouchDetector;
import cl.uc.saludestudiantiluc.utils.TouchListener;
import cl.uc.saludestudiantiluc.utils.VideoPlayer;

public class ImageryActivity extends SoundServiceActivity {

  private static final String TAG = ImageryActivity.class.getName();

  private VideoPlayer mVideoPlayer;
  private MediaController mSoundMediaController;
  private List<Imagery> mImageriesList = new ArrayList<>();
  private int mLastSoundSelected = 0;
  private TextureView mTextureView;
  private Handler mHandler = new Handler();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.imagery_activity);
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    mLastSoundSelected = extras.getInt(ImageriesListFragment.IMAGERIES_EXTRAS_INDEX, 0);
    mImageriesList = intent.getParcelableArrayListExtra(ImageriesListFragment.IMAGERIES_EXTRAS_LIST);
    mTextureView = (TextureView) findViewById(R.id.imagery_activity_texture_view);
    mVideoPlayer
        = new VideoPlayer(mTextureView, DownloadService.getStringDir(this, getCurrentImagery().getVideoRequest()));
    mVideoPlayer.setMediaPlayerPosition(savedInstanceState);
    mVideoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mp) {
        mp.start();
      }
    });
    mVideoPlayer.initialize();

    TouchDetector.register(mTextureView, new TouchListener() {
      @Override
      public void onTouch(int type) {
        switch (type) {
          case TouchDetector.ON_SINGLE_TAP_CONFIRMED:
            //onSingleTap();
            if (mSoundMediaController != null && !mSoundMediaController.isShowing()) {
              mSoundMediaController.show();
            }
            break;
        }
      }
    });
  }

  public List<Imagery> getImageriesList() {
    return mImageriesList;
  }

  private Imagery getCurrentImagery() {
    return getImageriesList().get(mLastSoundSelected);
  }

  @Override
  protected void onDestroy() {
    if (mVideoPlayer != null) {
      mVideoPlayer.onDestroy();
      mVideoPlayer = null;
    }
    super.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    if (mVideoPlayer != null) {
      mVideoPlayer.onSaveInstanceState(outState);
    }
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onServiceConnected(ComponentName name, IBinder service) {
    super.onServiceConnected(name, service);
    if (mSoundService != null && mTextureView != null) {
      mSoundMediaController = new MediaController(this, false); // TODO: change this to true
      mSoundMediaController.setAnchorView(mTextureView);
      mSoundMediaController.setMediaPlayer(new MediaController.MediaPlayerControl() {
        @Override
        public void start() {
          if (mSoundService != null) {
            mSoundService.startSound();
          }
        }

        @Override
        public void pause() {
          if (mSoundService != null) {
            mSoundService.pauseSound();
          }
        }

        @Override
        public int getDuration() {
          if (mSoundService != null) {
            MediaPlayer mediaPlayer = mSoundService.getMediaPlayer();
            if (mediaPlayer != null) {
              return mediaPlayer.getDuration();
            }
          }
          return 0;
        }

        @Override
        public int getCurrentPosition() {
          if (mSoundService != null) {
            MediaPlayer mediaPlayer = mSoundService.getMediaPlayer();
            if (mediaPlayer != null) {
              return mediaPlayer.getCurrentPosition();
            }
          }
          return 0;
        }

        @Override
        public void seekTo(int pos) {
          if (mSoundService != null) {
            MediaPlayer mediaPlayer = mSoundService.getMediaPlayer();
            if (mediaPlayer != null) {
              mediaPlayer.seekTo(pos);
            }
          }
        }

        @Override
        public boolean isPlaying() {
          if (mSoundService != null) {
            return mSoundService.getMediaPlayerState().equals(SoundService.PLAY_STATE);
          }
          return true;
        }

        @Override
        public int getBufferPercentage() {
          return 0;
        }

        @Override
        public boolean canPause() {
          return true;
        }

        @Override
        public boolean canSeekBackward() {
          return true;
        }

        @Override
        public boolean canSeekForward() {
          return true;
        }

        @Override
        public int getAudioSessionId() {
          return 0;
        }
      });
      mHandler.post(new Runnable() {
        public void run() {
          mSoundMediaController.setEnabled(true);
          mSoundMediaController.show();
        }
      });
    }
    if (mSoundService != null) {
      Imagery imagery = getCurrentImagery();
      mSoundService.newSound(DownloadService.getStringDir(this, imagery.getSoundRequest()), imagery.name, true, 0);
    }
  }
}
