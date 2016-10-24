package cl.uc.saludestudiantiluc.common;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.models.BaseFragmentListModel;

/**
 * Created by jchicao on 10/20/16.
 */

public class BaseListAdapter extends RecyclerView.Adapter<BaseListAdapter.BaseListHolder> {
  private BaseListFragment mFragment;

  public BaseListAdapter(BaseListFragment baseListFragment) {
    mFragment = baseListFragment;
  }

  public BaseListFragment getFragment() {
    return mFragment;
  }

  @Override
  public BaseListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_recycler_card_view, parent, false);
    return new BaseListHolder(view);
  }

  @Override
  public void onBindViewHolder(BaseListHolder holder, int position) {
    holder.setView(this, getFragment().getModelList().get(position));
  }

  public boolean isDownloaded(BaseFragmentListModel model) {
    return true;
  }

  @Override
  public int getItemCount() {
    return getFragment().getModelList().size();
  }

  public static class BaseListHolder extends RecyclerView.ViewHolder {
    private CardView mCardView;
    private TextView mName;
    private TextView mDescription;
    private ImageView mPreview;
    private Button mDownloadButton;

    public BaseListHolder(View itemView) {
      super(itemView);
      mCardView = (CardView) itemView.findViewById(R.id.recycler_card_view);
      mName = (TextView) itemView.findViewById(R.id.recycler_card_view_name);
      mDescription = (TextView) itemView.findViewById(R.id.recycler_card_view_description);
      mPreview = (ImageView) itemView.findViewById(R.id.recycler_card_view_preview);
      mDownloadButton = (Button) itemView.findViewById(R.id.recycler_card_view_download_button);
    }

    void setView(BaseListAdapter adapter, BaseFragmentListModel baseFragmentListModel) {
      mName.setText(baseFragmentListModel.name);
      mDescription.setText(baseFragmentListModel.description);
      adapter.getFragment().getDownloadService().requestIntoImageView(mPreview, baseFragmentListModel.getPreviewRequest());
    }

    public Button getDownloadButton() {
      return mDownloadButton;
    }

    public CardView getCardView() {
      return mCardView;
    }
  }
}
