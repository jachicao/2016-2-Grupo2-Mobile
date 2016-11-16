package cl.uc.saludestudiantiluc.imageries;

import android.view.View;

import cl.uc.saludestudiantiluc.common.MediaListAdapter;
import cl.uc.saludestudiantiluc.common.MediaListFragment;
import cl.uc.saludestudiantiluc.imageries.models.Imagery;

/**
 * Created by camilo on 14-09-16.
 */

class ImageriesListAdapter extends MediaListAdapter {

  ImageriesListAdapter(MediaListFragment mediaListFragment) {
    super(mediaListFragment);
  }

  @Override
  public void onBindViewHolder(MediaListHolder holder, int position) {
    super.onBindViewHolder(holder, position);
    final Imagery imagery = ((ImageriesListFragment) getFragment()).getDetailedList().get(position);
    holder.getCardView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ((ImageriesListFragment) getFragment()).loadActivity(imagery);
      }
    });
  }
}
