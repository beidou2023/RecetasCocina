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

public class Home extends AppCompatActivity {

    RecyclerView recyclerMasVisitados,recyclerMiHistorial;
    String idUsuario;
    AdapterRecetas adapterMasVisitados,adapterMiHistorial;
    List<Object[]> items1,items2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerMiHistorial=findViewById(R.id.rv_miHistorial);
        recyclerMasVisitados=findViewById(R.id.rv_masVisitados);
        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioPrefs", MODE_PRIVATE);
        idUsuario = sharedPreferences.getString("usuario_id", null);
        verMasVisitados();
        verMiHistorial();
    }

    private void verMasVisitados(){
        String URL=Urls.REMOTO_MAS_VISITADOS_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        JSONArray dataArray = jsonResponse.getJSONArray("data");
                        items1 = new ArrayList<>();
                        recyclerMasVisitados.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject carta = dataArray.getJSONObject(i);

                            String col0 = carta.getString("idCarta");
                            String col1 = carta.getString("NombreEmpresa");
                            String col2 = carta.getString("Fecha");
                            String col3 = carta.getString("NumeroCarta");
                            String col4 = carta.getString("Observaciones");

                            items1.add(new Object[]{col0,col1,col2,col3,col4});
                        }
                        adapterMasVisitados = new AdapterRecetas(Home.this,items1);
                        recyclerMasVisitados.setAdapter(adapterMasVisitados);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Home.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Home.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    private void verMiHistorial(){
        String URL=Urls.REMOTO_HISTORIAL_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        JSONArray dataArray = jsonResponse.getJSONArray("data");
                        items2 = new ArrayList<>();
                        recyclerMiHistorial.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject carta = dataArray.getJSONObject(i);

                            String col0 = carta.getString("idCarta");
                            String col1 = carta.getString("NombreEmpresa");
                            String col2 = carta.getString("Fecha");
                            String col3 = carta.getString("NumeroCarta");
                            String col4 = carta.getString("Observaciones");

                            items1.add(new Object[]{col0,col1,col2,col3,col4});
                        }
                        adapterMiHistorial = new AdapterRecetas(Home.this,items2);
                        recyclerMiHistorial.setAdapter(adapterMiHistorial);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Home.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Home.this, error.toString(), Toast.LENGTH_SHORT).show();
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