package cl.uc.saludestudiantiluc.auth.models;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import cl.uc.saludestudiantiluc.R;

/**
 * Created by jchicao on 21-09-16.
 */

class DataResponse {

  @SerializedName("id")
  private int mId;

  @SerializedName("email")
  private String mEmail;

  int getId() {
    return mId;
  }

  String getEmail() {
    return mEmail;
  }
}
