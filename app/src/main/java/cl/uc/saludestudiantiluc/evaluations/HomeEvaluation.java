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

  public static final String TEST_TYPE = "com.example.relaxuc.evaluations.type";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_evaluation);
    Button eval1= (Button) findViewById(R.id.button_evaluation_one);
    eval1.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), BaseEvaluationActivity.class);
        intent.putExtra(TEST_TYPE, 1);
        startActivity(intent);
      }
    });

    Button eval2= (Button) findViewById(R.id.button_evaluation_two);
    eval2.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), BaseEvaluationActivity.class);
        intent.putExtra(TEST_TYPE, 2);
        startActivity(intent);
      }
    });


  }

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, HomeEvaluation.class);
  }


}

