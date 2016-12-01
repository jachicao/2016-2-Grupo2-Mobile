package cl.uc.saludestudiantiluc.auth.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by lukas on 9/26/16.
 */

public class UserLocalDataRepository implements UserRepository {

  private static final String KEY_USER_EMAIL = "email";
  private static final String KEY_USER_NAME = "name";
  private static final String KEY_USER_ACCESS_TOKEN = "access_token";
  private static final String KEY_USER_ACCESS_TOKEN_CLIENT = "access_token_client";
  private static final String KEY_USER_UID = "uid";
  private static final String KEY_USER_PASSWORD = "password";
  private static final String KEY_USER_ACADEMIC_TYPE = "academic_type";

  private final SharedPreferences mPrefs;

  public UserLocalDataRepository(Context context) {
    final String sharedPrefsName = context.getPackageName() + ".user_prefs";
    mPrefs = context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE);
  }

  @Override
  public String getUserEmail() {
    return mPrefs.getString(KEY_USER_EMAIL, "");
  }

  @Override
  public String getName() {
    return mPrefs.getString(KEY_USER_NAME, "");
  }

  @Override
  public String getAcademicType() {
    return mPrefs.getString(KEY_USER_ACADEMIC_TYPE, "");
  }

  @Override
  public String getUserAccessToken() {
    return mPrefs.getString(KEY_USER_ACCESS_TOKEN, "");
  }

  @Override
  public String getUserAccessTokenClient() {
    return mPrefs.getString(KEY_USER_ACCESS_TOKEN_CLIENT, "");
  }


  @Override
  public String getUid() {
    return mPrefs.getString(KEY_USER_UID, "");
  }

  @Override
  public String getUserPassword() {
    return mPrefs.getString(KEY_USER_PASSWORD, "");
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
  public void storeName(String name) {
    mPrefs.edit()
        .putString(KEY_USER_NAME, name)
        .apply();
  }

  @Override
  public void storeAccessToken(String accessToken) {
    Log.d("repository access token", accessToken);
    mPrefs.edit()
        .putString(KEY_USER_ACCESS_TOKEN, accessToken)
        .apply();
  }

  @Override
  public void storeAccessTokenClient(String accessTokenClient) {
    Log.d("repository client", accessTokenClient);
    mPrefs.edit()
        .putString(KEY_USER_ACCESS_TOKEN_CLIENT, accessTokenClient)
        .apply();
  }

  @Override
  public void storeUid(String uid) {
    Log.d("repository uid", uid);
    mPrefs.edit()
        .putString(KEY_USER_UID, uid)
        .apply();
  }

  @Override
  public void storeAcademicType(String academicType) {
    mPrefs.edit()
        .putString(KEY_USER_ACADEMIC_TYPE, academicType)
        .apply();
  }

  @Override
  public void logOut() {
    mPrefs.edit().clear().apply();
  }
}
