package cl.uc.saludestudiantiluc.sequences;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cl.uc.saludestudiantiluc.R;

/**
 * Created by jchicao on 15-09-16.
 */
class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

  public interface CardViewListener {
    void onClick(Sequence sequence);
  }

  private List<Sequence> mSequences;
  private CardViewListener mCardViewListener;

  ListAdapter(List<Sequence> sequences, CardViewListener cardViewListener) {
    mSequences = sequences;
    mCardViewListener = cardViewListener;
  }
  static class ListHolder extends RecyclerView.ViewHolder {
    private CardView mCardView;
    private TextView mName;
    private TextView mDescription;
    private ImageView mThumbnail;
    private Sequence mSequence;
    private CardViewListener mCardViewListener;

    ListHolder(View itemView) {
      super(itemView);
      mCardView = (CardView) itemView.findViewById(R.id.recycler_card_view);
      mName = (TextView) itemView.findViewById(R.id.recycler_card_view_name);
      mDescription = (TextView) itemView.findViewById(R.id.recycler_card_view_description);
      mThumbnail = (ImageView) itemView.findViewById(R.id.recycler_card_view_preview);
      mCardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (mSequence != null && mCardViewListener != null) {
            mCardViewListener.onClick(mSequence);
          }
        }
      });
    }

    void setListener(CardViewListener cardViewListener) {
      mCardViewListener = cardViewListener;
    }

    public void setSequence(Sequence sequence) {
      mSequence = sequence;
    }

    void setView() {
      if (mSequence != null) {
        mName.setText(mSequence.name);
        mDescription.setText(mSequence.description);
        mSequence.loadPreview(mThumbnail);
      }
    }
  }
  @Override
  public ListHolder onCreateViewHolder(ViewGroup parent,
                                       int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_recycler_card_view, parent, false);
    ListHolder vh = new ListHolder(v);
    vh.setListener(mCardViewListener);
    return vh;
  }

  @Override
  public void onBindViewHolder(ListHolder holder, int position) {
    if (mSequences != null) {
      holder.setSequence(mSequences.get(position));
    }
    holder.setView();
  }

  @Override
  public int getItemCount() {
    return mSequences != null ? mSequences.size() : 1;
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }
}