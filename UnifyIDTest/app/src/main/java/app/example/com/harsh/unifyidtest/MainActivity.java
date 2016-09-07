package app.example.com.harsh.unifyidtest;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import POJO.SensorData;
import Schema.DBHelper;
import Schema.TableSchema;

public class MainActivity extends AppCompatActivity implements SensorEventListener,View.OnClickListener {

    SensorManager mSensorManager = null;
    Sensor mLight, mAccelorMeter,mGyro,mGrav;
    int count1, count2, count3, count4;

    Button btnAccel,btnLight,btnGyro,btnGrav;
    ArrayList<SensorData> sensorD = new ArrayList<SensorData>();
    TextView X,Y,Z,AVG;
    String xi, yi, zi, avg;
    int version;
    boolean iscount = false;

    DBHelper mHelper;
    boolean storeGyro = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        count1=0;
        count2=0;
        count3=0;
        count4=0;
        version=1;
        mHelper = new DBHelper(MainActivity.this,version);
        //Initializing the Sensor manager to capture the sensor information
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight= mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mGyro= mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mAccelorMeter = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGrav= mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        btnAccel = (Button) findViewById(R.id.butnAcceler);
        btnAccel.setOnClickListener(this);

        btnGyro = (Button) findViewById(R.id.butnGyro);
        btnGyro.setOnClickListener(this);

        btnLight = (Button) findViewById(R.id.butLight);
        btnLight.setOnClickListener(this);

        btnGrav = (Button) findViewById(R.id.butGrav);
        btnGrav.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()){
            case R.id.butnAcceler:
                mSensorManager.registerListener(MainActivity.this, mAccelorMeter, SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case R.id.butGrav:
                mSensorManager.registerListener(MainActivity.this, mGrav, SensorManager.SENSOR_DELAY_FASTEST);
                break;
            case R.id.butnGyro:
                mSensorManager.registerListener(MainActivity.this, mGyro, SensorManager.SENSOR_DELAY_FASTEST);
                break;
            case R.id.butLight:
                mSensorManager.registerListener(MainActivity.this, mLight, SensorManager.SENSOR_DELAY_FASTEST);
                break;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do something here if sensor accuracy changes.
        Log.d("Accuracy-->", "Accuracy Changed");
        Log.d("Sensor Name-->", sensor.getName());
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        Log.d("Sensor Name",event.sensor.getName());
        SensorData sd = new SensorData(event.sensor.getName(),event.accuracy,event.values[0],event.values[1],event.values[2]);
        Log.d("Sensor Name", sd.getSensorName());
        Log.d("Inside","SensorChanged");
        sensorD.add(sd);
       if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
          //  storeData(event,"Accel");

           count1++;
           Log.d("count1", String.valueOf(count1));
           if(count1==50){
               mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
               storeData();
           }
        }

        if( event.sensor.getType()==Sensor.TYPE_GYROSCOPE){
            Log.d("Gravity", "Entered");

            count2++;
            Log.d("count1", String.valueOf(count2));
            if(count2==50){
                mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY));
                storeData();
            }

        }

        if( event.sensor.getType()==Sensor.TYPE_GRAVITY){
            Log.d("Gravity", "Entered");

            count3++;
            Log.d("count1", String.valueOf(count2));
            if(count3==50){
                mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY));
                storeData();
            }

        }
        if( event.sensor.getType()==Sensor.TYPE_LIGHT) {
            Log.d("Light", "Entered");

            count4++;
            Log.d("count1", String.valueOf(count3));
            if(count4==1){
                mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
                storeData();
            }

        }
    }

    public void storeData(){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    String sensorName = null;
                    Log.d("sensor", String.valueOf(sensorD.size()));
                    for(int i=0;i<sensorD.size();i++) {
                        ContentValues values = new ContentValues();
                        values.put(TableSchema.table.COLUMN_NAME_SENSOR_NAME, sensorD.get(i).getSensorName());
                        values.put(TableSchema.table.COLUMN_NAME_Sensor_ValueX, sensorD.get(i).getX());
                        values.put(TableSchema.table.COLUMN_NAME_Sensor_ValueY, sensorD.get(i).getY());
                        values.put(TableSchema.table.COLUMN_NAME_Sensor_ValueZ, sensorD.get(i).getZ());
                        values.put(TableSchema.table.COLUMN_NAME_Accuracy, sensorD.get(i).getAccuracy());
                        long newRowId = db.insert(TableSchema.table.TABLE_NAME1, null, values);
                        Log.d("RowID", String.valueOf(newRowId));
                        Log.d("Sensor Value-->", String.valueOf(sensorD.get(i).getX()));
                        Log.d("Sensor Accuracy-->", String.valueOf(sensorD.get(i).getY()));
                        Log.d("Sensor time-->", String.valueOf(sensorD.get(i).getZ()));
                        Log.d("Sensor Name-->",sensorD.get(i).getSensorName());
                        sensorName=sensorD.get(i).getSensorName();

                    }
                    db.close();
                    readData(sensorName);
                }
            }).start();
    }

    public void readData(String name){
        SQLiteDatabase db1 = mHelper.getReadableDatabase();
        Cursor res = db1.rawQuery("Select * from " + TableSchema.table.TABLE_NAME1+ " where SensorName = '"+name+"' order by Id DESC LIMIT 1", null);
        if(res.moveToFirst()) {
            if (res.getColumnCount() != 0) {
                xi = res.getString(res.getColumnIndex("SensorValueX"));
                Log.d("xi", String.valueOf(xi));
                yi = res.getString(res.getColumnIndex("SensorValueY"));
                Log.d("yi", String.valueOf(yi));
                zi = res.getString(res.getColumnIndex("SensorValueZ"));
                Log.d("zi", String.valueOf(zi));
                double avgi = (Double.parseDouble(xi) + Double.parseDouble(yi) + Double.parseDouble(zi)) / 3;
                Log.d("Avg", String.valueOf(avgi));
                avg = String.valueOf(avgi);
                iscount = false;
                db1.close();
                Intent intent = new Intent(this, Result.class);
                intent.putExtra("X", xi);
                intent.putExtra("Y", yi);
                intent.putExtra("Z", zi);
                intent.putExtra("AVG", avg);
                startActivity(intent);
            } else
                Log.d("------->", "Column Count 0");
        }
        else
            Log.e("Error","No Data");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item );
    }
}
