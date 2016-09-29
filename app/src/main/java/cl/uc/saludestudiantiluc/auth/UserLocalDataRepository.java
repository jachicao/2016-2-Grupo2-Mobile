package cl.uc.saludestudiantiluc.auth;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lukas on 9/26/16.
 */

public class UserLocalDataRepository implements UserRepository {

  private static final String KEY_USER_EMAIL = "email";
  private static final String KEY_USER_NAME = "name";
  private static final String KEY_USER_ACCESS_TOKEN = "access_token";
  private static final String KEY_USER_ACCESS_TOKEN_CLIENT = "access_token_client";
  private static final String KEY_USER_UID = "uid";

  private final SharedPreferences mPrefs;

  public UserLocalDataRepository(Context context) {
    final String sharedPrefsName = context.getPackageName() + ".user_prefs";
    mPrefs = context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE);
  }

  @Override
  public String getUserEmail() {
    return "msoto@uc.cl";
  }

  @Override
  public String getUserName() {
    return "Marcelo Soto";
  }

  @Override
  public boolean isUserLoggedIn() {
    return !getUserEmail().equals("");
  }

  @Override
  public void storeUserEmail(String email) {
    mPrefs.edit()
        .putString(KEY_USER_EMAIL, email)
        .apply();
  }

  @Override
  public void storeUserName(String name) {
    mPrefs.edit()
        .putString(KEY_USER_NAME, name)
        .apply();
  }

  @Override
  public void storeAccessToken(String accessToken) {
    mPrefs.edit()
        .putString(KEY_USER_ACCESS_TOKEN, accessToken)
        .apply();
  }

  @Override
  public void storeAccessTokenClient(String accessTokenClient) {
    mPrefs.edit()
        .putString(KEY_USER_ACCESS_TOKEN_CLIENT, accessTokenClient)
        .apply();
  }

  @Override
  public void storeUid(String uid) {
    mPrefs.edit()
        .putString(KEY_USER_UID, uid)
        .apply();
  }

  @Override
  public void logOut() {
    mPrefs.edit().clear().apply();
  }
}
