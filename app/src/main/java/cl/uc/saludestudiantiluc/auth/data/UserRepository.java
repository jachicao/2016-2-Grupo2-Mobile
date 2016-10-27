package cl.uc.saludestudiantiluc.auth.data;

/**
 * Created by lukas on 9/26/16.
 */

public interface UserRepository {

  String getUserEmail();

  String getUserName();

  String getUserAccessToken();

  String getUserAccessTokenClient();

  String getUid();

  String getUserPassword();

  boolean isUserLoggedIn();

  void storeUserEmail(String email);

  void storeUserName(String name);

  void storeAccessToken(String accessToken);

  void storeAccessTokenClient(String accessTokenClient);

  void storeUid(String uid);

  void logOut();

}
