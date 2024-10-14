package com.example.recetascocina;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    EditText user,password;
    String userP="",passP="";

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
        user=findViewById(R.id.edt_user);
        password=findViewById(R.id.edt_password);
    }

    public void Ingresar(View v){
        if(user.getText().toString().equals(userP)){
            if(password.getText().toString().equals(passP)){
                Intent it=new Intent(getApplicationContext(), Home.class);
                startActivity(it);
            }
            else{
                Toast.makeText(getApplicationContext(),"Contraseña Incorrecta",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Usuario Incorrecto",Toast.LENGTH_LONG).show();
        }
    }
}