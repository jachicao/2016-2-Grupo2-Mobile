package cl.uc.saludestudiantiluc.sequences;

import android.view.View;

import cl.uc.saludestudiantiluc.common.MediaListAdapter;
import cl.uc.saludestudiantiluc.common.MediaListFragment;
import cl.uc.saludestudiantiluc.sequences.models.Sequence;

/**
 * Created by jchicao on 15-09-16.
 */

class SequencesListAdapter extends MediaListAdapter {

  SequencesListAdapter(MediaListFragment mediaListFragment) {
    super(mediaListFragment);
  }

  @Override
  public void onBindViewHolder(MediaListHolder holder, int position) {
    super.onBindViewHolder(holder, position);
    final Sequence sequence = ((SequencesListFragment) getFragment()).getDetailedList().get(position);
    holder.getCardView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ((SequencesListFragment) getFragment()).loadActivity(sequence);
      }
    });
  }
}