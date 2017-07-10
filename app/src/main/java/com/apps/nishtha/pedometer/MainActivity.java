package com.apps.nishtha.pedometer;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int count = 0;
    TextView textView;
    SensorManager sensorManager;
    Sensor stepCounter;
    SensorEventListener sensorEventListener;
    public static final String INPUT="count";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = getPreferences(MODE_PRIVATE);

        editor = sharedPreferences.edit();

        textView = (TextView) findViewById(R.id.textView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        count=Integer.parseInt(sharedPreferences.getString(INPUT,"0"));
        textView.setText(sharedPreferences.getString(INPUT,"0"));

        sensorEventListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                count++;
                editor.putString(INPUT,""+count);
                editor.apply();
                textView.setText("" + count);

                if (count % 10 == 0) {
                    Toast.makeText(MainActivity.this, "" + count + " steps walked!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        sensorManager.registerListener(sensorEventListener,stepCounter,sensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.refresh){
            AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Are you sure you want to set the count to 0?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            count=0;
                            editor.putString(INPUT,""+count);
                            textView.setText(""+count);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();


        }
        return super.onOptionsItemSelected(item);
    }
}