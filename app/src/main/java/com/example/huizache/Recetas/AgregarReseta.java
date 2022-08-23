package com.example.huizache.Recetas;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.huizache.Bebidas.AgregarBebidas;
import com.example.huizache.Bebidas.MenuBebidas;
import com.example.huizache.Imagenes.CropperActivity;
import com.example.huizache.Menu;
import com.example.huizache.Modelo.UsuariosDAO;
import com.example.huizache.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgregarReseta extends AppCompatActivity {

    CircleImageView ivImagen;
    EditText txtNombre,txtIngredientes, txtDescripcion;
    FloatingActionButton btnGuardar;
    ActivityResultLauncher<String> mGetContent;
    Uri resultUri;
    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;
    UsuariosDAO u;
    ImageButton btnAtrasAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_reseta);

        //Toast.makeText(AgregarReseta.this, "idUsuario: "+ id, Toast.LENGTH_SHORT).show();

        ivImagen = findViewById(R.id.btnAgregarImagenResetas);
        txtNombre = findViewById(R.id.txtAgregarNombreReseta);
        txtIngredientes = findViewById(R.id.txtAgregarIngredientesReseta);
        btnGuardar = findViewById(R.id.btnAGuardarReseta);
        txtDescripcion = findViewById(R.id.txtAgregarDescripcionReseta);
        btnAtrasAR = findViewById(R.id.btnAtrasAR);

        ivImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // muestra el cuadro de diálogo de selección de imagen
                mGetContent.launch("image/*");
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AgregarReseta.this, "Id"+usuariosDAO.getId(), Toast.LENGTH_SHORT).show();
                if(resultUri == null){
                    Toast.makeText(AgregarReseta.this, "Inserte una imagen", Toast.LENGTH_SHORT).show();
                }else {
                    insertar();
                }

                //
            }
        });

        mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent= new Intent(AgregarReseta.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }
        });

        btnAtrasAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AgregarReseta.this, MenuResetas.class));
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==-1 && requestCode==101){
            String result=data.getStringExtra("RESULT");
            resultUri=null;
            if (result!=null){
                resultUri=Uri.parse(result);
            }
            ivImagen.setImageURI(resultUri);

        }


        if(requestCode == RECOGNIZE_SPEECH_ACTIVITY){
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> speech = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String strSpeech2Text = speech.get(0);
                txtDescripcion.setText(strSpeech2Text);
            }
        }
    }


    public void onClickbtnSensor(View view) {
        Intent intentActionRecognizeSpeech = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Configura el Lenguaje (Español-México)
        intentActionRecognizeSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-MX");
        try {
            startActivityForResult(intentActionRecognizeSpeech,
                    RECOGNIZE_SPEECH_ACTIVITY);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Tú dispositivo no soporta el reconocimiento por voz",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void insertar(){
        String nombre = txtNombre.getText().toString().trim();
        String imagen = resultUri.toString();
        String ingredientes = txtIngredientes.getText().toString().trim();
        String descripcion = txtDescripcion.getText().toString().trim();
        String idUsuario = u.getId();
        //Toast.makeText(AgregarReseta.this, "imagen "+imagen, Toast.LENGTH_SHORT).show();

        ProgressDialog progressDialog = new ProgressDialog(this);
        if (nombre.isEmpty()){
            txtNombre.setError("Complete los campos");
        } else if (ingredientes.isEmpty()){
            txtIngredientes.setError("Complete los campos");
        } else if (descripcion.isEmpty()){
            txtDescripcion.setError("Complete los campos");
        } else {
            progressDialog.setMessage("Agregando receta");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://prueba9717.000webhostapp.com/serviciohuizache/Recetas/recetasCrear.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("Receta insertada")) {

                        Toast.makeText(AgregarReseta.this, "Receta insertada", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AgregarReseta.this, Menu.class));
                        progressDialog.dismiss();


                    } else {
                        Toast.makeText(AgregarReseta.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AgregarReseta.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String>params= new HashMap<String, String>();
                    params.put("nombre", nombre);
                    params.put("imagen", imagen);
                    params.put("ingredientes", ingredientes);
                    params.put("descripcion", descripcion);
                    params.put("idUsuario", idUsuario);

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(AgregarReseta.this);
            requestQueue.add(stringRequest);
        }


    }

    @Override
    public void onBackPressed(){
        //No hace nada
    }

}