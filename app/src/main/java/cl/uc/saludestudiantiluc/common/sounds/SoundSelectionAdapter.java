package cl.uc.saludestudiantiluc.common.sounds;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.imageries.CardViewListener;
import cl.uc.saludestudiantiluc.imageries.ImageryDisplayActivity;

/**
 * Created by camilo on 14-09-16.
 */
public class SoundSelectionAdapter extends RecyclerView.Adapter<SoundSelectionAdapter.ImageryHolder> {

  private List<Sound> mImageries;
  private CardViewListener mCardViewListener;
  private String mParent;

  public static class ImageryHolder extends RecyclerView.ViewHolder{
    private CardView mCardView;
    private TextView mName;
    private Sound mImagery;
    private Context mContext;
    private CardViewListener mCardViewListener;
    private String mOrigin;

    public ImageryHolder(View itemView) {
      super(itemView);
      mContext = itemView.getContext();
      mCardView = (CardView) itemView.findViewById(R.id.imageryCardView);
      mName = (TextView) itemView.findViewById(R.id.sequenceCardViewName);
      mCardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (mImagery != null) {
            Intent intent;
            Sound sound;
            if (mOrigin.equals("Imagery")){
              intent = new Intent(mContext, ImageryDisplayActivity.class);
              sound = new Sound(1, "Imagery", "An imagery description", "Imagery");
            }
            else {
              intent = new Intent(mContext, AmbientalSoundActivity.class);
              sound = new Sound(1, "Rain sound", "A sound of raining", "Ambiental");
            }
            intent.putExtra("Sound", sound);
            mContext.startActivity(intent);
          }
        }
      });
    }

    public void setCardViewListener(CardViewListener cardViewListener){
      mCardViewListener = cardViewListener;
    }

    public void setOriging(String origin){
      mOrigin = origin;
    }

    public void setImagery(Sound imagery) {
      mImagery = imagery;
    }

    public void setView() {
      if (mImagery != null) {
        mName.setText(mImagery.getName());
      }
    }

  }


  public SoundSelectionAdapter(List<Sound> imageries, String parent){
    mImageries = imageries;
    mParent = parent;
  }

  @Override
  public ImageryHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.cardview_imagery, parent, false);
    ImageryHolder ih = new ImageryHolder(v);
    //ih.setCardViewListener(mCardViewListener);
    ih.setOriging(mParent);
    return ih;
  }


  @Override
  public void onBindViewHolder(ImageryHolder holder, int position) {
    if (mImageries != null){
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
