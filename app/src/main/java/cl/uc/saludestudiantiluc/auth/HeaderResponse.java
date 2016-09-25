package cl.uc.saludestudiantiluc.auth;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.Set;

import cl.uc.saludestudiantiluc.R;
import okhttp3.Headers;

/**
 * Created by jchicao on 21-09-16.
 */


class HeaderResponse {
  static HeaderResponse newInstanceFromHeader(Headers header) {
    HeaderResponse response = new HeaderResponse();
    if (header != null) {
      for (String name : header.names()) {
        if (name.equalsIgnoreCase("access-token")) {
          response.access_token = header.get(name);
        } else if (name.equalsIgnoreCase("client")) {
          response.client = header.get(name);
        } else if (name.equalsIgnoreCase("uid")) {
          response.uid = header.get(name);
        }
      }
    }
    return response;
  }

  static HeaderResponse getPreviousInstance(Activity activity, SharedPreferences sharedPreferences) {
    String token = sharedPreferences.getString(activity.getString(R.string.auth_shared_preferences_header_access_token), null);
    if (token != null) {
      HeaderResponse response = new HeaderResponse();
      response.access_token = token;
      response.client = sharedPreferences.getString(activity.getString(R.string.auth_shared_preferences_header_client), null);
      response.uid = sharedPreferences.getString(activity.getString(R.string.auth_shared_preferences_header_uid), null);
      return response;
    }
    return null;
  }

  public void store(Activity activity, SharedPreferences.Editor editor) {
    if (access_token != null) {
      editor.putString(activity.getString(R.string.auth_shared_preferences_header_access_token), access_token);
      editor.putString(activity.getString(R.string.auth_shared_preferences_header_client), client);
      editor.putString(activity.getString(R.string.auth_shared_preferences_header_uid), uid);
      editor.commit();
    }
  }
  public String access_token;
  public String client;
  public String uid;
}
