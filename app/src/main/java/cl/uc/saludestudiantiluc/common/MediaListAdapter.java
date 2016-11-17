package cl.uc.saludestudiantiluc.common;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.models.Media;
import cl.uc.saludestudiantiluc.services.download.FilesListener;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by jchicao on 10/20/16.
 */

public class MediaListAdapter extends RecyclerView.Adapter<MediaListAdapter.MediaListHolder> {

  private MediaListFragment mFragment;

  public MediaListAdapter(MediaListFragment mediaListFragment) {
    mFragment = mediaListFragment;
  }

  public MediaListFragment getFragment() {
    return mFragment;
  }

  @Override
  public MediaListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.media_recycler_card_view, parent, false);
    return new MediaListHolder(view);
  }

  @Override
  public void onBindViewHolder(MediaListHolder holder, int position) {
    holder.setView(this, getFragment().getModelList().get(position));
  }

  @Override
  public int getItemCount() {
    return getFragment().getModelList().size();
  }

  public static class MediaListHolder extends RecyclerView.ViewHolder {
    private CardView mCardView;
    private TextView mName;
    private TextView mDescription;
    private ImageView mPreview;
    private Button mDownloadButton;
    private Media mMedia;
    private boolean mDownloadClicked = false;
    private MediaListAdapter mAdapter;
    private String mDownloadingString = "";

    public MediaListHolder(View itemView) {
      super(itemView);
      mCardView = (CardView) itemView.findViewById(R.id.recycler_card_view);
      mName = (TextView) itemView.findViewById(R.id.recycler_card_view_name);
      mDescription = (TextView) itemView.findViewById(R.id.recycler_card_view_description);
      mPreview = (ImageView) itemView.findViewById(R.id.recycler_card_view_preview);
      mDownloadButton = (Button) itemView.findViewById(R.id.recycler_card_view_download_button);
    }

    void setView(MediaListAdapter adapter, Media media) {
      mDownloadClicked = false;
      mAdapter = adapter;
      mMedia = media;
      mName.setText(media.getName());
      mDescription.setText(media.getDescription());
      mDownloadButton.setText(mAdapter.getFragment().getString(R.string.download));
      mPreview.setImageResource(0);
      mAdapter
          .getFragment()
          .getDownloadService()
          .requestIntoImageView(
              mAdapter.getFragment().getContext(), mPreview, media.getPreviewRequest()
          );
      mDownloadingString = mAdapter.getFragment().getString(R.string.downloading);

      if (media.isDownloaded(mAdapter.getFragment().getContext())) {
        mDownloadButton.setVisibility(View.GONE);
      } else {
        mDownloadButton.setVisibility(View.VISIBLE);
        mDownloadButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (mDownloadClicked) {
              return;
            }
            FilesRequest filesRequest = mMedia.getFilesRequest();
            filesRequest.addFilesListener(new FilesListener() {
              @Override
              public void onFilesReady(ArrayList<File> files) {
                if (mAdapter != null) {
                  MediaListFragment fragment = mAdapter.getFragment();
                  if (fragment != null && fragment.isAdded()) {
                    fragment.showSnackbarMessage(
                        mMedia.getName() + " "
                            + fragment.getString(R.string.downloaded).toLowerCase()
                    );
                  }
                }
                mDownloadButton.setVisibility(View.GONE);
              }

              @Override
              public void onProgressUpdate(long percentage) {
                mDownloadButton.setText(mDownloadingString + " " + percentage + "%");
              }
            });
            mAdapter.getFragment().getDownloadService().requestFiles(mAdapter.getFragment().getContext(), filesRequest);
            mDownloadClicked = true;
          }
        });
      }
    }

    public Button getDownloadButton() {
      return mDownloadButton;
    }

    public CardView getCardView() {
      return mCardView;
    }
  }
}
