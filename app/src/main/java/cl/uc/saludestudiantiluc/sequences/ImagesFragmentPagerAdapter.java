package cl.uc.saludestudiantiluc.sequences;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cl.uc.saludestudiantiluc.sequences.models.Sequence;
import cl.uc.saludestudiantiluc.sequences.models.SequencesImage;

/**
 * Created by jchicao on 15-09-16.
 */
class ImagesFragmentPagerAdapter extends FragmentStatePagerAdapter {
  private Sequence mSequence;

  ImagesFragmentPagerAdapter(FragmentManager fm, Sequence sequence) {
    super(fm);
    mSequence = sequence;
  }

  @Override
  public Fragment getItem(int position) {
    for (SequencesImage image : mSequence.getImages()) {
      if (image.getIndex() == position) {
        return ImageFragment.newInstance(image);
      }
    }
    return ImageFragment.newInstance(null);
  }

  @Override
  public int getCount() {

    return mSequence != null ? mSequence.getImages().size() : 1;
  }
}
