package cl.uc.saludestudiantiluc.evaluations;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;

public class EvaluationResults extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_evaluation_results);

    //setToolBar

    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.evaluetion_results);
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
    Glide
        .with(this)
        .load(R.drawable.main_background)
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .centerCrop()
        .into((ImageView) findViewById(R.id.main_background_image));

    Intent intent = getIntent();
    int score = intent.getIntExtra(BaseEvaluationActivity.TOTAL_SCORE, 0);
    int role = intent.getIntExtra(BaseEvaluationActivity.USER_ROLE, 0);
    int type = intent.getIntExtra(BaseEvaluationActivity.EVALUATION_TYPE, 0);

    TextView textview = (TextView) findViewById(R.id.total_score_results);
    textview.setText("" + getRecomendation(score, type, role));
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
