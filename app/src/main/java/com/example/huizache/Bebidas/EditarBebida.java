package com.example.huizache.Bebidas;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarBebida extends AppCompatActivity {

    EditText txtNombreActual, txtprecioActual;
    String recordID;
    ImageButton btnAtrasEB;
    CircleImageView ivImagenActual;
    FloatingActionButton btnActualGuardar;
    ActivityResultLauncher<String> mGetContentA;
    Uri resultUriActual, ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_bebida);

        Intent intent = getIntent();
        recordID = intent.getStringExtra("RECORD_ID");

        ivImagenActual = findViewById(R.id.btnActualizarImagenBebida);
        txtNombreActual = findViewById(R.id.txtActualizarNombreBebida);
        txtprecioActual = findViewById(R.id.txtActualizarPrecioBebida);
        btnActualGuardar = findViewById(R.id.btnActualizarGuardarBebida);
        btnAtrasEB = findViewById(R.id.btnAtrasEB);

        mostrarDetalles();


        //SelecionarImagen
        ivImagenActual.setOnClickListener(new View.OnClickListener() {
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
                Intent intent= new Intent(EditarBebida.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }
        });

        //Boton de actualizar
        btnActualGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar();
                //startActivity(new Intent(EditarReceta.this, Menu.class).putExtra("idUsuario",idUsuario));
            }
        });

        btnAtrasEB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditarBebida.this, MenuBebidas.class));
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == -1 && requestCode == 101) {
            String result = data.getStringExtra("RESULT");
            resultUriActual = null;
            if (result != null) {
                resultUriActual = Uri.parse(result);
            }
            ivImagenActual.setImageURI(resultUriActual);

        }
    }

    private void actualizar(){

        String nombre = txtNombreActual.getText().toString().trim();

        String imagen = resultUriActual.toString();
        String precio = txtprecioActual.getText().toString().trim();

        ProgressDialog progressDialog = new ProgressDialog(this);
        if (nombre.isEmpty()){
            txtNombreActual.setError("Complete los campos");
        } else if (precio.isEmpty()){
            txtprecioActual.setError("Complete los campos");
        } else {
            progressDialog.setMessage("Actualizando Bebida");

            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://prueba9717.000webhostapp.com/serviciohuizache/Bebidas/bebidadActualizar.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    startActivity(new Intent(EditarBebida.this, Menu.class));
                    finish();
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(EditarBebida.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params = new HashMap<String,String>();

                    params.put("idBebidas",recordID);
                    params.put("nombre",nombre);
                    params.put("imagen",imagen);
                    params.put("precio",precio);


                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(EditarBebida.this);
            requestQueue.add(stringRequest);
        }


    }

    private void mostrarDetalles(){
        Intent intent = getIntent();
        recordID = intent.getStringExtra("RECORD_ID");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://prueba9717.000webhostapp.com/serviciohuizache/Bebidas/bebidasDetalles.php"+"?idBebidas="+recordID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject Bebidas = array.getJSONObject(i);
                        ivImagenActual.setImageURI(Uri.parse(Bebidas.getString("imagen")));
                        txtNombreActual.setText(Bebidas.getString("nombre"));
                        txtprecioActual.setText(Bebidas.getString("precio"));

                        resultUriActual = Uri.parse(Bebidas.getString("imagen"));
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditarBebida.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);

    }

    public void onBackPressed(){
        //No hace nada
    }
}