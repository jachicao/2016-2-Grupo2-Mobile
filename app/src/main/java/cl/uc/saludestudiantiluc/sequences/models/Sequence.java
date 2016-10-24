package cl.uc.saludestudiantiluc.sequences.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.common.models.BaseFragmentListModel;
import cl.uc.saludestudiantiluc.services.download.FilesRequest;

/**
 * Created by jchicao on 15-09-16.
 */

public class Sequence extends BaseFragmentListModel implements Parcelable {
  public List<SequencesImage> images = new ArrayList<>();


  protected Sequence(Parcel in) {
    super(in);
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
    super.writeToParcel(parcel, i);
    parcel.writeList(images);
  }

  public FilesRequest getFilesRequest() {
    FilesRequest filesRequest = new FilesRequest();
    for(SequencesImage image : images) {
      filesRequest.addRequest(image.getImageRequest());
    }
    return filesRequest;
  }
}