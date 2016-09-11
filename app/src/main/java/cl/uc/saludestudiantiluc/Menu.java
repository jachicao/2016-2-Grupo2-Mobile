package cl.uc.saludestudiantiluc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button exerciseButton = (Button)findViewById(R.id.exerciseButton);


        Button imageButton = (Button)findViewById(R.id.imageButton);


    }
}
