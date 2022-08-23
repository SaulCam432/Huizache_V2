package com.example.huizache;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.huizache.Bebidas.MenuBebidas;
import com.example.huizache.Recetas.MenuResetas;
import com.example.huizache.Modelo.UsuariosDAO;
import com.example.huizache.Usuarios.Login;

public class Menu extends AppCompatActivity {

    ImageButton menuResetas, menuBebidas;

    //Variables del sensor de proximidad
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (sensor == null){
            Toast.makeText(this, "No tengo sensor de proximidad :))", Toast.LENGTH_SHORT).show();
            finish();
        }

        menuResetas = findViewById(R.id.btnRecetas);
        menuResetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this, MenuResetas.class));
            }
        });

        menuBebidas = findViewById(R.id.btnBebidas);
        menuBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this, MenuBebidas.class));
            }
        });

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[0] < sensor.getMaximumRange()){
                    startActivity(new Intent(Menu.this, Login.class));
                    //getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                }else {
                    //getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }
    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public void onBackPressed(){
        //No hace nada
    }


}