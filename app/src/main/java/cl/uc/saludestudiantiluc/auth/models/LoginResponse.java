package cl.uc.saludestudiantiluc.auth.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * Created by jchicao on 21-09-16.
 */

public class LoginResponse {

  @SerializedName("data")
  private DataResponse mData;


  public List<String> errors = new ArrayList<>();


  public int getId() {
    return mData.getId();
  }

  public String getEmail() {
    return mData.getEmail();
  }

}