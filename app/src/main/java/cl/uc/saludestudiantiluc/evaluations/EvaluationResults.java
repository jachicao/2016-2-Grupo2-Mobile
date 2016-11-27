package cl.uc.saludestudiantiluc.evaluations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;

public class EvaluationResults extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_evaluation_results);

    //setToolBar
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.evaluation_results);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });

    //Set background
    loadMainBackground();

    Intent intent = getIntent();
    int score = intent.getIntExtra(BaseEvaluationActivity.TOTAL_SCORE, 0);
    int role = intent.getIntExtra(BaseEvaluationActivity.USER_ROLE, 0);
    int type = intent.getIntExtra(BaseEvaluationActivity.EVALUATION_TYPE, 0);

    TextView textview = (TextView) findViewById(R.id.total_score_results);
    textview.setText(""  + getRecomendation(score, type, role));

    getPostService().sendEvaluation(this, score, type);

    saveResult(score, role, type);
  }

  public String read(int type) {
    String text = "";
    try
    {
      BufferedReader fin =
          new BufferedReader(
              new InputStreamReader(
                  openFileInput("results"+ type +".txt")));

      String texto = fin.readLine();
      text += texto;
      fin.close();
    }
    catch (Exception ex)
    {
      Log.e("Ficheros", "Error al leer fichero desde recurso raw");
    }
    return text;

  }

  public void saveResult(int score, int role, int type) {
    try
    {


      OutputStreamWriter fout=
          new OutputStreamWriter(
              openFileOutput("results"+ type +".txt", Context.MODE_PRIVATE));

      fout.write("" + score + "|" + role + "|" + type +"\n");
      fout.close();


    }
    catch (Exception ex)
    {
      Log.e("Ficheros", "Error al leer fichero desde recurso raw");
    }

  }


  public String getRecomendation(int score, int testType, int role) {
    String recomendation;
    recomendation = "";
    switch (testType) {
      case 1: //Stress
        recomendation =  getRecomendationToStress(score, role);
        break;
      case 2: //anxiety
        recomendation =  getRecomendationToAnxiety(score, role);
        break;
      case 3: //Sleeping
        recomendation =  getRecomendationToSleeping(score, role);
        break;
      default:
        break;
    }
    return recomendation;
  }

  public String getRecomendationToStress(int score, int role) {

    String recomendation;
    recomendation = "";

    switch (role) {
      case 1: //Student
        if (score <= 18) {
          recomendation = getString(R.string.stress_1_student);
        } else if (score <= 37) {
          recomendation = getString(R.string.stress_2_student);
        } else {
          recomendation = getString(R.string.stress_3_student);
        }


        break;
      case 2: //Functionary
        if (score <= 18) {
          recomendation = getString(R.string.stress_1_functionary);
        } else if (score <= 37) {
          recomendation = getString(R.string.stress_2_functionary);
        } else {
          recomendation = getString(R.string.stress_3_functionary);
        }
        break;

      default:
        break;
    }
    return recomendation;

  }

  public String getRecomendationToAnxiety(int score, int role) {
    String recomendation;
    recomendation = "";

    switch (role) {
      case 1: //Student
        if (score <= 9) {
          recomendation = getString(R.string.anxiety_1_student);
        } else if (score <= 14) {
          recomendation = getString(R.string.anxiety_2_student);
        } else {
          recomendation = getString(R.string.anxiety_3_student);
        }
        break;
      case 2: //Functionary
        if (score <= 9) {
          recomendation = getString(R.string.anxiety_1_functionary);
        } else if (score <= 14) {
          recomendation = getString(R.string.anxiety_2_functionary);
        } else {
          recomendation = getString(R.string.anxiety_3_functionary);
        }
        break;

      default:
        break;
    }
    return recomendation;

  }

  public String getRecomendationToSleeping(int score, int role) {
    String recomendation;
    recomendation = "" ;

    switch (role) {
      case 1: //Student
        if (score == 0) {
          recomendation = getString(R.string.sleeping_1);
        } else if (score == 1) {
          recomendation = getString(R.string.sleeping_2);
        } else {
          recomendation = getString(R.string.sleeping_3_student);
        }
        break;
      case 2: //Functionary

        if (score == 0) {
          recomendation = getString(R.string.sleeping_1);
        } else if (score == 1) {
          recomendation = getString(R.string.sleeping_2);
        } else {
          recomendation = getString(R.string.sleeping_3_functionary);
        }

      default:
        break;
    }
    return recomendation;

  }


}
