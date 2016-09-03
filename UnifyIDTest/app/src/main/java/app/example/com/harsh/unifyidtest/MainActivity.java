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

import Schema.DBHelper;
import Schema.TableSchema;

public class MainActivity extends AppCompatActivity implements SensorEventListener,View.OnClickListener {

    SensorManager mSensorManager = null;
    Sensor mLight, mAccelorMeter, mGyro, mGrav;
    Button btnAccel,btnLight,btnGyro,btnGrav;
    TextView X,Y,Z,AVG;
    String xi, yi, zi, avg;
    int count;
    int version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        count=0;
        version=1;
        if(mSensorManager!=null){
            mSensorManager.unregisterListener(this);
        }
        // Initializing the Sensor manager to capture the sensor information
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight= mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mAccelorMeter = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyro= mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mGrav= mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        btnAccel = (Button) findViewById(R.id.butnAcceler);
        btnAccel.setOnClickListener(this);

        btnLight = (Button) findViewById(R.id.butLight);
        btnLight.setOnClickListener(this);

        btnGyro = (Button) findViewById(R.id.butGyro);
        btnGyro.setOnClickListener(this);

        btnGrav = (Button) findViewById(R.id.butGrav);
        btnGrav.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()){
            case R.id.butnAcceler:
                mSensorManager.registerListener(MainActivity.this, mAccelorMeter, SensorManager.SENSOR_DELAY_NORMAL);
                version=1;
                break;
            case R.id.butGrav:
                mSensorManager.registerListener(MainActivity.this, mGrav, SensorManager.SENSOR_DELAY_FASTEST);
                version=2;
                break;
            case R.id.butGyro:
                mSensorManager.registerListener(MainActivity.this, mGyro, SensorManager.SENSOR_DELAY_FASTEST);
                version=3;
                break;
            case R.id.butLight:
                mSensorManager.registerListener(MainActivity.this, mLight, SensorManager.SENSOR_DELAY_FASTEST);
                version=4;
                break;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        Log.d("Accuracy-->", "Accuracy Changed");
        Log.d("Sensor Name-->", sensor.getName());
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        Log.d("Inside","SensorChanged");
        Log.d("Count", String.valueOf(count));
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
           storeData(event);
        }
       if( event.sensor.getType()==Sensor.TYPE_GYROSCOPE){
          Log.d("Gyroscope","Entered");
           storeData(event);
       }
        if( event.sensor.getType()==Sensor.TYPE_GRAVITY){
            Log.d("Gravity","Entered");
            storeData(event);
        }
        if( event.sensor.getType()==Sensor.TYPE_LIGHT){
            Log.d("Light","Entered");
            storeData(event);
        }
    }


    public void storeData(SensorEvent event){
        DBHelper mHelper = new DBHelper(this,version);
        SQLiteDatabase db = mHelper.getWritableDatabase();

        float lux = event.values[0];
        float luy = event.values[1];
        float luz = event.values[2];
        long time = event.timestamp;
        int accuracy = event.accuracy;
        Sensor sensor = event.sensor;
        Boolean iscount = false;
        if(count==10)
            count=0;
        if (count < 10) {
            ContentValues values = new ContentValues();
            values.put(TableSchema.table.COLUMN_NAME_ID, count++);
            values.put(TableSchema.table.COLUMN_NAME_SENSOR_NAME, sensor.getName());
            values.put(TableSchema.table.COLUMN_NAME_SENSOR_Time, time);
            values.put(TableSchema.table.COLUMN_NAME_Sensor_ValueX, lux);
            values.put(TableSchema.table.COLUMN_NAME_Sensor_ValueY, luy);
            values.put(TableSchema.table.COLUMN_NAME_Sensor_ValueZ, luz);
            values.put(TableSchema.table.COLUMN_NAME_Accuracy, accuracy);
            long newRowId = db.insert(TableSchema.table.TABLE_NAME, null, values);
            Log.d("RowID", String.valueOf(newRowId));
            Log.d("Sensor Value-->", String.valueOf(lux));
            Log.d("Sensor Accuracy-->", String.valueOf(time));
            Log.d("Sensor time-->", String.valueOf(accuracy));
            Log.d("Sensor Name-->", sensor.getName());
            if (count == 10)
                iscount = true;
        }

        if (iscount == true) {
            SQLiteDatabase db1 = mHelper.getReadableDatabase();
            Cursor res = db1.rawQuery("Select * from " + TableSchema.table.TABLE_NAME + " where Id=1", null);
            res.moveToFirst();
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
                iscount=false;
                Intent intent = new Intent(this,Result.class);
                intent.putExtra("X",xi);
                intent.putExtra("Y",yi);
                intent.putExtra("Z",zi);
                intent.putExtra("AVG",avg);
                startActivity(intent);
            }
            else
                Log.d("------->", "Column Count 0");
        }
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
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY));
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
