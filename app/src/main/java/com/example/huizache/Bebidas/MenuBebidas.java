package com.example.huizache.Bebidas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.huizache.Adaptadores.AdaptadorFilasBebidas;
import com.example.huizache.Menu;
import com.example.huizache.Modelo.BebidasDAO;
import com.example.huizache.R;
import com.example.huizache.Modelo.UsuariosDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuBebidas extends AppCompatActivity {
    UsuariosDAO u;
    private FloatingActionButton btnAgregarRegistroBebidas;
    //RecyclerView
    private RecyclerView ress;
    public static ArrayList<BebidasDAO> bebidasDAOLista = new ArrayList<>();
    ImageButton btnAtrasMB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bebidas);

        ress = findViewById(R.id.registroBebidas);
        ress.setHasFixedSize(true);
        ress.setLayoutManager(new LinearLayoutManager(this));

        mostrar(u.getId());

        //Inicializar Vista
        btnAtrasMB = findViewById(R.id.btnAtrasMB);
        btnAgregarRegistroBebidas = findViewById(R.id.btnAgregarRegistroBebidas);
        btnAgregarRegistroBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Iniciar la Activity
                startActivity(new Intent(MenuBebidas.this, AgregarBebidas.class));
            }
        });

        btnAtrasMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuBebidas.this, Menu.class));
            }
        });
    }

    private void mostrar(String ident){

        //idUsuario = intent.getStringExtra("idUsuario");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://prueba9717.000webhostapp.com/serviciohuizache/Bebidas/bebidasMostrar.php"+"?idUsuario="+ident, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                bebidasDAOLista.clear();
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject Bebidas = array.getJSONObject(i);

                        bebidasDAOLista.add(new BebidasDAO(
                                Bebidas.getString("idBebidas"),
                                Bebidas.getString("nombre"),
                                Bebidas.getString("imagen"),
                                Bebidas.getString("precio")
                        ));

                    }

                    AdaptadorFilasBebidas adaptadorFilasBebidas = new AdaptadorFilasBebidas(MenuBebidas.this, bebidasDAOLista);
                    ress.setAdapter(adaptadorFilasBebidas);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MenuBebidas.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);

    }


    public void onBackPressed(){
        //No hace nada
    }
}