package com.example.recetascocina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText user,password;
    String userP="",passP="";
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String usuarioId = sharedPreferences.getString("usuario_id", null);

        if (usuarioId == null) {
            editor.putString("usuario_id", null);
            editor.apply();
        }


        user=findViewById(R.id.edt_user);
        password=findViewById(R.id.edt_password);
    }


    public void Ingresar(View v){
        String URL=Urls.REMOTO_VERIFICAR_INICIO_URL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        editor.putString("usuario_id", jsonResponse.getString("idUsuario"));
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrecto", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error en la respuesta del servidor", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = "";
                if (error instanceof TimeoutError) {
                    message = "Error: Timeout";
                } else if (error instanceof NoConnectionError) {
                    message = "Error: No hay conexión";
                } else if (error instanceof AuthFailureError) {
                    message = "Error: Autenticación fallida";
                } else if (error instanceof ServerError) {
                    message = "Error: Problema con el servidor";
                } else if (error instanceof NetworkError) {
                    message = "Error: Problema de red";
                } else if (error instanceof ParseError) {
                    message = "Error: Problema al parsear la respuesta";
                }

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Log.e("VolleyError", "Error: " + error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("Usuario", user.getText().toString());
                parametros.put("Contrasena", password.getText().toString());
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}