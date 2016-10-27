package cl.uc.saludestudiantiluc.evaluations.data;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import cl.uc.saludestudiantiluc.R;

/**
 * Created by Jr on 13-10-2016.
 */

public class generateEvaluation {

  public String loadJson(File file) {
    String jsonStr = null;
    try {


      FileInputStream stream = new FileInputStream(file);

      try {
        FileChannel fc = stream.getChannel();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

        jsonStr = Charset.defaultCharset().decode(bb).toString();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        stream.close();

      }
    } catch (Exception e) {
    e.printStackTrace();
    }
    return jsonStr;
  }


  public String getStringFromJson(InputStream is) {
    StringBuffer sb = new StringBuffer();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      String temp;
      while ((temp = br.readLine()) != null)
        sb.append(temp);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        br.close(); // stop reading
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return sb.toString();
  }


  public evaluationModel getEvaluation(String strJson) {
    Gson gson = new Gson();
    evaluationModel evaluation = gson.fromJson(strJson, evaluationModel.class);
    String p1 = evaluation.evaluationName;
    p1 = " " + p1;
    return evaluation;
  }

}
