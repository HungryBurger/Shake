package org.androidtown.shaketest;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Hee-Su, Lee
 */
public class ShakeService extends Service implements SensorEventListener {
    private SensorManager mSensorManager = null;
    private Sensor mAccelermeter = null;
    private long mShakeTime;
    private String TAG = "AtServiceClass";
    private static final int SHAKE_SKIP_TIME = 5000; // 스킵 시간
    private static final float SHAKE_THRESHOLD_GRAVITY = 3.0F;

    private BroadcastReceiver mReceiver = null;

    public ShakeService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Service onCreate");
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelermeter = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mReceiver == null)
            registerScreenOffAction();

        Log.d(TAG, "Start Service");
        mSensorManager.registerListener(this, mAccelermeter, SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
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

            Log.d(TAG, "onSensorChanged " + ServiceApplication.service_flag + "");

            if (gForce > SHAKE_THRESHOLD_GRAVITY && ServiceApplication.service_flag) {
                /* 흔들림이 감지 되는 부분 */
                long currentTime = System.currentTimeMillis();

                if (mShakeTime + SHAKE_SKIP_TIME > currentTime) {
                    return;
                } mShakeTime = currentTime;

                Intent intent = new Intent(ShakeService.this, DialogueActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }
    }

    private void registerScreenOffAction () {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenOnOffBroadcastReceiver();
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "Kill Service");
        mSensorManager.unregisterListener(this, mAccelermeter);
        unregisterReceiver(mReceiver);
        mReceiver = null;
    }

}
