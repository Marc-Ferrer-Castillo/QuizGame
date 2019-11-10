package com.example.joc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import static com.example.joc.MainActivity.DIRECTORIO_IMAGENES;

public class Contenido extends AppCompatActivity {

    private final byte MAX_PLANETAS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido);

        ImageView continuar = findViewById(R.id.btncontinuar);
        ImageView imagenNarrador = findViewById(R.id.imagenNarrador);
        ImageButton volverMenu = findViewById(R.id.inicio);
        TextView informacion = findViewById(R.id.informacion);

        /*EL TEXTO SE CARGA DEL PLANTA planetaMostrado*/
        informacion.setText(MainActivity.planetas.get(MainActivity.planetaMostrado).getContenido());

        /*CARGA imagen3.png DEL DIRECTORIO imatges Y LO COLOCA EN EL imageview*/
        String fname = new File(DIRECTORIO_IMAGENES, "imagen3.png").getAbsolutePath();
        Bitmap myBitmap = BitmapFactory.decodeFile(fname);
        imagenNarrador.setImageBitmap(myBitmap);

        // Botón continuar
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Juego.class);
                // abre la activity del Juego
                startActivity(intent);
            }
        });

        // Botón salir
        volverMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                // abre la activity del menu principal
                startActivity(intent);

            }
        });


    }
}