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
import com.example.huizache.Modelo.UsuariosDAO;
import com.example.huizache.Recetas.AgregarReseta;
import com.example.huizache.Usuarios.Login;
import com.example.huizache.Usuarios.RegistroUsuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgregarBebidas extends AppCompatActivity {

    CircleImageView ivImagen;
    EditText txtNombre,txtPrecio;
    FloatingActionButton btnGuardar;
    ActivityResultLauncher<String> mGetContent;
    Uri resultUri;
    ImageButton btnAtrasAB;
    Uri imageUri;
    UsuariosDAO u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_bebidas);

        ivImagen = findViewById(R.id.btnAgregarImagenBebida);
        txtNombre = findViewById(R.id.txtAgregarNombreBebida);
        txtPrecio = findViewById(R.id.txtAgregarPrecioBebida);
        btnGuardar = findViewById(R.id.btnAGuardarBebida);
        btnAtrasAB = findViewById(R.id.btnAtrasAB);

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
                if(resultUri == null){
                    Toast.makeText(AgregarBebidas.this, "Inserte una imagen", Toast.LENGTH_SHORT).show();
                }else {
                    insertar();
                }

            }
        });

        mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent= new Intent(AgregarBebidas.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }
        });

        btnAtrasAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AgregarBebidas.this, MenuBebidas.class));
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
    }


    private void insertar(){
        String nombre = txtNombre.getText().toString().trim();
        //imagen = resultUri
        String imagen = resultUri.toString();
        String precio = txtPrecio.getText().toString().trim();
        String idUsuario = u.getId();

        ProgressDialog progressDialog = new ProgressDialog(this);
        if (nombre.isEmpty()){
            txtNombre.setError("Complete los campos");
        } else if (precio.isEmpty()){
            txtPrecio.setError("Complete los campos");
        }else {
            progressDialog.setMessage("Agregando bebida");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://prueba9717.000webhostapp.com/serviciohuizache/Bebidas/bebidasCrear.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("Bebida insertada")) {
                        Toast.makeText(AgregarBebidas.this, "Bebida insertada", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AgregarBebidas.this, Menu.class));
                        progressDialog.dismiss();


                    } else {
                        Toast.makeText(AgregarBebidas.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AgregarBebidas.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String>params= new HashMap<String, String>();
                    params.put("nombre", nombre);
                    params.put("imagen", imagen);
                    params.put("precio", precio);
                    params.put("idUsuario", idUsuario);

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(AgregarBebidas.this);
            requestQueue.add(stringRequest);
        }


    }

    public void onBackPressed(){
        //No hace nada
    }
}