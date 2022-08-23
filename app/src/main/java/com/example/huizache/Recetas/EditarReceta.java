package com.example.huizache.Recetas;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.huizache.Imagenes.CropperActivity;
import com.example.huizache.Menu;
import com.example.huizache.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditarReceta extends AppCompatActivity {

    EditText txtNombreA, txtIngredientesA, txtDescripcionA;
    String recordID;

    CircleImageView ivImagenA;
    FloatingActionButton btnActualizarGuardar;
    ActivityResultLauncher<String> mGetContentA;
    Uri resultUriA, ur;
    private static final int RECOGNIZE_SPEECH_ACTIVITY_A = 1;
    ImageButton btnAtrasER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_receta);
        //id de la receta

        Intent intent = getIntent();
        recordID = intent.getStringExtra("RECORD_ID");


        //Toast.makeText(EditarReceta.this, "idUsuario= "+ recordID, Toast.LENGTH_SHORT).show();
        //Relacionar botones con la vista
        ivImagenA = findViewById(R.id.btnActualizarImagenResetas);
        txtNombreA = findViewById(R.id.txtActualizarNombreReseta);
        txtIngredientesA = findViewById(R.id.txtActualizarIngredientesReseta);
        btnActualizarGuardar = findViewById(R.id.btnActualizarReseta);
        txtDescripcionA = findViewById(R.id.txtActualizarDescripcionReseta);
        btnAtrasER = findViewById(R.id.btnAtrasER);

        mostrarDetalles();
        //SelecionarImagen
        ivImagenA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // muestra el cuadro de diálogo de selección de imagen
                mGetContentA.launch("image/*");
            }
        });
        //Buscar imagen
        mGetContentA=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent= new Intent(EditarReceta.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }
        });

        //Boton de actualizar
        btnActualizarGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar();
                //startActivity(new Intent(EditarReceta.this, Menu.class).putExtra("idUsuario",idUsuario));
            }
        });

        btnAtrasER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditarReceta.this, MenuResetas.class));
            }
        });

    }

    //Metodo para Guardar y buscar la imagen - metodo para la voz
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==-1 && requestCode==101){
            String result=data.getStringExtra("RESULT");
            resultUriA=null;
            if (result!=null){
                resultUriA=Uri.parse(result);
            }
            ivImagenA.setImageURI(resultUriA);

        }


        if(requestCode == RECOGNIZE_SPEECH_ACTIVITY_A){
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> speech = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String strSpeech2Text = speech.get(0);
                txtDescripcionA.setText(strSpeech2Text);
            }
        }
    }

    //Accion al presionar el boton del microfono

    public void onClickbtnActualizarSensor(View view) {
        Intent intentActionRecognizeSpeech = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Configura el Lenguaje (Español-México)
        intentActionRecognizeSpeech.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-MX");
        try {
            startActivityForResult(intentActionRecognizeSpeech,
                    RECOGNIZE_SPEECH_ACTIVITY_A);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Tú dispositivo no soporta el reconocimiento por voz",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizar(){

        String nombre = txtNombreA.getText().toString().trim();
        String imagen = String.valueOf(resultUriA);
        String ingredientes = txtIngredientesA.getText().toString().trim();
        String descripcion = txtDescripcionA.getText().toString().trim();

        ProgressDialog progressDialog = new ProgressDialog(this);
        if (nombre.isEmpty()){
            txtNombreA.setError("Complete los campos");
        } else if (ingredientes.isEmpty()){
            txtIngredientesA.setError("Complete los campos");
        } else if (descripcion.isEmpty()){
            txtDescripcionA.setError("Complete los campos");
        } else {

            progressDialog.setMessage("Actualizando Receta");

            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://prueba9717.000webhostapp.com/serviciohuizache/Recetas/recetasActualizar.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        startActivity(new Intent(EditarReceta.this, Menu.class));
                        finish();
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(EditarReceta.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> params = new HashMap<String,String>();

                        params.put("idReceta",recordID);
                        params.put("nombre",nombre);
                        params.put("imagen",imagen);
                        params.put("ingredientes",ingredientes);
                        params.put("descripcion",descripcion);


                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(EditarReceta.this);
        requestQueue.add(stringRequest);
        }


    }

    private void mostrarDetalles(){
        Intent intent = getIntent();
        recordID = intent.getStringExtra("RECORD_ID");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://prueba9717.000webhostapp.com/serviciohuizache/Recetas/recetasDetalles.php"+"?idReceta="+recordID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject Recetas = array.getJSONObject(i);
                        ivImagenA.setImageURI(Uri.parse(Recetas.getString("imagen")));
                        txtNombreA.setText(Recetas.getString("nombre"));
                        txtIngredientesA.setText(Recetas.getString("ingredientes"));
                        txtDescripcionA.setText(Recetas.getString("descripcion"));

                        resultUriA = Uri.parse(Recetas.getString("imagen"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditarReceta.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);

    }

    @Override
    public void onBackPressed(){
        //No hace nada
    }
}