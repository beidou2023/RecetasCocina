package com.example.recetascocina;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PRecetas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_precetas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }




    public void goHome(View v){
        Intent it=new Intent(getApplicationContext(), Home.class);
        startActivity(it);
    }
    public void goBusqueda(View v){
        Intent it=new Intent(getApplicationContext(), Busqueda.class);
        startActivity(it);
    }
    public void goPFavoritos(View v){
        Intent it=new Intent(getApplicationContext(), PFavoritos.class);
        startActivity(it);
    }
    public void goCrearReceta(View v){
        Intent it=new Intent(getApplicationContext(), CrearReceta.class);
        startActivity(it);
    }
}