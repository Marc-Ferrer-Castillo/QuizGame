package com.example.joc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class Juego extends AppCompatActivity {
/*
 1. Recibimos la primera pregunta a mostrar con las variables:
    MainActivity.getPlanetaMostrado() y preguntaMostrada

 2. Filtrar las preguntas y guardarlas en una lista nueva

 3. Rellenar(); <-- Muestra la pregunta en el textview y sus respuestas en la gridview

 4. OnClick respuesta:
    4.1 Si hay más preguntas se muestra la siguiente y llamamos a rellenar()
    4.2 Si no hay más preguntas pasamos de planeta (si quedan)
*/

    private static final byte RESULTADO_ACTIVIDAD = 1;
    private static int aciertos;
    private static int preguntaMostrada = 0;
    private static int planetaMostrado;
    private boolean dificultadSeleccionada;
    private List<Pregunta> preguntasFiltradas = new ArrayList<Pregunta>();

    private CountDownTimer contador = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            aciertos = 0;
            reiniciarPreguntas();
            // envia result_OK y Cierra esta actividad
            setResult(Resultado.RESULT_OK);
            // Vuelve al main
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);


        // Lista de planetas
        final List<Planeta> planetas = Importar.getPlanetas();

        Intent intentDoble = getIntent();
        planetaMostrado = intentDoble.getIntExtra("planetaMostrado", 0);
        dificultadSeleccionada = intentDoble.getBooleanExtra("dificultad", false);

        filtrarPreguntas( planetas.get(planetaMostrado).getPreguntas() );

        final GridView gridRespuestas = findViewById(R.id.gridRespuestas);

        cargarContenido(gridRespuestas);

        // Imagen Salir
        ImageView salir = findViewById(R.id.inicio);

        // Click en salir
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Devuelve RESULT OK a la clase Contenido
                setResult(Contenido.RESULT_OK);

                // Cierra esta actividad
                finish();
            }
        });


        final RelativeLayout juegoLayout;
        juegoLayout = findViewById(R.id.juegoLayout);

        // Al pulsar sobre un item del grid
        gridRespuestas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                contador.cancel();

                gridRespuestas.setEnabled(false);
                juego(position, juegoLayout, gridRespuestas);


            }
        });

    }

    private void juego(int position, RelativeLayout juegoLayout, final GridView gridRespuestas ) {

        // Siguiente Pregunta
        if ( preguntaMostrada < preguntasFiltradas.size() - 1 ){

            if (preguntasFiltradas.get(preguntaMostrada).getRespuestas().get(position).isEsCorrecta()) {
                Resultado.setAciertos(aciertos++);

                mostrarSnackBar(juegoLayout);

                // Pausa antes de pasar a la siguiente pregunta
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        gridRespuestas.setEnabled(true);
                        preguntaMostrada++;
                        cargarContenido(gridRespuestas);
                    }
                }, 2000);   //5 seconds

            }
            else{
                gridRespuestas.setEnabled(true);
                preguntaMostrada++;
                cargarContenido(gridRespuestas);
            }

        }
        // Si no quedan mas preguntas
        else{

            if (preguntasFiltradas.get(preguntaMostrada).getRespuestas().get(position).isEsCorrecta()) {
                Resultado.setAciertos(aciertos++);

                mostrarSnackBar(juegoLayout);
            }

            planetaMostrado++;

            // Siguiente planeta
            if ( planetaMostrado <= MainActivity.getUltimoPlaneta() ){

                // Devuelve RESULT OK a la clase Dificultad
                setResult(Contenido.RESULT_FIRST_USER);

                reiniciarPreguntas();

                //Cierra esta actividad
                finish();
            }
            // Pantalla resultado
            else{
                reiniciarPreguntas();

                Intent intentResultado = new Intent(getApplicationContext(), Resultado.class);

                intentResultado.putExtra("planetaMostrado", planetaMostrado);
                // abre la activity del Juego

                startActivityForResult(intentResultado, RESULTADO_ACTIVIDAD );
            }

        }
    }

    private void mostrarSnackBar(RelativeLayout juegoLayout) {
        // Muestra un snackbar

        // Instancia un snackbar
        Snackbar snackbar = Snackbar.make(juegoLayout,getString(R.string.respuestaCorrecta),Snackbar.LENGTH_SHORT);

        // Colores del Snackbar
        View snackbarView = snackbar.getView();
        TextView snackText =  snackbarView.findViewById(R.id.snackbar_text);

        // Color del texto
        // Dependiendo de la versión de android
        snackText.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            snackText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            snackText.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        // Color de fondo
        snackbarView.setBackgroundColor(Color.parseColor("#355e4e"));

        // Tamaño del texto
        snackText.setTextSize(50);

        // Tamaño del snackbar
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout)snackbar.getView();
        layout.setMinimumHeight(60);//your custom height.
        layout.setMinimumWidth(400);


        // Display the Snackbar
        snackbar.show();
    }

    private void cargarContenido(final GridView gridRespuestas) {

        List<Respuesta> respuestas = preguntasFiltradas.get(preguntaMostrada).getRespuestas();

        TextView viewPregunta = findViewById(R.id.pregunta);

        viewPregunta.setText(preguntasFiltradas.get(preguntaMostrada).getPregunta());

        //Instancia nuestro adaptador personalizado
        Adaptador adaptador = new Adaptador(this, respuestas);

        gridRespuestas.setAdapter(adaptador);

        contador.start();
    }

    private void filtrarPreguntas(List<Pregunta> preguntas) {

        // Dificultad de juego: Facil
        if (!dificultadSeleccionada){

            // Recorre la lista de preguntas
            for (int i = 0 ; i < preguntas.size() ; i++){

                // Si la pregunta es facil
                if (preguntas.get(i).isDificultad()){

                    // Se añade a la lista de preguntas filtradas
                    preguntasFiltradas.add( preguntas.get(i) );
                }
            }
        }
        //Dificultad de juego: Dificil
        else{

            // Recorre la lista de preguntas
            for (int i = 0 ; i <  preguntas.size() ; i++){

                // Si la pregunta es dificil
                if ( ! preguntas.get(i).isDificultad() ){

                    // Se añade a la lista de preguntas filtradas
                    preguntasFiltradas.add(preguntas.get(i) );
                }
            }
        }


    }

    // Resultado de startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Si la actividad de la que volvemos
        if (requestCode == RESULTADO_ACTIVIDAD) {

            // Y devuelve RESULT_OK
            if (resultCode == RESULT_OK) {

                // Devuelve RESULT OK a la clase Dificultad
                setResult(Contenido.RESULT_OK);

                aciertos = 0;
                reiniciarPreguntas();


                // Cerramos esta actividad también
                finish();
            }


        }
    }

    private void reiniciarPreguntas() {
        preguntasFiltradas.clear();
        preguntaMostrada = 0;
    }
}
