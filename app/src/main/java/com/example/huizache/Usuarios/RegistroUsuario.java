package com.example.huizache.Usuarios;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.huizache.R;

import java.util.HashMap;
import java.util.Map;

public class RegistroUsuario extends AppCompatActivity {
    private Button btnRegistrarR, cancelar;
    private EditText nom, ap, am, user, emailR, pswR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);


        nom = findViewById(R.id.txtNombreR);
        ap = findViewById(R.id.txtApellidoPaternoR);
        am = findViewById(R.id.txtApellidoMaternoR);
        user = findViewById(R.id.txtUsuarioR);
        emailR = findViewById(R.id.txtCorreoR);
        pswR = findViewById(R.id.txtPswR);

        btnRegistrarR = findViewById(R.id.btnRegistrarR);
        btnRegistrarR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                existeU();
                //insertar();
            }
        });

        cancelar = findViewById(R.id.btnCancelarR);
        cancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroUsuario.this, Login.class));
            }
        });

        emailR.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });

        pswR.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });
    }

    private void insertar(){
        String nombre = nom.getText().toString().trim();
        String apellidoPaterno = ap.getText().toString().trim();
        String apellidoMaterno = am.getText().toString().trim();
        String username = user.getText().toString().trim();
        String email = emailR.getText().toString().trim();
        String psw = pswR.getText().toString().trim();


        if (nombre.isEmpty()){
            nom.setError("Complete los campos");
        } else if (apellidoPaterno.isEmpty()){
            ap.setError("Complete los campos");
        } else if (apellidoMaterno.isEmpty()){
            am.setError("Complete los campos");
        } else if (username.isEmpty()){
            user.setError("Complete los campos");
        } else if (email.isEmpty()){
            emailR.setError("Complete los campos");
        } else if (psw.isEmpty()){
            pswR.setError("Complete los campos");
        } else {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Registrando usuario");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://prueba9717.000webhostapp.com/serviciohuizache/Usuarios/usuariosCreate.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("Usuario insertado")) {
                        Toast.makeText(RegistroUsuario.this, "Usuario insertado", Toast.LENGTH_SHORT).show();
                        nom.setText("");
                        ap.setText("");
                        am.setText("");
                        user.setText("");
                        emailR.setText("");
                        pswR.setText("");
                        startActivity(new Intent(RegistroUsuario.this, Login.class));
                        progressDialog.dismiss();


                    } else {
                        Toast.makeText(RegistroUsuario.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegistroUsuario.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String>params= new HashMap<String, String>();
                    params.put("nombre", nombre);
                    params.put("apellidoPaterno", apellidoPaterno);
                    params.put("apellidoMaterno", apellidoMaterno);
                    params.put("username", username);
                    params.put("email", email);
                    params.put("psw", psw);

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(RegistroUsuario.this);
            requestQueue.add(stringRequest);
        }
    }

    public void existeU () {

        String nombre = nom.getText().toString().trim();
        String apellidoPaterno = ap.getText().toString().trim();
        String apellidoMaterno = am.getText().toString().trim();
        String username = user.getText().toString().trim();
        String email = emailR.getText().toString().trim();
        String psw = pswR.getText().toString().trim();


        if (nombre.isEmpty()) {
            nom.setError("Complete los campos");
        } else if (apellidoPaterno.isEmpty()) {
            ap.setError("Complete los campos");
        } else if (apellidoMaterno.isEmpty()) {
            am.setError("Complete los campos");
        } else if (username.isEmpty()) {
            user.setError("Complete los campos");
        } else if (email.isEmpty()) {
            emailR.setError("Complete los campos");
        } else if (psw.isEmpty()) {
            pswR.setError("Complete los campos");
        } else {
            String emailU = emailR.getText().toString().trim();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://prueba9717.000webhostapp.com/serviciohuizache/Usuarios/usuariosBuscar.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equalsIgnoreCase("No hay registro")) {
                        validarEmail(emailR);
                    } else {
                        Toast.makeText(RegistroUsuario.this, "El correo ya esta registrado, ingresa otro", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", emailU);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(RegistroUsuario.this);
            requestQueue.add(stringRequest);
        }
    }


    public void validarEmail(EditText emailValidate) {

        String emailV = emailValidate.getText().toString();
        if (!emailV.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailV).matches()) {
            insertar();
        } else {
            emailR.setError("Correo electronico no valido");
            //Toast.makeText(this, "Correo electronico no valido", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed(){
        //No hace nada
    }
}