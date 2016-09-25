package cl.uc.saludestudiantiluc.auth;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * Created by jchicao on 21-09-16.
 */

public class LoginResponse {
  //Header
  public HeaderResponse header = new HeaderResponse();
  //body
  public DataResponse data = new DataResponse();
  public boolean success = true;
  public List<String> errors = new ArrayList<>();
  public void setHeaderResponse(Headers h){
    header = HeaderResponse.newInstanceFromHeader(h);
  }
}