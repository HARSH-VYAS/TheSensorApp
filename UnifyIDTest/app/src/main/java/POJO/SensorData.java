package POJO;

/**
 * Created by Harsh P Vyas on 9/6/2016.
 */
public class SensorData {

    String sensorName;
    float x,y,z,accuracy;

    public SensorData(String name, int accuracy, float x, float y, float z) {

        this.sensorName = name;
        this.accuracy = accuracy;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
}
