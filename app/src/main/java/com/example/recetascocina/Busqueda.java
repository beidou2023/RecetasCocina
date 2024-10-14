package com.example.recetascocina;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Busqueda extends AppCompatActivity {

    EditText agregar,quitar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_busqueda);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        agregar=findViewById(R.id.ett_agregarIng);
        quitar=findViewById(R.id.ett_quitarIng);
    }

    public void irHome(View v){
        Intent it=new Intent(getApplicationContext(), Home.class);
        startActivity(it);
    }

    public void mostrarFiltro(View v){
        Intent it=new Intent(getApplicationContext(), BusquedaRes.class);
        String agregarR=agregar.getText().toString();
        String quitarR=quitar.getText().toString();
        it.putExtra("agrearIngrediente",agregarR);
        it.putExtra("quitarIngrediente",quitarR);
        startActivity(it);
    }
}