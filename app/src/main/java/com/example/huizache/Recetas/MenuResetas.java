package com.example.huizache.Recetas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.huizache.Adaptadores.AdaptadorFilasResetas;
import com.example.huizache.Menu;
import com.example.huizache.R;
import com.example.huizache.Modelo.RecetasDAO;
import com.example.huizache.Modelo.UsuariosDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuResetas extends AppCompatActivity {
    UsuariosDAO u;
    private FloatingActionButton btnAgregarRegistroResetas;
    private RecyclerView ress;
    //private static String url=;
    public static ArrayList<RecetasDAO> recetasDAOLista = new ArrayList<>();
    ImageButton btnAtrasMR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_resetas);

        ress = findViewById(R.id.registroResetas);
        ress.setHasFixedSize(true);
        ress.setLayoutManager(new LinearLayoutManager(this));
        btnAtrasMR = findViewById(R.id.btnAtrasMR);

        mostrar(u.getId());


        btnAgregarRegistroResetas = findViewById(R.id.btnAgregarRegistroResetas);
        btnAgregarRegistroResetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuResetas.this, AgregarReseta.class));
            }
        });

        btnAtrasMR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuResetas.this, Menu.class));
            }
        });

    }

    private void mostrar(String ident){

        //idUsuario = intent.getStringExtra("idUsuario");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://prueba9717.000webhostapp.com/serviciohuizache/Recetas/recetasMostrar.php"+"?idUsuario="+ident, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                recetasDAOLista.clear();
                //Toast.makeText(MenuResetas.this, "idUsuario: ["+idUsuario+"]", Toast.LENGTH_SHORT).show();
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject Recetas = array.getJSONObject(i);

                        recetasDAOLista.add(new RecetasDAO(
                                Recetas.getString("idReceta"),
                                Recetas.getString("nombre"),
                                Recetas.getString("imagen"),
                                Recetas.getString("ingredientes"),
                                Recetas.getString("descripcion"),
                                Recetas.getString("idUsuario")
                        ));

                    }

                    AdaptadorFilasResetas adaptadorFilasResetas = new AdaptadorFilasResetas(MenuResetas.this, recetasDAOLista);
                    ress.setAdapter(adaptadorFilasResetas);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MenuResetas.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        //maneja elementos del menu
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        //No hace nada
    }
}
