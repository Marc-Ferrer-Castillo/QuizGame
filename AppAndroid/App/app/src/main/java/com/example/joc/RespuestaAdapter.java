package com.example.joc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.List;

public class RespuestaAdapter extends ArrayAdapter {
    Context contexto;
    List<Respuesta> respuestas;


    public RespuestaAdapter(@NonNull Context contexto, List<Respuesta> respuestas) {
        super(contexto, R.layout.place_respuesta, respuestas);

        this.contexto = contexto;
        this.respuestas = respuestas;
    }

    public View getView(int numRespuesta, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.place_respuesta, null,true);

        TextView campoRespuesta = rowView.findViewById(R.id.placeRespuesta);

        campoRespuesta.setText( MainActivity.planetas.get(MainActivity.planetaMostrado).
                getPreguntas().get(Juego.numPregunta).getRespuestas().get(numRespuesta).getRespuesta());

        return(rowView);
    }

}
