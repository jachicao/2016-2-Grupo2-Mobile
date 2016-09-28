package cl.uc.saludestudiantiluc.sequences;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jchicao on 15-09-16.
 */

public class Sequence implements Parcelable {
  public void loadPreview(ImageView imageView) {
    BitmapManager.loadImage(imageView, preview);
  }

  public int id;
  public String name;
  public String description;
  public String preview;

  public List<SequencesImage> images = new ArrayList<>();


  protected Sequence(Parcel in) {
    id = in.readInt();
    name = in.readString();
    description = in.readString();
    preview = in.readString();
    in.readList(images, SequencesImage.class.getClassLoader());
  }


  public static final Creator<Sequence> CREATOR = new Creator<Sequence>() {
    @Override
    public Sequence createFromParcel(Parcel in) {
      return new Sequence(in);
    }

    @Override
    public Sequence[] newArray(int size) {
      return new Sequence[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeInt(id);
    parcel.writeString(name);
    parcel.writeString(description);
    parcel.writeString(preview);
    parcel.writeList(images);
  }
}