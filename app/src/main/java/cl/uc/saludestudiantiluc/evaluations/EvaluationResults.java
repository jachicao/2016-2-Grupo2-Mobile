package cl.uc.saludestudiantiluc.evaluations;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.BufferedReader;
import java.io.InputStream;
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

    saveResult(score, role, type);
  }

  public String read() {
    String text = "";
    try
    {
      BufferedReader fin =
          new BufferedReader(
              new InputStreamReader(
                  openFileInput("results.txt")));

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
              openFileOutput("results.txt", Context.MODE_PRIVATE));

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
          recomendation = "Has podido afrontar y adaptarte a situaciones demandantes en el último mes. , Identifica lo que te ha ayudado hasta ahora para mantener niveles bajos de estrés y así poder seguir utilizando esos recursos cada vez que los necesites. Para que tu nivel no aumente, te recomendamos organizar tus tiempos acorde a tus demandas, priorizar tareas y equilibrar tu estilo de vida con actividades que posibiliten tu recreación y descanso. Existen diferentes actividades realizadas por nuestro programa que te pueden interesar, como lo son los talleres de Mindfulness, consejerías de ansiedad y talleres grupales “manejando mi ansiedad”. Recuerda que puedes solicitar tu hora online. ";
        } else if (score <= 37) {
          recomendation = "En el último mes,  puedes percibir que tu nivel de estrés se ha mantenido estable en el tiempo llevándote a sentir irritabilidad, cansancio, problemas para dormir, dolores de cabeza, entre otros. Es importante que puedas generar cambios para que tu nivel de percepción de estrés disminuya. Revisa las demandas que sean modificables y que estén bajo tu control, y asegúrate de complementar tus actividades con tiempo de descanso y recreación. Si sientes que esto se te dificulta, te recomendamos solicitar una hora de consejería de ansiedad o informarte de los servicios que ofrece la universidad. ";
        } else {
          recomendation = "En el último mes la intensidad de tus síntomas ha logrado interferir en algunas áreas de tu vida.  Puedes estar mostrándote irritable, estar teniendo insomnio, pesadillas, sintiendo tensión muscular, agotamiento, ansiedad, falta de concentración y atención, dificultades para pensar, además de una disminución en tus defensas inmunitarias. Mantenerte en este estado puede perjudicarte aún más si las condiciones de tu entorno no se modifican. Te recomendamos solicitar una opinión profesional dentro del corto plazo para evaluar tu situación. Acércate a la Unidad de Apoyo Psicológico ubicada en el 3er piso del Hall Universitario en campus San Joaquín o ingresa a http://apoyo.saludestudiantil.uc.cl/ para pedir una hora.";
        }


        break;
      case 2: //Functionary
        if (score <= 18) {
          recomendation = "Has podido afrontar y adaptarte a situaciones demandantes en el último mes. , Identifica lo que te ha ayudado hasta ahora para mantener niveles bajos de estrés y así poder seguir utilizando esos recursos cada vez que los necesites. Para que tu nivel no aumente, te recomendamos organizar tus tiempos acorde a las demandas, priorizar tareas y equilibrar tu estilo de vida con actividades que posibiliten tu recreación y descanso. Existen diferentes actividades realizadas por nuestro programa que te pueden interesar, como lo son los talleres de yoga para funcionarios ofrecidos de manera semanal.  Recuerda que puedes reservar tu cupo online. ";
        } else if (score <= 37) {
          recomendation = "En el último mes, puedes percibir que tu nivel de estrés se ha mantenido estable en el tiempo llevándote a sentir irritabilidad, cansancio, problemas para dormir, dolores de cabeza, entre otros. Es importante que puedas generar cambios para que tu nivel de percepción de estrés disminuya. Revisa las demandas que sean modificables y que estén bajo tu control, y asegúrate de complementar tus actividades con tiempo de descanso y recreación. Si tienes dudas en relación a este tema, contáctanos a ansiedad@uc.cl";
        } else {
          recomendation = "En el último mes la intensidad de tus síntomas ha logrado interferir en algunas áreas de tu vida.  Puedes estar mostrándote irritable, estar teniendo insomnio, pesadillas, sintiendo tensión muscular, agotamiento, ansiedad, falta de concentración y atención, dificultades para pensar, además de una disminución en tus defensas inmunitarias. Mantenerte en este estado puede perjudicarte aún más si las condiciones de tu entorno no se modifican. Te recomendamos solicitar una opinión profesional dentro del corto plazo para evaluar tu situación. Si tienes alguna duda en relación a tu resultado, escríbenos a ansiedad@uc.cl";
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
          recomendation = "Tus resultados indican que tienes pocos o ningún síntoma de ansiedad. Si bien eso puede ser beneficioso para ti, debes entender que la ansiedad es una emoción que te permite activarte, y que presentar bajos niveles puede generar conductas pasivas y falta de motivación. Si tienes dudas acerca de tu resultado o acerca de la ansiedad, escríbenos a ansiedad@uc.cl. ";
        } else if (score <= 14) {
          recomendation = "Tus resultados indican que presentas síntomas moderados de ansiedad. Según tus respuestas, estos síntomas pueden estar afectando tu diario vivir y generándote dificultades en una o más áreas de tu vida.  Te recomendamos hablar con un profesional para ver qué puedes hacer para que tus síntomas disminuyan. Es importante que sepas que presentar síntomas moderados de ansiedad no equivale a tener un trastorno de ansiedad. Puedes solicitar una consejería individual de ansiedad o participar de los talleres \"Manejando mi ansiedad\" a cargo de un psicólogo perteneciente al Programa para el Manejo de la Ansiedad y el buen dormir. Para mayor información, escríbenos a ansiedad@uc.cl ";
        } else {
          recomendation = "Tus resultados indican que estás teniendo sintomatología severa de ansiedad. Tus respuestas señalan que durante las últimas semanas, la ansiedad ha logrado interferir en gran medida en tu diario vivir. Te recomendamos consultar en la Unidad de Apoyo Psicológico ubicada en el 3er piso del Hall Universitario del campus San Joaquín para solicitar una hora con un profesional que te puede orientar con lo que estás sintiendo. Presentar síntomas severos de ansiedad no equivale a tener un trastorno de ansiedad. Para mayor información, puedes escribirnos a ansiedad@uc.cl o visitar el sitio http://apoyo.saludestudiantil.uc.cl/";
        }
        break;
      case 2: //Functionary
        if (score <= 9) {
          recomendation = "Tus resultados indican que tienes pocos o ningún síntoma de ansiedad. Si bien eso puede ser beneficioso para ti, debes entender que la ansiedad es una emoción que te permite activarte, y que presentar bajos niveles puede generar conductas pasivas y falta de motivación. Si tienes dudas acerca de tu resultado o acerca de la ansiedad, escríbenos a ansiedad@uc.cl. ";
        } else if (score <= 14) {
          recomendation = "Tus resultados indican que presentas síntomas moderados de ansiedad. Según tus respuestas, estos síntomas pueden estar afectando tu diario vivir y generándote dificultades en una o más áreas de tu vida.  Te recomendamos hablar con un profesional para ver qué puedes hacer para que tus síntomas disminuyan. Es importante que sepas que presentar síntomas moderados de ansiedad no equivale a tener un trastorno de ansiedad. Para mayor información, escríbenos a ansiedad@uc.cl";
        } else {
          recomendation = "Tus resultados indican que estás teniendo sintomatología severa de ansiedad. Tus respuestas señalan que durante las últimas semanas, la ansiedad ha logrado interferir en gran medida en tu diario vivir. Te recomendamos consultar a un profesional que te pueda orientar con lo que estás sintiendo. Presentar síntomas severos de ansiedad no equivale a tener un trastorno de ansiedad. Para mayor información, puedes escribirnos a ansiedad@uc.c";
        }
        break;

      default:
        break;
    }
    return recomendation;

  }

  public String getRecomendationToSleeping(int score, int role) {
    String recomendation;
    recomendation = "";

    switch (role) {
      case 1: //Student

        break;
      case 2: //Functionary

      default:
        break;
    }
    return recomendation;

  }


}
