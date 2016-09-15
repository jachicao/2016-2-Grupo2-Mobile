package cl.uc.saludestudiantiluc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cl.uc.saludestudiantiluc.squarebreathing.SquareBreathingActivity;

public class Menu extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);

    Button exerciseButton = (Button) findViewById(R.id.exerciseButton);
    exerciseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(SquareBreathingActivity.getIntent(Menu.this));
      }
    });

    Button imageButton = (Button) findViewById(R.id.imageButton);


  }
}
