package cl.uc.saludestudiantiluc.sequences.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.common.models.Media;
import cl.uc.saludestudiantiluc.services.download.FileRequest;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by jchicao on 15-09-16.
 */

public class Sequence extends Media implements Parcelable {

  private static final String SEQUENCE_PREVIEW_CACHE_PATH = "/sequence/previews/";

  @SerializedName("image_sequences")
  private List<SequencesImage> mImages = new ArrayList<>();

  protected Sequence(Parcel in) {
    super(in);
    in.readList(mImages, SequencesImage.class.getClassLoader());
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
    super.writeToParcel(parcel, i);
    parcel.writeList(mImages);
  }


  @Override
  public FileRequest getPreviewRequest() {
    return new FileRequest(mPreviewUrl, SEQUENCE_PREVIEW_CACHE_PATH + mPreviewName);
  }

  @Override
  public FilesRequest getFilesRequest() {
    FilesRequest filesRequest = new FilesRequest();
    for (SequencesImage image : mImages) {
      filesRequest.addRequest(image.getImageRequest());
    }
    return filesRequest;
  }

  public List<SequencesImage> getImages() {
    return mImages;
  }
}