package com.example.joc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

public class Juego extends AppCompatActivity {

    //Iterador de preguntas
    public static int numPregunta = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        // Objeto GridView donde iran las respuestas
        ListView gridRespuestas = findViewById(R.id.gridRespuestas);

        // Objeto Textview donde ira la pregunta
        TextView preguntaView = findViewById(R.id.pregunta);

        // Contiene el string con la pregunta numPregunta de planetaMostrado
        String pregunta = MainActivity.planetas.get(MainActivity.planetaMostrado).
                getPreguntas().get(numPregunta).getPregunta();

        // coloca la pregunta en el textView
        preguntaView.setText(pregunta);

        // Lista de respuestas de la pregunta
        final List<Respuesta> respuestas = MainActivity.planetas.get(MainActivity.planetaMostrado).
                getPreguntas().get(numPregunta).getRespuestas();

        // Instancia nuestro adaptador personalizado
        RespuestaAdapter adaptador = new RespuestaAdapter(this, respuestas);

        // La grid usa ahora este adaptador personalizado
        gridRespuestas.setAdapter(adaptador);



        // Al pulsar sobre una respuesta
        gridRespuestas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // El maximo de preguntas será igual al numero de preguntas que contenga el planeta
                int maxPreguntas = MainActivity.planetas.get(MainActivity.planetaMostrado).
                        getPreguntas().size();

                // Como mucho hay 3 planetas por idioma
                byte ultimoPlaneta = 3;

                // Instanciamos un intent con el contexto y la clase destinataria
                Intent intent = new Intent(Juego.this, Contenido.class);
                Intent intentResultado = new Intent(Juego.this, Resultado.class);



                // Si quedan mas preguntas en el planeta
                if (numPregunta < maxPreguntas-1){

                    // Incrementamos 1 para mostrar la siguiente
                    numPregunta++;

                    // recarga la actividad
                    recreate();
                }

                //Si no quedan mas preguntas en el planeta
                else{
                    // Si no es el último planeta
                    if (MainActivity.planetaMostrado < ultimoPlaneta - 1){

                        // Incrementamos 1 para mostrar el siguiente
                        MainActivity.planetaMostrado++;

                        // Reiniciamos el iterador de preguntas para que muestre la priemera en el siguiente planeta
                        numPregunta = 0;

                        // Vuelve para mostrar el contenido
                        startActivity(intent);
                    }

                    //Si es el último planeta
                    else{
                        // Muestra el activity resultado
                        startActivity(intentResultado);
                    }
                }



            }
        });

    }
}