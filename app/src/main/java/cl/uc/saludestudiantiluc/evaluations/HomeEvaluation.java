package cl.uc.saludestudiantiluc.evaluations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.evaluations.models.EvaluationModel;

public class HomeEvaluation extends BaseActivity {

  public static final String TEST_TYPE = "com.example.relaxuc.evaluations.type";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_evaluation);

    TextView stressEvaluation = (TextView) findViewById(R.id.stressEvaluation);
    stressEvaluation.setText(getResources().getString(R.string.stress_evaluation));
    stressEvaluation.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), BaseEvaluationActivity.class);
        intent.putExtra(TEST_TYPE, EvaluationModel.EVALUATION_TYPE_STRESS);
        startActivity(intent);
      }
    });

    TextView anxietyEvaluation = (TextView) findViewById(R.id.anxietyEvaluation);
    anxietyEvaluation.setText(getResources().getString(R.string.anxiety_evaluation));
    anxietyEvaluation.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), BaseEvaluationActivity.class);
        intent.putExtra(TEST_TYPE, EvaluationModel.EVALUATION_TYPE_GAD7);
        startActivity(intent);
      }
    });

    TextView sleepingEvaluation = (TextView) findViewById(R.id.sleepingEvaluation);
    sleepingEvaluation.setText(getResources().getString(R.string.sleeping_evaluation));
    sleepingEvaluation.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), BaseEvaluationActivity.class);
        intent.putExtra(TEST_TYPE, EvaluationModel.EVALUATION_TYPE_SLEEP);
        startActivity(intent);
      }
    });

    //setToolBar

    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(R.string.evaluations);
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

  }

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, HomeEvaluation.class);
  }


}

