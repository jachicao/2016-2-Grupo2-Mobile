package cl.uc.saludestudiantiluc.auth;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import cl.uc.saludestudiantiluc.R;

/**
 * Created by jchicao on 21-09-16.
 */

public class DataResponse implements Parcelable {
  public DataResponse(){

  }
  protected DataResponse(Parcel in) {
    id = in.readInt();
    email = in.readString();
    uid = in.readString();
    name = in.readString();
    nickname = in.readString();
    image = in.readString();
  }

  public static final Creator<DataResponse> CREATOR = new Creator<DataResponse>() {
    @Override
    public DataResponse createFromParcel(Parcel in) {
      return new DataResponse(in);
    }

    @Override
    public DataResponse[] newArray(int size) {
      return new DataResponse[size];
    }
  };

  public static DataResponse getPreviousInstance(Activity activity, SharedPreferences sharedPreferences) {
    String email = sharedPreferences.getString(activity.getString(R.string.auth_shared_preferences_data_email), null);
    if (email != null) {
      DataResponse response = new DataResponse();
      response.email = email;
      response.uid = sharedPreferences.getString(activity.getString(R.string.auth_shared_preferences_data_uid), null);
      response.name = sharedPreferences.getString(activity.getString(R.string.auth_shared_preferences_data_name), null);
      response.nickname = sharedPreferences.getString(activity.getString(R.string.auth_shared_preferences_data_nickname), null);
      response.image = sharedPreferences.getString(activity.getString(R.string.auth_shared_preferences_data_image), null);
      return response;
    }
    return null;
  }

  public void store(Activity activity, SharedPreferences.Editor editor) {
    if (email != null) {
      editor.putString(activity.getString(R.string.auth_shared_preferences_data_email), email);
      editor.putString(activity.getString(R.string.auth_shared_preferences_data_uid), uid);
      editor.putString(activity.getString(R.string.auth_shared_preferences_data_name), name);
      editor.putString(activity.getString(R.string.auth_shared_preferences_data_nickname), nickname);
      editor.putString(activity.getString(R.string.auth_shared_preferences_data_image), image);
      editor.commit();
    }
  }
  public int id;
  public String email;
  public String uid;
  public String name;
  public String nickname;
  public String image;
  public Error errors = new Error();

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(email);
    dest.writeString(uid);
    dest.writeString(name);
    dest.writeString(nickname);
    dest.writeString(image);
  }
}
class Error {
  public List<String> full_messages = new ArrayList<>();
}
