package cl.uc.saludestudiantiluc.sequences;

import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseListAdapter;
import cl.uc.saludestudiantiluc.common.BaseListFragment;
import cl.uc.saludestudiantiluc.common.models.BaseFragmentListModel;
import cl.uc.saludestudiantiluc.sequences.models.Sequence;
import cl.uc.saludestudiantiluc.services.download.DownloadService;
import cl.uc.saludestudiantiluc.services.download.FilesListener;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by jchicao on 15-09-16.
 */

class SequencesListAdapter extends BaseListAdapter {

  SequencesListAdapter(BaseListFragment baseListFragment) {
    super(baseListFragment);
  }

  @Override
  public boolean isDownloaded(BaseFragmentListModel model) {
    Sequence sequence = (Sequence) model;
    if (sequence != null) {
      return DownloadService.containsFiles(getFragment().getContext(), sequence.getFilesRequest());
    }
    return false;
  }

  @Override
  public void onBindViewHolder(final BaseListHolder holder, int position) {
    super.onBindViewHolder(holder, position);
    final Sequence sequence = ((SequencesListFragment) getFragment()).getDetailedList().get(position);
    holder.getCardView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ((SequencesListFragment) getFragment()).loadActivity(sequence);
      }
    });
    final Button downloadButton = holder.getDownloadButton();
    if (isDownloaded(sequence)) {
      downloadButton.setVisibility(View.GONE);
    } else {
      final boolean[] clicked = { false };
      downloadButton.setVisibility(View.VISIBLE);
      downloadButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
          if (clicked[0]) {
            return;
          }
          FilesRequest filesRequest = sequence.getFilesRequest();
          filesRequest.addFilesListener(new FilesListener() {
            @Override
            public void onFilesReady(ArrayList<File> files) {
              getFragment().notifyMessage(sequence.name + " " + getFragment().getContext().getString(R.string.downloaded).toLowerCase());
              v.setVisibility(View.GONE);
            }

            @Override
            public void onProgressUpdate(long percentage) {
              downloadButton.setText(getFragment().getString(R.string.downloading) + " " + percentage + "%");
            }
          });
          getFragment().getDownloadService().requestFiles(getFragment().getContext(), filesRequest);
          clicked[0] = true;
        }
      });
    }
  }
}