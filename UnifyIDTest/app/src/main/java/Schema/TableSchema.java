package Schema;

import android.provider.BaseColumns;

/**
 * Created by Harsh P Vyas on 9/1/2016.
 */
public class TableSchema {


    public static class table implements BaseColumns{

        public static final String TABLE_NAME1 = "AccelerometerSensor";
        public static final String TABLE_NAME2 = "GyroScopeSensor";
        public static final String TABLE_NAME3 = "GravitySensor";
        public static final String TABLE_NAME4 = "LightSensor";

        public static final String COLUMN_NAME_ID = "Id";
        public static final String COLUMN_NAME_SENSOR_NAME = "SensorName";
        public static final String COLUMN_NAME_SENSOR_Time = "Time";
        public static final String COLUMN_NAME_Sensor_ValueX="SensorValueX";
        public static final String COLUMN_NAME_Sensor_ValueY="SensorValueY";
        public static final String COLUMN_NAME_Sensor_ValueZ="SensorValueZ";
        public static final String COLUMN_NAME_Accuracy="SensorAccuracy";

    }

}
