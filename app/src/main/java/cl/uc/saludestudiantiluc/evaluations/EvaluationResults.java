package cl.uc.saludestudiantiluc.evaluations;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cl.uc.saludestudiantiluc.R;

public class EvaluationResults extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_evaluation_results);

    Intent intent = getIntent();
    int score = intent.getIntExtra(BaseEvaluationActivity.TOTAL_SCORE, 0);

    TextView textview = (TextView) findViewById(R.id.total_score_results);
    textview.setText("Su resultado fue:" + score);


  }

}
