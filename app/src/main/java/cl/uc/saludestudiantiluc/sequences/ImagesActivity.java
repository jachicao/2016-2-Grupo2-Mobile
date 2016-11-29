package cl.uc.saludestudiantiluc.sequences;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.sequences.models.Sequence;
import cl.uc.saludestudiantiluc.sequences.models.SequencesImage;
import me.relex.circleindicator.CircleIndicator;

public class ImagesActivity extends BaseActivity {

  private ViewPager mPager;
  private PagerAdapter mPagerAdapter;

  private Sequence mSequence;
  private int mCurrentPosition;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sequences_viewpager);
    loadMainBackground();

    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.sequences);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      mSequence = extras.getParcelable(SequencesListFragment.SEQUENCE_EXTRAS);
      if (mSequence != null) {
        mPager
            = (ViewPager) findViewById(R.id.sequences_view_pager);
        CircleIndicator indicator
            = (CircleIndicator) findViewById(R.id.sequences_view_pager_circle_indicator);
        mPagerAdapter = new ImagesFragmentPagerAdapter(getSupportFragmentManager(), mSequence);
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
          @Override
          public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

          }

          @Override
          public void onPageSelected(int position) {
            onNewPage(position);
          }

          @Override
          public void onPageScrollStateChanged(int state) {

          }
        });
        indicator.setViewPager(mPager);
        onNewPage(0);
        enableImmersiveMode();
        getPostService().sendStatistic(this, mSequence);
      }
    }
  }


  @Override
  public void onBackPressed() {
    if (mPager != null) {
      final int currentItem = mPager.getCurrentItem();
      if (currentItem == 0 || currentItem == mPagerAdapter.getCount() - 1) {
        super.onBackPressed();
      } else {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.sequences_cancel_confirmation))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.sequences_cancel_confirmation_cancel), new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                ImagesActivity.super.onBackPressed();
              }
            }).setNegativeButton(getString(R.string.sequences_cancel_confirmation_revert), new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

          }
        });
        AlertDialog alert = builder.create();
        alert.show();
      }
    } else {
      super.onBackPressed();
    }
  }

  private SequencesImage getCurrentImage() {
    return mSequence.getImages().get(mCurrentPosition);
  }

  private void onNewPage(int position) {
    mCurrentPosition = position;
  }

  public Sequence getSequence() {
    return mSequence;
  }
}