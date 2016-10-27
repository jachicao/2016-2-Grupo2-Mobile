package cl.uc.saludestudiantiluc.evaluations;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cl.uc.saludestudiantiluc.MainActivity;
import cl.uc.saludestudiantiluc.R;
import cl.uc.saludestudiantiluc.common.BaseActivity;
import cl.uc.saludestudiantiluc.profile.ProfileActivity;

public class HomeEvaluation extends BaseActivity {

  public static final String TEST_TYPE = "com.example.relaxuc.evaluations.type";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_evaluation);
    /*Button eval1= (Button) findViewById(R.id.button_evaluation_one);
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
    */

    TextView stressEvaluation = (TextView) findViewById(R.id.stressEvaluation);
    stressEvaluation.setText(getResources().getString(R.string.stress_evaluetion));
    stressEvaluation.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), BaseEvaluationActivity.class);
        intent.putExtra(TEST_TYPE, 1);
        startActivity(intent);
      }
    });

    TextView anxietyEvaluation = (TextView) findViewById(R.id.anxietyEvaluation);
    anxietyEvaluation.setText(getResources().getString(R.string.anxiety_evaluetion));
    anxietyEvaluation.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), BaseEvaluationActivity.class);
        intent.putExtra(TEST_TYPE, 2);
        startActivity(intent);
      }
    });

    TextView sleepingEvaluation = (TextView) findViewById(R.id.sleepingEvaluation);
    sleepingEvaluation.setText(getResources().getString(R.string.sleeping_evaluetion));
    sleepingEvaluation.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), BaseEvaluationActivity.class);
        intent.putExtra(TEST_TYPE, 2);
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
    Glide
        .with(this)
        .load(R.drawable.sunset_background)
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .centerCrop()
        .into((ImageView) findViewById(R.id.main_background_image));






  }

  public static Intent getIntent(Activity activity) {
    return new Intent(activity, HomeEvaluation.class);
  }


}

