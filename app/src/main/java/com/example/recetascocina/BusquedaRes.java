package com.example.recetascocina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusquedaRes extends AppCompatActivity {

    String agregarR;
    String quitarR;

    RecyclerView recyclerBusqueda;
    String idUsuario;
    AdapterRecetas adapterBusqueda;
    List<Object[]> items;

    TextView nroRecetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_busqueda_res);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nroRecetas=findViewById(R.id.txv_nroBusquedaRecetas);
        recyclerBusqueda=findViewById(R.id.rv_resultado);
        agregarR=getIntent().getStringExtra("agrearIngrediente");
        quitarR=getIntent().getStringExtra("quitarIngrediente");
        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioPrefs", MODE_PRIVATE);
        idUsuario = sharedPreferences.getString("usuario_id", null);
        verBusqueda();
    }

    private void verBusqueda(){
        String URL=Urls.REMOTO_BUSQUEDA_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        JSONArray dataArray = jsonResponse.getJSONArray("data");
                        items = new ArrayList<>();
                        recyclerBusqueda.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject carta = dataArray.getJSONObject(i);

                            String col0 = carta.getString("id");
                            String col1 = carta.getString("titulo");
                            String col2 = carta.getString("imagen");
                            String col3 = carta.getString("username");
                            String col4 = carta.getString("Uimagen");

                            items.add(new Object[]{col0,col1,col2,col3,col4});
                        }
                        nroRecetas.setText(String.valueOf(items.size()));
                        adapterBusqueda = new AdapterRecetas(BusquedaRes.this,items);
                        recyclerBusqueda.setAdapter(adapterBusqueda);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(BusquedaRes.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerBusqueda.setLayoutManager(layoutManager);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(BusquedaRes.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BusquedaRes.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("agregarR", agregarR);
                parametros.put("quitarR", quitarR);
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
        Intent it=new Intent(getApplicationContext(), PRecetas.class);
        startActivity(it);
    }
    public void goCrearReceta(View v){
        Intent it=new Intent(getApplicationContext(), CrearReceta.class);
        startActivity(it);
    }
}