package cl.uc.saludestudiantiluc.evaluations;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cl.uc.saludestudiantiluc.MainActivity;
import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.profile.ProfileActivity;

public class HomeEvaluation extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_evaluation);
    Button eval1= (Button) findViewById(R.id.button_evaluation_one);
    eval1.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        startActivity(BaseEvaluationActivity.getIntent(HomeEvaluation.this));
      }
    });

  }

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, HomeEvaluation.class);
  }
}

