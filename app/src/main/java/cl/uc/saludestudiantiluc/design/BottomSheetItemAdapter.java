package cl.uc.saludestudiantiluc.design;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cl.uc.saludestudiantiluc.R;

/**
 * Created by jchicao on 9/24/16.
 */

public class BottomSheetItemAdapter extends RecyclerView.Adapter<BottomSheetItemAdapter.BottomSheetViewHolder> {
  private Context mContext;
  private ArrayList<BottomSheetItem> mItems;
  private BottomSheetItemListener mListener;

  public BottomSheetItemAdapter(ArrayList<BottomSheetItem> items, BottomSheetItemListener listener) {
    mItems = items;
    mListener = listener;
  }

  @Override
  public BottomSheetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_bottom_sheet_item_fragment, parent, false);
    RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    rootView.setLayoutParams(lp);
    return new BottomSheetViewHolder(rootView);
  }

  @Override
  public void onBindViewHolder(BottomSheetViewHolder holder, int position) {
    if (holder != null) {
      holder.setItem(mItems.get(position));
    }
  }

  @Override
  public int getItemCount() {
    return mItems != null ? mItems.size() : 1;
  }

  class BottomSheetViewHolder extends RecyclerView.ViewHolder {

    private ImageView mImageView;
    private TextView mTextView;
    private View mItemView;
    private BottomSheetItem mItem;

    public BottomSheetViewHolder(View itemView) {
      super(itemView);
      mItemView = itemView;
      mImageView = (ImageView) itemView.findViewById(R.id.main_bottom_sheet_item_fragment_image);
      mTextView = (TextView) itemView.findViewById(R.id.main_bottom_sheet_item_fragment_text);
    }

    public void setItem(BottomSheetItem item) {
      mItem = item;
      mItemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mListener.onClick(mItem);
          mItem.onClick();
        }
      });
      mImageView.setImageResource(item.getDrawableResource());
      mTextView.setText(item.getTitle());
    }
  }
}
