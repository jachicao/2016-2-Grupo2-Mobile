package cl.uc.saludestudiantiluc.ambiences;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.common.SoundServiceActivity;
import me.relex.circleindicator.CircleIndicator;

public class AmbienceActivity extends SoundServiceActivity {

  public static final String TAG = AmbienceActivity.class.getSimpleName();

  private HashMap<Integer, MediaPlayer> mMediaPlayers = new HashMap<>();
  private ViewPager mPager;
  private PagerAdapter mPagerAdapter;
  private List<Ambience> mAmbiencesList = new ArrayList<>();
  private int mLastPageSelected = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ambience_activity);
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    mLastPageSelected = extras.getInt(AmbiencesListFragment.AMBIENCE_EXTRAS_INDEX, 0);
    mAmbiencesList = intent.getParcelableArrayListExtra(AmbiencesListFragment.AMBIENCE_EXTRAS_LIST);
    if (mLastPageSelected > -1) {
      setViewPager();
      enableImmersiveMode();
    }
  }

  public List<Ambience> getAmbiencesList() {
    return mAmbiencesList;
  }

  private Ambience getCurrentAmbience() {
    return getAmbiencesList().get(mLastPageSelected);
  }

  private void setViewPager() {
    mPager = (ViewPager)
        findViewById(R.id.sequences_view_pager);
    CircleIndicator indicator =
        (CircleIndicator) findViewById(R.id.sequences_view_pager_circle_indicator);
    mPagerAdapter = new AmbiencePagerAdapter(getSupportFragmentManager(), this);
    mPager.setAdapter(mPagerAdapter);
    mPager.setCurrentItem(mLastPageSelected);
    mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        mLastPageSelected = position;
        startVideoAndSound();
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
    indicator.setViewPager(mPager);
  }

  public void addVideoMediaPlayer(int index, MediaPlayer mediaPlayer) {
    mMediaPlayers.put(index, mediaPlayer);
    startVideoAndSound();
  }

  public void removeVideoMediaPlayer(int index) {
    mMediaPlayers.remove(index);
  }

  public void startVideoAndSound() {
    setNewSound(getCurrentAmbience());
    for(Map.Entry<Integer, MediaPlayer> entry : mMediaPlayers.entrySet()) {
      MediaPlayer mediaPlayer = entry.getValue();
      if (entry.getKey() == mLastPageSelected) {
        if (!mediaPlayer.isPlaying()) {
          mediaPlayer.start();
        }
      } else {
        mediaPlayer.seekTo(0);
        if (mediaPlayer.isPlaying()) {
          mediaPlayer.pause();
        }
      }
    }
  }

  public void setNewSound(Ambience ambience) {
    if (mSoundService != null) {
      mSoundService.newSound(ambience.getSoundRequest().getAbsoluteFile().getAbsolutePath(), ambience.name, isImmersiveMode(), 0);
    }
  }
}
