package cl.uc.saludestudiantiluc.sequences;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
    for(SequencesImage image : mSequence.images) {
      if (image.index == position) {
        return ImageFragment.newInstance(image);
      }
    }
    return ImageFragment.newInstance(null);
  }

  @Override
  public int getCount() {

    return mSequence != null ? mSequence.images.size() : 1;
  }
}
