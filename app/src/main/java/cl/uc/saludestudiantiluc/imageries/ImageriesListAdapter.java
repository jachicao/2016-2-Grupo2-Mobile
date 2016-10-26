package cl.uc.saludestudiantiluc.imageries;

import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseListAdapter;
import cl.uc.saludestudiantiluc.common.BaseListFragment;
import cl.uc.saludestudiantiluc.common.models.BaseFragmentListModel;
import cl.uc.saludestudiantiluc.imageries.models.Imagery;
import cl.uc.saludestudiantiluc.services.download.DownloadService;
import cl.uc.saludestudiantiluc.services.download.FilesListener;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by camilo on 14-09-16.
 */

class ImageriesListAdapter extends BaseListAdapter {

  ImageriesListAdapter(BaseListFragment baseListFragment) {
    super(baseListFragment);
  }

  @Override
  public void onBindViewHolder(final BaseListHolder holder, int position) {
    super.onBindViewHolder(holder, position);
    final Imagery imagery = ((ImageriesListFragment) getFragment()).getDetailedList().get(position);
    holder.getCardView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ((ImageriesListFragment) getFragment()).loadActivity(imagery);
      }
    });
    final Button downloadButton = holder.getDownloadButton();
    if (isDownloaded(imagery)) {
      downloadButton.setVisibility(View.GONE);
    } else {
      final boolean[] clicked = { false};
      downloadButton.setVisibility(View.VISIBLE);
      downloadButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
          if (clicked[0]) {
            return;
          }
          FilesRequest filesRequest = imagery.getFilesRequest();
          filesRequest.addListener(new FilesListener() {
            @Override
            public void onFilesReady(ArrayList<File> files) {
              getFragment().notifyMessage(imagery.name + " " + getFragment().getContext().getString(R.string.downloaded).toLowerCase());
              v.setVisibility(View.GONE);
            }
          });
          getFragment().getDownloadService().requestFiles(getFragment().getContext(), filesRequest);
          downloadButton.setText(getFragment().getString(R.string.downloading));
          clicked[0] = true;
        }
      });
    }
  }

  @Override
  public boolean isDownloaded(BaseFragmentListModel model) {
    Imagery imagery = (Imagery) model;
    if (imagery != null) {
      return DownloadService.containsFiles(getFragment().getContext(), imagery.getFilesRequest());
    }
    return false;
  }
}
