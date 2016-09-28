package cl.uc.saludestudiantiluc.common.sounds;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.imageries.ImageryDisplayActivity;
import cl.uc.saludestudiantiluc.sequences.BitmapManager;

/**
 * Created by camilo on 14-09-16.
 */
public class SoundSelectionAdapter extends RecyclerView.Adapter<SoundSelectionAdapter.ImageryHolder> {

  private List<Sound> mImageries;
  private String mParent;

  public static class ImageryHolder extends RecyclerView.ViewHolder {
    private CardView mCardView;
    private ImageView mImageView;
    private TextView mName;
    private TextView mDescription;
    private Sound mImagery;
    private Context mContext;
    private String mOrigin;

    public ImageryHolder(View itemView) {
      super(itemView);
      mContext = itemView.getContext();
      mCardView = (CardView) itemView.findViewById(R.id.recycler_card_view);
      mName = (TextView) itemView.findViewById(R.id.recycler_card_view_name);
      mDescription = (TextView) itemView.findViewById(R.id.recycler_card_view_description);
      mImageView = (ImageView) itemView.findViewById(R.id.recycler_card_view_preview);
      mCardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (mImagery != null) {
            Intent intent;
            Sound sound;
            if (mOrigin.equals("Imagery")) {
              intent = new Intent(mContext, ImageryDisplayActivity.class);
              sound = new Sound(1, "Imagery", "An imagery description", "Imagery", 30);
            } else {
              intent = new Intent(mContext, SoundActivity.class);
              sound = new Sound(1, "Rain sound", "A sound of raining", "Ambiental", 90);
            }
            intent.putExtra("Sound", sound);
            mContext.startActivity(intent);
          }
        }
      });
    }

    public void setOriging(String origin){
      mOrigin = origin;
    }

    public void setImagery(Sound imagery) {
      mImagery = imagery;
    }

    public void setView() {
      if (mImagery != null) {
        if (mName != null) {
          mName.setText(mImagery.name);
        }
        if (mDescription != null) {
          mDescription.setText(mImagery.description);
        }
        if (mImageView != null) {
          Glide.with(this.mContext).load(SoundApi.BASE_URL + mImagery.preview).diskCacheStrategy(DiskCacheStrategy.RESULT).into(mImageView);
        }
      }
    }

  }


  public SoundSelectionAdapter(List<Sound> imageries, String parent) {
    mImageries = imageries;
    mParent = parent;
  }

  @Override
  public ImageryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_recycler_card_view, parent, false);
    ImageryHolder ih = new ImageryHolder(view);
    ih.setOriging(mParent);
    return ih;
  }


  @Override
  public void onBindViewHolder(ImageryHolder holder, int position) {
    if (mImageries != null) {
      holder.setImagery(mImageries.get(position));
    }
    holder.setView();
  }

  @Override
  public int getItemCount() {
    return mImageries != null ? mImageries.size() : 1;
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }

}
