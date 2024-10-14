package com.example.recetascocina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PFavoritos extends AppCompatActivity {

    RecyclerView recyclerFavoritos;
    String idUsuario;
    AdapterRecetas adapterFavoritos;
    List<Object[]> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pfavoritos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerFavoritos=findViewById(R.id.rv_favoritos);
        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioPrefs", MODE_PRIVATE);
        idUsuario = sharedPreferences.getString("usuario_id", null);
        verFavoritos();
    }

    private void verFavoritos(){
        String URL=Urls.REMOTO_FAVORITOS_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        JSONArray dataArray = jsonResponse.getJSONArray("data");
                        items = new ArrayList<>();
                        recyclerFavoritos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject carta = dataArray.getJSONObject(i);

                            String col0 = carta.getString("id");
                            String col1 = carta.getString("titulo");
                            String col2 = carta.getString("imagen");
                            String col3 = carta.getString("username");
                            String col4 = carta.getString("Uimagen");

                            items.add(new Object[]{col0,col1,col2,col3,col4});
                        }
                        adapterFavoritos = new AdapterRecetas(PFavoritos.this,items);
                        recyclerFavoritos.setAdapter(adapterFavoritos);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PFavoritos.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PFavoritos.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
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