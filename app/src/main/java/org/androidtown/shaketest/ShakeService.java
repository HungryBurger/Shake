package org.androidtown.shaketest;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by Hee-Su, Lee
 */
public class ShakeService extends Service implements SensorEventListener {
    private SensorManager mSensorManager = null;
    private Sensor mAccelermeter = null;
    private long mShakeTime;
    private static final int SHAKE_SKIP_TIME = 500; // 스킵 시간
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;

    public ShakeService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelermeter = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mSensorManager.registerListener(this, mAccelermeter, SensorManager.SENSOR_DELAY_NORMAL);

        //return super.onStartCommand(intent, flags, startId);
        /* 서비스가 강제종료 되었을 경우 재시작한다.(디폴트값) */
        return START_STICKY;

        /* 강제종료 되어도 재시작하지 않는다. */
        //return START_NOT_STICKY;

        /* 서비스가 강제 종료 되었을 경우 재시작하며, 서비스를 시작할때 받았던 intent를 다시 받으며 시작한다. */
        //return START_REDELIVER_INTENT
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            float gravityX = axisX / SensorManager.GRAVITY_EARTH;
            float gravityY = axisY / SensorManager.GRAVITY_EARTH;
            float gravityZ = axisZ / SensorManager.GRAVITY_EARTH;

            Float f = gravityX * gravityX + gravityY * gravityY + gravityZ * gravityZ;
            double squaredD = Math.sqrt(f.doubleValue());
            float gForce = (float) squaredD;

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                long currentTime = System.currentTimeMillis();
                if (mShakeTime + SHAKE_SKIP_TIME > currentTime) {
                    return;
                }
                mShakeTime = currentTime;
                Toast.makeText(getApplicationContext(), "Shake 감지", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopSelf();
        Toast.makeText(getApplicationContext(), "서비스 종료", Toast.LENGTH_SHORT).show();
    }
}
