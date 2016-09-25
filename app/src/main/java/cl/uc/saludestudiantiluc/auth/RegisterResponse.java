package cl.uc.saludestudiantiluc.auth;

import okhttp3.Headers;

/**
 * Created by jchicao on 21-09-16.
 */

public class RegisterResponse {
  public HeaderResponse header;
  //body
  public String status;
  public DataResponse data;
  public void setHeaderResponse(Headers h){
    header = HeaderResponse.newInstanceFromHeader(h);
  }
}

