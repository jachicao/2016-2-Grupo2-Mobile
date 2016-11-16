package cl.uc.saludestudiantiluc.sequences.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import cl.uc.saludestudiantiluc.services.download.FileRequest;

/**
 * Created by jchicao on 11/5/16.
 */

class Image implements Parcelable {

  private static final String SEQUENCES_IMAGE_CACHE_PATH = "/sequence/%1$d/images/";

  @SerializedName("image_file")
  private String mImageUrl = "";

  @SerializedName("image_file_file_name")
  private String mImageFileName = "";

  @SerializedName("description")
  private String mDescription = null;

  Image() {

  }

  Image(Parcel in) {
    mImageUrl = in.readString();
    mImageFileName = in.readString();
    mDescription = in.readString();
  }

  public static final Creator<Image> CREATOR = new Creator<Image>() {
    @Override
    public Image createFromParcel(Parcel in) {
      return new Image(in);
    }

    @Override
    public Image[] newArray(int size) {
      return new Image[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mImageUrl);
    dest.writeString(mImageFileName);
    dest.writeString(mDescription);
  }

  FileRequest getFileRequest(int sequenceId) {
    return new FileRequest(mImageUrl,
        String.format(SEQUENCES_IMAGE_CACHE_PATH, sequenceId) + mImageFileName);
  }

  public String getDescription() {
    return mDescription;
  }
}
