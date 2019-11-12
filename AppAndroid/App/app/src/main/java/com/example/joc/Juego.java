package com.example.joc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

public class Juego extends AppCompatActivity {

    // Iterador de preguntas
    public static int numPregunta = 0;
    // Lista con las respuestas sin filtrar por dificultad
    private List<Respuesta> respuestas =  new ArrayList<Respuesta>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        // Objeto GridView donde iran las respuestas
        final GridView gridRespuestas = findViewById(R.id.listRespuestas);

        // Objeto Textview donde ira la pregunta
        TextView preguntaView = findViewById(R.id.pregunta);

        // Contiene las preguntas
        List<Pregunta> preguntas = MainActivity.planetas.get(MainActivity.planetaMostrado).getPreguntas();

        // Guarda las preguntas según la dificultad seleccionada
        List<Pregunta> preguntasFiltradas = new ArrayList<Pregunta>();


        // Dificultad de juego: Facil
        if (Dificultad.dificultadSeleccionada){

            // Recorre la lista de preguntas
            for (int i = 0 ; i < preguntas.size() ; i++){

                // Si la pregunta es facil
                if (preguntas.get(i).isDificultad()){

                    // Se añade a la lista de preguntas filtradas
                    preguntasFiltradas.add(preguntas.get(i) );
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

        // Si tras filtrar las preguntas por dificultad hay más de 0
        if(!(preguntasFiltradas.size() == 0)) {

            // Se carga la pregunta y sus respuestas
            cargarContenido( preguntaView, preguntasFiltradas, gridRespuestas );
        }
        // Si el planeta no contiene preguntas con la dificultad
        else{
            // Se pasa al siguiente planeta
            pasarPlaneta();
        }



        // Al pulsar sobre una respuesta
        gridRespuestas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // El maximo de preguntas será igual al numero de preguntas que contenga el planeta
                int maxPreguntas = MainActivity.planetas.get(MainActivity.planetaMostrado).
                        getPreguntas().size();

                // Guarda la respuesta seleccionada por el usuario
                Integer respuestaSeleccionada = (Integer)view.getTag();

                // Si la respuesta es correcta
                if (respuestas.get(respuestaSeleccionada).isEsCorrecta() ){

                    Resultado.aciertos++;

                    Toast.makeText(Juego.this, "ACIERTO",
                            Toast.LENGTH_LONG).show();
                }

                // Si quedan mas preguntas en el planeta
                if (numPregunta < maxPreguntas-1){

                    // Incrementamos 1 para mostrar la siguiente
                    numPregunta++;

                    // Recarga la actividad
                    recreate();
                }

                // Si no quedan mas preguntas en el planeta
                else{
                    pasarPlaneta();
                }
            }
        });
    }

    // Carga la pregunta en el textview y sus respuestas en el gridview
    private void cargarContenido(TextView preguntaView, List<Pregunta> preguntasFiltradas, GridView gridRespuestas) {

        // coloca la pregunta en el textView
        preguntaView.setText(preguntasFiltradas.get(numPregunta).getPregunta());

        // Lista de respuestas de la pregunta
        respuestas = preguntasFiltradas.get(numPregunta).getRespuestas();

        // Instancia nuestro adaptador personalizado
        RespuestaAdapter adaptador = new RespuestaAdapter(this, respuestas);

        // La grid usa ahora este adaptador personalizado
        gridRespuestas.setAdapter(adaptador);
    }

    // Pasa al siguiente planeta. Si es el último, abre la activity Resultado
    private void pasarPlaneta(){

        // Intents
        Intent contenido = new Intent(Juego.this, Contenido.class);
        Intent Resultado = new Intent(Juego.this, Resultado.class);

        // El máximo de preguntas será igual al número de preguntas que contenga el planeta
        int maxPreguntas = MainActivity.planetas.get(MainActivity.planetaMostrado).
                getPreguntas().size();

        // Si no es el último planeta
        if (MainActivity.planetaMostrado < MainActivity.ultimoPlaneta ){

            // Incrementamos 1 para mostrar el siguiente
            MainActivity.planetaMostrado++;

            // Reiniciamos el iterador de preguntas para que muestre la priemera del siguiente planeta
            numPregunta = 0;

            // Abre la activity contenido
            startActivity(contenido);
        }
        // Si es el último planeta
        else{
            // Abre la activity Resultado
            startActivity(Resultado);
        }



    }
}
