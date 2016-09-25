package cl.uc.saludestudiantiluc.sequences;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jchicao on 15-09-16.
 */
public class ImagesFragmentPagerAdapter extends FragmentStatePagerAdapter {
  private Sequence mSequence;

  public ImagesFragmentPagerAdapter(FragmentManager fm, Sequence sequence) {
    super(fm);
    mSequence = sequence;
  }

  @Override
  public Fragment getItem(int position) {
    for(SequencesImage image : mSequence.images){
      if (image.index == position){
        return ImageFragment.newInstance(image);
      }
    }
    return ImageFragment.newInstance(null);
  }

  @Override
  public int getCount() {

    return mSequence != null ? mSequence.images.size() : 1;
  }

  // Returns the page title for the top indicator
  @Override
  public CharSequence getPageTitle(int position) {
    return (position + 1) + "";
  }
}
