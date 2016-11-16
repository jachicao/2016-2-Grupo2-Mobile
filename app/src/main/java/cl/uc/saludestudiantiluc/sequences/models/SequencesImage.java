package cl.uc.saludestudiantiluc.sequences.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import cl.uc.saludestudiantiluc.services.download.FileRequest;

/**
 * Created by jchicao on 15-09-16.
 */
public class SequencesImage implements Parcelable {

  @SerializedName("infographic_id")
  private int mSequenceId = 0;

  @SerializedName("order")
  private int mOrder = 1;

  @SerializedName("image")
  private Image mImage = new Image();

  protected SequencesImage(Parcel in) {
    mSequenceId = in.readInt();
    mOrder = in.readInt();
    mImage = in.readParcelable(Image.class.getClassLoader());
  }

  public static final Creator<SequencesImage> CREATOR = new Creator<SequencesImage>() {
    @Override
    public SequencesImage createFromParcel(Parcel in) {
      return new SequencesImage(in);
    }

    @Override
    public SequencesImage[] newArray(int size) {
      return new SequencesImage[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(mSequenceId);
    dest.writeInt(mOrder);
    dest.writeParcelable(mImage, flags);
  }

  public FileRequest getImageRequest() {
    return mImage.getFileRequest(mSequenceId);
  }

  public int getIndex() {
    return mOrder - 1;
  }

  public String getDescription() {
    return mImage != null ? mImage.getDescription() : null;
  }
}