package com.example.recetascocina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

import java.util.HashMap;
import java.util.Map;

public class CrearReceta extends AppCompatActivity {

    ImageView receta;
    EditText nnombreReceta,ccomentario,dduracio,iingrediente,ppreingrediente,uutensilios,ppasos;
    String idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_receta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioPrefs", MODE_PRIVATE);
        idUsuario = sharedPreferences.getString("usuario_id", null);

        nnombreReceta=findViewById(R.id.edt_CnombreReceta );
        ccomentario=findViewById(R.id.edt_Ccomentarios );
        dduracio=findViewById(R.id.edt_Cduracion );
        iingrediente=findViewById(R.id.edt_iingrediente );
        ppreingrediente=findViewById(R.id.edt_ppreingrediente );
        uutensilios=findViewById(R.id.edt_uutencilios );
        ppasos=findViewById(R.id.edt_ppasos );
    }
    public void CrearRecetaa(View v){
        if(nnombreReceta.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Debe ingresar un nombre a la receta",Toast.LENGTH_SHORT).show();
        }
        else if(dduracio.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"La receta debe tener una duracion",Toast.LENGTH_SHORT).show();
        }
        else if(iingrediente.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"La receta debe tener al menos un ingrediente",Toast.LENGTH_SHORT).show();
        }
        else if(ppasos.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"La receta debe tener la menos un paso",Toast.LENGTH_SHORT).show();
        }
        else{
            String URL=Urls.REMOTO_CREAR_RECETA_URL;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            Toast.makeText(CrearReceta.this, "Se creo la receta con exito", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CrearReceta.this, Home.class);
                            startActivity(intent);
                        }
                        else{
                            String erore=jsonResponse.getString("numError");
                            Toast.makeText(CrearReceta.this, "No se creo nada "+erore, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CrearReceta.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(CrearReceta.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws    AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("idUsuario", idUsuario);
                    parametros.put("nombreReceta", nnombreReceta.getText().toString());
                    parametros.put("comentario", ccomentario.getText().toString());
                    parametros.put("duracion", dduracio.getText().toString());

                    parametros.put("ingrediente", iingrediente.getText().toString());
                    parametros.put("preingrediente", ppreingrediente.getText().toString());
                    parametros.put("utensilios", uutensilios.getText().toString());
                    parametros.put("pasos", ppasos.getText().toString());
                    return parametros;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
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