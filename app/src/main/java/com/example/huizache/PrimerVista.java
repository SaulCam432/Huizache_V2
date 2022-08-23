package com.example.huizache;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huizache.Usuarios.Login;

public class PrimerVista extends AppCompatActivity {

    Button LoginRedirecionado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer_vista);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);

        ImageView titulo = findViewById(R.id.txtTitulo);
        ImageView imagen = findViewById(R.id.txtLogoImageView);

        titulo.setAnimation(animation);
        imagen.setAnimation(animation1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PrimerVista.this, Login.class));
            }
        },4000);
    }
}