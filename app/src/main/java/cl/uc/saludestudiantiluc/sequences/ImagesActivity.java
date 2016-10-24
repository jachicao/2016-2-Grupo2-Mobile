package cl.uc.saludestudiantiluc.sequences;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.sequences.models.Sequence;
import me.relex.circleindicator.CircleIndicator;

public class ImagesActivity extends BaseActivity {

  private ViewPager mPager;
  private PagerAdapter mPagerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sequences_viewpager);
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      Sequence sequence = extras.getParcelable(SequencesListFragment.SEQUENCE_EXTRAS);
      if (sequence != null) {
        mPager = (ViewPager) findViewById(R.id.sequences_view_pager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.sequences_view_pager_circle_indicator);
        mPagerAdapter = new ImagesFragmentPagerAdapter(getSupportFragmentManager(), sequence);
        mPager.setAdapter(mPagerAdapter);
        indicator.setViewPager(mPager);
        enableImmersiveMode();
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
}