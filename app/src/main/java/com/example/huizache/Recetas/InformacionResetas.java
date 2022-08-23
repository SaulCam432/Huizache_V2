package com.example.huizache.Recetas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.huizache.Menu;
import com.example.huizache.R;
import com.example.huizache.Modelo.RecetasDAO;
import com.example.huizache.Modelo.UsuariosDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class InformacionResetas extends AppCompatActivity {

    private Context context;
    private ImageButton borrar;
    private CircleImageView informacionImagen;
    private TextView informacionIngredientes, informacionNombre, informacionDescripcion;
    //
    private String recordID,idUsuario;
    ImageButton btnAtrasIR;

    //private static String url="https://prueba9717.000webhostapp.com/serviciohuizache/Recetas/recetasDetalles.php";
    RecetasDAO recetasDAO;
    UsuariosDAO u;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_resetas);

        //obtener la identificación de registro del adaptador mediante la intención
        Intent intent = getIntent();
        recordID = intent.getStringExtra("RECORD_ID");

        //Inicializamos las vistas
        informacionImagen = findViewById(R.id.lblInformacionImagenReseta);
        informacionIngredientes = findViewById(R.id.lblInformacionIngredientesReseta);
        informacionNombre = findViewById(R.id.lblInformacionNombreReseta);
        informacionDescripcion = findViewById(R.id.lblInformacionDescripcionReseta);
        btnAtrasIR = findViewById(R.id.btnAtrasIR);
        mostrarDetalles();

        borrar = findViewById(R.id.btnBorrar);
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Eliminar(recordID);
                startActivity(new Intent(InformacionResetas.this, Menu.class));
            }
        });

        btnAtrasIR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformacionResetas.this, MenuResetas.class));
            }
        });

    }

    private void mostrarDetalles(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://prueba9717.000webhostapp.com/serviciohuizache/Recetas/recetasDetalles.php"+"?idReceta="+recordID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject Recetas = array.getJSONObject(i);
                        informacionImagen.setImageURI(Uri.parse(Recetas.getString("imagen")));
                        informacionNombre.setText(Recetas.getString("nombre"));
                        informacionIngredientes.setText(Recetas.getString("ingredientes"));
                        informacionDescripcion.setText(Recetas.getString("descripcion"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InformacionResetas.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void Eliminar(final String id){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Eliminando Receta");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://prueba9717.000webhostapp.com/serviciohuizache/Recetas/recetasEliminar.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("Receta Borrada")){
                    Toast.makeText(InformacionResetas.this, "Receta eliminada", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(InformacionResetas.this, "No se ha podido eliminar la receta", Toast.LENGTH_SHORT).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InformacionResetas.this, "error", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                params.put("idReceta",id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InformacionResetas.this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed(){
        //No hace nada
    }

}