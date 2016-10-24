package cl.uc.saludestudiantiluc.sequences.models;

import android.os.Parcel;
import android.os.Parcelable;

import cl.uc.saludestudiantiluc.sequences.api.SequencesApi;
import cl.uc.saludestudiantiluc.services.download.FileRequest;

/**
 * Created by jchicao on 15-09-16.
 */
public class SequencesImage implements Parcelable {
  public int index;
  public String url;

  protected SequencesImage(Parcel in) {
    index = in.readInt();
    url = in.readString();
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
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeInt(index);
    parcel.writeString(url);
  }

  public FileRequest getImageRequest() {
    return new FileRequest(SequencesApi.BASE_URL, url);
  }
}