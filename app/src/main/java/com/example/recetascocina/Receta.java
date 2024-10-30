package com.example.recetascocina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Receta extends AppCompatActivity {

    TextView nombreReceta,autor,comentarios,duracion,ingredientes,preIngredientes,utencilios,pasos;
    ImageView imgRecetaa,imgUsuario;

    String idUsuario,idReceta,urlImgReceta,urlImgUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioPrefs", MODE_PRIVATE);
        idUsuario = sharedPreferences.getString("usuario_id", "1");

        idReceta=getIntent().getStringExtra("idReceta");

        imgRecetaa=findViewById(R.id.img_VReceta);
        imgUsuario=findViewById(R.id.img_VUsuario);

        nombreReceta=findViewById(R.id.txv_VnombreReceta);
        autor=findViewById(R.id.txv_Vautor);
        comentarios=findViewById(R.id.txv_Vcomentarios);
        duracion=findViewById(R.id.txv_Vduracion);
        ingredientes=findViewById(R.id.txv_Vingredientes);
        preIngredientes=findViewById(R.id.txv_Vprereceta);
        utencilios=findViewById(R.id.txv_Vutencilios);
        pasos=findViewById(R.id.txv_Vpasos);
        verReceta();
    }

    private void verReceta(){
        String URL=Urls.REMOTO_VER_RECETA_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        JSONArray dataArray = jsonResponse.getJSONArray("datos");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject receta = dataArray.getJSONObject(i);

                            String titulo = receta.getString("titulo");
                            String comentario = receta.getString("comentario");
                            String duracionn = receta.getString("duracion");
                            String imagen = receta.getString("imagen");
                            if(!imagen.equals("null")){
                                urlImgReceta=Urls.BASE_URL+imagen;
                                Glide.with(getApplicationContext())
                                        .load(urlImgReceta)
                                        .into(imgRecetaa);
                            }
                            String username = receta.getString("username");
                            String Uimagen = receta.getString("Uimagen");
                            if(!Uimagen.equals("null")){
                                urlImgUsuario=Urls.BASE_URL+Uimagen;
                                Glide.with(getApplicationContext())
                                        .load(urlImgUsuario)
                                        .into(imgUsuario);
                            }

                            nombreReceta.setText(titulo);
                            autor.setText(username);
                            if(comentario.equals("null"))
                                comentario="Sin comentarios";
                            comentarios.setText(comentario);
                            duracion.setText(duracionn+" minutos");
                        }

                        dataArray = jsonResponse.getJSONArray("ingredientes");
                        StringBuilder cadenaAux= new StringBuilder();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject receta = dataArray.getJSONObject(i);
                            String ingrediente = receta.getString("nombre");
                            cadenaAux.append(i+1).append(".- ").append(ingrediente).append("\n");
                        }
                        ingredientes.setText(cadenaAux.toString());

                        if(jsonResponse.getBoolean("vUtencilios")){
                            dataArray = jsonResponse.getJSONArray("utencilios");
                            cadenaAux= new StringBuilder();
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject receta = dataArray.getJSONObject(i);
                                String utencilio = receta.getString("nombre");
                                cadenaAux.append(i+1).append(".- ").append(utencilio).append("\n");
                            }
                            utencilios.setText(cadenaAux.toString());
                        }

                        if(jsonResponse.getBoolean("vpreIngredientes")){
                            dataArray = jsonResponse.getJSONArray("preIngredientes");
                            cadenaAux= new StringBuilder();
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject receta = dataArray.getJSONObject(i);
                                String preIngr = receta.getString("descripcion");
                                cadenaAux.append(i+1).append(".- ").append(preIngr).append("\n");
                            }
                            preIngredientes.setText(cadenaAux.toString());
                        }
                        else{
                            preIngredientes.setText("Sin pre-ingredientes");
                        }

                        dataArray = jsonResponse.getJSONArray("pasos");
                        cadenaAux= new StringBuilder();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject receta = dataArray.getJSONObject(i);
                            String paso=receta.getString("descripcion");
                            cadenaAux.append(i+1).append(".- ").append(paso).append("\n");
                        }
                        pasos.setText(cadenaAux.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Receta.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Receta.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idReceta", idReceta);
                parametros.put("idUsuario", idUsuario);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
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