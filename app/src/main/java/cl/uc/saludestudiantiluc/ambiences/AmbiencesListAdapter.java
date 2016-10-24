package cl.uc.saludestudiantiluc.ambiences;

import android.view.View;
import android.widget.Button;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.ambiences.models.Ambience;
import cl.uc.saludestudiantiluc.common.models.BaseFragmentListModel;
import cl.uc.saludestudiantiluc.common.BaseListAdapter;
import cl.uc.saludestudiantiluc.common.BaseListFragment;
import cl.uc.saludestudiantiluc.services.download.DownloadService;
import cl.uc.saludestudiantiluc.services.download.FilesListener;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by jchicao on 10/20/16.
 */

class AmbiencesListAdapter extends BaseListAdapter {

  AmbiencesListAdapter(BaseListFragment baseListFragment) {
    super(baseListFragment);
  }

  @Override
  public void onBindViewHolder(final BaseListHolder holder, int position) {
    super.onBindViewHolder(holder, position);
    final Ambience ambience = ((AmbiencesListFragment) getFragment()).getDetailedList().get(position);
    holder.getCardView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ((AmbiencesListFragment) getFragment()).loadActivity(ambience);
      }
    });
    final Button downloadButton = holder.getDownloadButton();
    if (isDownloaded(ambience)) {
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
          FilesRequest filesRequest = ambience.getFilesRequest();
          filesRequest.addListener(new FilesListener() {
            @Override
            public void onFilesReady() {
              getFragment().notifyMessage(ambience.name + " " + getFragment().getContext().getString(R.string.downloaded).toLowerCase());
              v.setVisibility(View.GONE);
            }
          });
          getFragment().getDownloadService().requestFiles(filesRequest);
          downloadButton.setText(getFragment().getString(R.string.downloading));
          clicked[0] = true;
        }
      });
    }
  }

  @Override
  public boolean isDownloaded(BaseFragmentListModel model) {
    Ambience ambience = (Ambience) model;
    if (ambience != null) {
      return DownloadService.containsFiles(ambience.getFilesRequest());
    }
    return false;
  }
}
