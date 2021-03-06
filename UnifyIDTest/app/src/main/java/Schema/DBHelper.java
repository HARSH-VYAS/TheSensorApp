package Schema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Harsh P Vyas on 9/1/2016.
 */
public class DBHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "UnifyID";

    public DBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);

    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TableSchema.table.TABLE_NAME1 + "(" + TableSchema.table.COLUMN_NAME_ID + " integer primary key Autoincrement, " +
                TableSchema.table.COLUMN_NAME_SENSOR_NAME + " text," + TableSchema.table.COLUMN_NAME_SENSOR_Time + " text, " +
                TableSchema.table.COLUMN_NAME_Sensor_ValueX + " text," + TableSchema.table.COLUMN_NAME_Sensor_ValueY + " text," +
                TableSchema.table.COLUMN_NAME_Sensor_ValueZ + " text," +
                TableSchema.table.COLUMN_NAME_Accuracy + " text)");

        Log.e("table Created","Here");
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS "+ TableSchema.table.TABLE_NAME1);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
