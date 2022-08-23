package com.example.huizache.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.huizache.Recetas.EditarReceta;
import com.example.huizache.Recetas.InformacionResetas;
import com.example.huizache.R;
import com.example.huizache.Modelo.RecetasDAO;

import java.util.List;

public class AdaptadorFilasResetas extends RecyclerView.Adapter<AdaptadorFilasResetas.HolderRecord>{
    private Context context;
    private List<RecetasDAO> recetasDAOS;


    public AdaptadorFilasResetas(Context context, List<RecetasDAO> recetasDAOS){
        this.context = context;
        this.recetasDAOS = recetasDAOS;

    }

    @NonNull
    @Override
    public AdaptadorFilasResetas.HolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.contenedores_resetas, null);
        return new AdaptadorFilasResetas.HolderRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecord holder, int position) {
        RecetasDAO recetasDAO = recetasDAOS.get(position);


        Glide.with(context)
                .load(recetasDAO.getImagen())
                .into(holder.lblRowImagenReseta);
        holder.lblRowNombreReseta.setText(recetasDAO.getNombre());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, InformacionResetas.class);
                intent.putExtra("RECORD_ID", recetasDAO.getIdReceta());
                context.startActivity(intent);
            }
        });

        holder.EditarResetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, EditarReceta.class);
                intent.putExtra("RECORD_ID", recetasDAO.getIdReceta());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return recetasDAOS.size();// devuelve el tamaño de la lista / número o registros

    }


    class HolderRecord extends RecyclerView.ViewHolder{
        //vistas
        ImageView lblRowImagenReseta;
        TextView lblRowNombreReseta;
        ImageButton EditarResetas;
        String idUsuario;


        public HolderRecord(@NonNull View itemView){
            super(itemView);

            //Inicializamos la vistas
            lblRowImagenReseta = itemView.findViewById(R.id.lblRowImagenReseta);
            lblRowNombreReseta = itemView.findViewById(R.id.lblRowNombreReseta);
            EditarResetas = itemView.findViewById(R.id.EditarResetas);


        }
    }

}
