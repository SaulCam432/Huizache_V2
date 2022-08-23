package com.example.huizache.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.huizache.Bebidas.EditarBebida;
import com.example.huizache.Menu;
import com.example.huizache.Modelo.BebidasDAO;
import com.example.huizache.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptadorFilasBebidas extends RecyclerView.Adapter<AdaptadorFilasBebidas.HolderRecord>{
    //Variables
    private Context context;
    private List<BebidasDAO> bebidasDAOS;
    String idBebida;

    //Constructor
    public AdaptadorFilasBebidas(Context context, List<BebidasDAO> bebidasDAOS){
        this.context = context;
        this.bebidasDAOS = bebidasDAOS;
    }

    @NonNull
    @Override
    public HolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.contenedores_bebidas,null);
        return new HolderRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorFilasBebidas.HolderRecord holder, int position) {

        BebidasDAO bebidasDAO = bebidasDAOS.get(position);


        Glide.with(context)
                .load(bebidasDAO.getImagen())
                .into(holder.lblRowImagenBebidas);
        holder.lblRowNombreBebidas.setText(bebidasDAO.getNombre());
        holder.lblRowPrecioBebidas.setText("$"+bebidasDAO.getPrecio());

        holder.EditarBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditarBebida.class);
                intent.putExtra("RECORD_ID",bebidasDAO.getId());
                context.startActivity(intent);
            }
        });

        holder.BorrarBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idBebida = bebidasDAO.getId();
                Eliminar(idBebida);
                //Toast.makeText(context.getApplicationContext(), idBebida, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return bebidasDAOS.size();// devuelve el tamaño de la lista / número o registros
    }

    class HolderRecord extends RecyclerView.ViewHolder{
        //vistas
        ImageView lblRowImagenBebidas;
        TextView lblRowNombreBebidas;
        TextView lblRowPrecioBebidas;
        ImageButton EditarBebidas, BorrarBebidas;
        public HolderRecord(@NonNull View itemView){
            super(itemView);

            //Inicializamos la vistas
            lblRowImagenBebidas = itemView.findViewById(R.id.lblRowImagenBebida);
            lblRowNombreBebidas = itemView.findViewById(R.id.lblRowNombreBebida);
            lblRowPrecioBebidas = itemView.findViewById(R.id.lblRowPrecioBebida);
            EditarBebidas = itemView.findViewById(R.id.EditarBebidas);
            BorrarBebidas = itemView.findViewById(R.id.BorrarBebidas);

        }
    }

    private void Eliminar(final String id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://prueba9717.000webhostapp.com/serviciohuizache/Bebidas/bebidasEliminar.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("Bebida Borrada")){

                    Intent in = new Intent(context, Menu.class);
                    context.startActivity(in);

                    Toast.makeText(context.getApplicationContext(), "Bebida eliminada", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context.getApplicationContext(), "No se ha podido eliminar la receta", Toast.LENGTH_SHORT).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                params.put("idBebidas",id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(stringRequest);

    }
}
