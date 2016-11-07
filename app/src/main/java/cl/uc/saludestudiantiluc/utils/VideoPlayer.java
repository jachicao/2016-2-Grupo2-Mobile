package cl.uc.saludestudiantiluc.utils;

import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jchicao on 10/23/16.
 */

public class VideoPlayer {

  private static final String TAG = VideoPlayer.class.getName();
  private static final String VIDEO_POSITION = "VideoPlayerPosition";

  private MediaPlayer mMediaPlayer;
  private TextureView mTextureView;
  private String mVideoPath = "";
  private ArrayList<MediaPlayer.OnPreparedListener> mOnPreparedListeners = new ArrayList<>();
  private TouchListener mTouchListener;
  private float mVideoWidth;
  private float mVideoHeight;
  private int mMediaPlayerPosition = 0;

  public VideoPlayer(TextureView textureView, String videoPath) {
    mTextureView = textureView;
    mVideoPath = videoPath;
  }

  public void addOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
    mOnPreparedListeners.add(onPreparedListener);
  }

  public void setTouchListener(TouchListener touchListener) {
    mTouchListener = touchListener;
  }

  public void setMediaPlayerPosition(int mediaPlayerPosition) {
    mMediaPlayerPosition = mediaPlayerPosition;
  }

  public void setMediaPlayerPosition(Bundle savedInstanceState) {
    if (savedInstanceState != null && savedInstanceState.containsKey(VIDEO_POSITION)) {
      mMediaPlayerPosition = savedInstanceState.getInt(VIDEO_POSITION, 0);
    }
  }

  public void initialize() {
    initVideoView();
  }

  private void initVideoView() {
    TouchDetector.register(mTextureView, new TouchListener() {
      @Override
      public void onTouch(int type) {
        if (mTouchListener != null) {
          mTouchListener.onTouch(type);
        }
      }
    });
    mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
      @Override
      public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        try {
          FileDescriptor fileDescriptor = new FileInputStream(new File(mVideoPath)).getFD();

          //get Height and Width from VideoFile
          MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
          metaRetriever.setDataSource(fileDescriptor);
          mVideoHeight = Float.parseFloat(metaRetriever
              .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
          mVideoWidth = Float.parseFloat(metaRetriever
              .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));

          //set video
          Surface surface = new Surface(surfaceTexture);
          MediaPlayer mediaPlayer = new MediaPlayer();
          mediaPlayer
              .setDataSource(fileDescriptor);
          mediaPlayer.setSurface(surface);
          mediaPlayer.setVolume(0, 0);
          mediaPlayer.setLooping(true);
          mediaPlayer.prepareAsync();
          mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
              updateTextureViewSize(mTextureView.getWidth(), mTextureView.getHeight());
              mMediaPlayer = mp;
              mp.seekTo(mMediaPlayerPosition);
              for (MediaPlayer.OnPreparedListener onPreparedListener : mOnPreparedListeners) {
                onPreparedListener.onPrepared(mp);
              }
            }
          });
        } catch (IOException err) {
          Log.d(TAG, err.getMessage());
        }
      }

      @Override
      public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

      }

      @Override
      public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
      }

      @Override
      public void onSurfaceTextureUpdated(SurfaceTexture surface) {

      }
    });
  }

  private void updateTextureViewSize(int viewWidth, int viewHeight) {
    float scaleX = 1.0f;
    float scaleY = 1.0f;

    if (mVideoWidth > viewWidth && mVideoHeight > viewHeight) {
      scaleX = mVideoWidth / viewWidth;
      scaleY = mVideoHeight / viewHeight;
    } else if (mVideoWidth < viewWidth && mVideoHeight < viewHeight) {
      scaleY = viewWidth / mVideoWidth;
      scaleX = viewHeight / mVideoHeight;
    } else if (viewWidth > mVideoWidth) {
      scaleY = (viewWidth / mVideoWidth) / (viewHeight / mVideoHeight);
    } else if (viewHeight > mVideoHeight) {
      scaleX = (viewHeight / mVideoHeight) / (viewWidth / mVideoWidth);
    }

    // Calculate pivot points, in our case crop from center
    int pivotPointX = viewWidth / 2;
    int pivotPointY = viewHeight / 2;

    Matrix matrix = new Matrix();
    matrix.setScale(scaleX, scaleY, pivotPointX, pivotPointY);

    mTextureView.setTransform(matrix);
    mTextureView.setLayoutParams(new FrameLayout.LayoutParams(viewWidth, viewHeight));
  }

  public MediaPlayer getMediaPlayer() {
    return mMediaPlayer;
  }

  public void onDestroy() {
    if (mMediaPlayer != null) {
      mMediaPlayer.stop();
      mMediaPlayer.reset();
      mMediaPlayer.release();
      mMediaPlayer = null;
    }
  }

  public void onSaveInstanceState(Bundle outState) {
    if (mMediaPlayer != null && outState != null) {
      outState.putInt(VIDEO_POSITION, mMediaPlayer.getCurrentPosition());
    }
  }
}
