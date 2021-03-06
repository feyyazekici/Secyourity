package ideanity.oceans.secyourity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity implements SensorEventListener {

    Switch motionSwitch, proximitySwitch, chargerSwitch;
    CountDownTimer cdt;
    private SensorManager sensorMan;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor accelerometer;
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    AlertDialog alertDialog;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView yapimci;
    private Button Geri;
    private static final int SENSOR_SENSITIVITY = 4;

    TextView sifreHatirla;

    int mSwitchSet,pSwitchSet = 0;
    int chargerFlag, chargerFlag1, chargerFlag2 = 0;


    @Override
    public void onResume() {
        super.onResume();
        sensorMan.registerListener((SensorEventListener) this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMan.unregisterListener(this);
        mSensorManager.unregisterListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.aktivite_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                startActivity(new Intent(HomeActivity.this, ResetPin.class));
                finish();
                return true;
            case R.id.item2:
                hakkinda();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aktivite_anasayfa);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        alertDialog = new AlertDialog.Builder(this).create();
        chargerSwitch = (Switch) findViewById(R.id.sCharger);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

                if (plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged==BatteryManager.BATTERY_PLUGGED_USB) {
                    chargerFlag = 1;
                } else if (plugged == 0) {
                    chargerFlag1 = 1;
                    chargerFlag = 0;
                    func();

                }
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);

        chargerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {

                    if (chargerFlag != 1) {
                        Toast.makeText(HomeActivity.this, "Sarj Aletine Ba??lay??n??z.", Toast.LENGTH_SHORT).show();
                        chargerSwitch.setChecked(false);
                    } else {
                        Toast.makeText(HomeActivity.this, "Sarj Koruma Modu Aktif!", Toast.LENGTH_SHORT).show();
                        chargerFlag2 = 1;
                        func();
                    }


                } else {
                    chargerFlag2 = 0;
                }

            }
        });



        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        motionSwitch = (Switch) findViewById(R.id.sMotion);
        motionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    alertDialog.setTitle("Alarm Aktifle??iyor...");
                    alertDialog.setMessage("00:10");

                    cdt = new CountDownTimer(10000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            alertDialog.setMessage("00:" + (millisUntilFinished / 1000));
                        }

                        @Override
                        public void onFinish() {
                            //info.setVisibility(View.GONE);
                            mSwitchSet = 1;
                            alertDialog.hide();
                            Toast.makeText(HomeActivity.this, "Deprem Alg??lama Modu Aktif.", Toast.LENGTH_SHORT).show();

                        }
                    }.start();
                    alertDialog.show();
                    alertDialog.setCancelable(false);


                } else {
                    Toast.makeText(HomeActivity.this, "Deprem Alg??lama Modu Kapat??ld??.", Toast.LENGTH_SHORT).show();
                    mSwitchSet = 0;
                }

            }
        });
        proximitySwitch = (Switch) findViewById(R.id.sProximity);
        proximitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    alertDialog.setTitle("Telefonu Yerine Yerle??tiriniz...");
                    alertDialog.setMessage("00:10");



                    cdt = new CountDownTimer(10000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            alertDialog.setMessage("00:" + (millisUntilFinished / 1000));
                        }

                        @Override
                        public void onFinish() {
                            //info.setVisibility(View.GONE);
                            pSwitchSet = 1;
                            alertDialog.hide();
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startForegroundService(new Intent(HomeActivity.this, PocketService.class));
                            } else {
                                startService(new Intent(HomeActivity.this, PocketService.class));
                                finish();
                            }
                        }
                    }.start();
                    alertDialog.show();
                    alertDialog.setCancelable(false);

                } else {
                    Toast.makeText(HomeActivity.this, "Mod Kapat??ld??.", Toast.LENGTH_SHORT).show();
                    pSwitchSet = 0;
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void func() {
        if (chargerFlag == 0 && chargerFlag1 == 1 && chargerFlag2 == 1) {
            startActivity(new Intent(HomeActivity.this, EnterPin.class));
            chargerFlag2 = 0;
            finish();
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone();
            // Deprem Tespiti
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if (mAccel > 0.5) {

                if (mSwitchSet == 1) {
                    startActivity(new Intent(HomeActivity.this, EnterPin.class));
                    finish();
                }

            }
        }

    }

    public void hakkinda(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.aktivite_hak, null);
        Geri = (Button) contactPopupView.findViewById(R.id.Geri);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        Geri.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();
            }
        }));
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}