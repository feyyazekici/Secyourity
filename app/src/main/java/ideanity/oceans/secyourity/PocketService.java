package ideanity.oceans.secyourity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.ComponentActivity;
import androidx.core.app.NotificationCompat;

public class PocketService  extends Service implements SensorEventListener {
    private SensorManager sensorMan;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor accelerometer;
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    Notification _notification;
    private static final int SENSOR_SENSITIVITY = 4;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


//        startInForeground();
        if (Build.VERSION.SDK_INT >= 26) {
            String NOTIFICATION_CHANNEL_ID = "example.permanence";
            String channelName = "Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            _notification = notificationBuilder.setOngoing(true)
                    .setContentTitle("Uygulama Arka Planda ??al??????yor.")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();


        }
        if (Build.VERSION.SDK_INT >= 26) {

            startForeground(10101, _notification);
        }
       // Toast.makeText(this, "servis ba??ar??yla ??al????t??r??ld??.", Toast.LENGTH_SHORT).show();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
//        sensorMan = (SensorManager) getSystemService(SENSOR_SERVICE);
//        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

//        sensorMan.registerListener(this, accelerometer,
//                SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType()== Sensor.TYPE_PROXIMITY){
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {

                } else{
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName cn = new ComponentName(this, EnterPin.class);
                    intent.setComponent(cn);
                    getApplicationContext().startActivity(intent);


                }
            }
        }

    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorMan.unregisterListener(this);
        mSensorManager.unregisterListener(this);
    }

}
