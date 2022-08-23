package com.example.huizache.Usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.ActionMode;
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
import com.example.huizache.Menu;
import com.example.huizache.Modelo.UsuariosDAO;
import com.example.huizache.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {
/*
Variables que se usarona para las accines de la interfaz
 */
    private Context context;
    Button btnIniciarSesion, btnCrearCuenta, btnHuella;
    EditText email, psw;
    String str_email, str_psw, idUsuario;
    public static ArrayList<UsuariosDAO> usuariosDAOLista = new ArrayList<>();
    UsuariosDAO usuariosDAO = new UsuariosDAO();

    /*
Variable url que contiene el servicio php que ocupa la aplicacion en 000webhost
 */
    String url = "https://prueba9717.000webhostapp.com/serviciohuizache/Usuarios/usuariosLogear.php";


/*
Variables que fueron utililes para la identificacion de huella digital
 */
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.txtCorreoL);
        psw = findViewById(R.id.txtPswL);


        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Iniciar la Activity
                startActivity(new Intent(Login.this, RegistroUsuario.class));
            }
        });

        psw.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });

        email.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });

    }

    public void Login (View view){
        if(email.getText().toString().equals("")) {
            email.setError("Complete los campos");
        } else if (psw.getText().toString().equals("")) {
            psw.setError("Complete los campos");
        } else {

            str_email = email.getText().toString().trim();
            str_psw = psw.getText().toString().trim();

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Iniciando Sesión");

            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();

                    if (response.equalsIgnoreCase("Sesión iniciada")) {
                        buscarID(str_email,str_psw);
                        //startActivity(new Intent(Login.this, Menu.class));
                        Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String>params= new HashMap<String, String>();
                    params.put("email", str_email);
                    params.put("psw", str_psw);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
            requestQueue.add(stringRequest);
        }
    }

    private void buscarID(String emailRe, String pswRe){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://prueba9717.000webhostapp.com/serviciohuizache/Usuarios/usuariosID.php?email="+email.getText().toString().trim()+"&psw="+psw.getText().toString().trim(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject Recetas = array.getJSONObject(i);
                        idUsuario = Recetas.getString("idUsuario");
                    }

                    usuariosDAO.setId(idUsuario);
                    //Toast.makeText(Login.this, "idUsuario"+ idUsuario, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(Login.this, Menu.class));
                    //email.setText("");
                    //psw.setText("");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);

    }
    @Override
    public void onBackPressed(){
        //No hace nada
    }

}