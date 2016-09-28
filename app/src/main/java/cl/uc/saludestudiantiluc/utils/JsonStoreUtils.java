package cl.uc.saludestudiantiluc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by lukas on 9/27/16.
 */

public final class JsonStoreUtils {

  private JsonStoreUtils() {

  }

  public static void storeJsonToFile(File dir, String fileName, String json) throws IOException {
    File cacheFile = new File(dir, fileName);

    FileOutputStream outputStream = new FileOutputStream(cacheFile);
    outputStream.write(json.getBytes());
    outputStream.close();
  }

  public static String readJsonStringFromFile(File dir, String fileName) throws IOException {
    File cacheFile = new File(dir, fileName);
    FileInputStream fileInputStream = new FileInputStream(cacheFile);
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(fileInputStream));
    String line = "";
    StringBuilder jsonBuilder = new StringBuilder();
    while ((line = bufferedReader.readLine()) != null) {
      jsonBuilder.append(line);
    }
    fileInputStream.close();
    return jsonBuilder.toString();
  }
}
