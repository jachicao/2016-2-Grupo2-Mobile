package cl.uc.saludestudiantiluc.ambiences;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by jchicao on 10/21/16.
 */

class AmbiencePagerAdapter extends FragmentStatePagerAdapter {
  private AmbienceActivity mActivity;
  public AmbiencePagerAdapter(FragmentManager fm, AmbienceActivity activity) {
    super(fm);
    mActivity = activity;
  }

  @Override
  public Fragment getItem(int position) {
    return AmbienceViewPagerFragment.newInstance(position, mActivity.getAmbiencesList().get(position));
  }

  @Override
  public int getCount() {
    return mActivity.getAmbiencesList().size();
  }
}
