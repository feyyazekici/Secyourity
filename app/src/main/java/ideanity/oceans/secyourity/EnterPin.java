package ideanity.oceans.secyourity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static ideanity.oceans.secyourity.SetPin.MyPREFERENCES;


public class EnterPin extends AppCompatActivity {
    EditText etSifreGir;
    SharedPreferences sharedpreferences;
    View view;


    //Geri Butonu Devre Dışı Bırakma
    @Override
    public void onBackPressed() {
        //
        return;
    }

    //Uygulamalar Butonunu Devre Dışı Bırakma
    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    //Ses Açma Kapama Butonlarını Devre Dışı Bırakma
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            //

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aktivite_sifre_gir);

        /*final Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 1000};
        vb.vibrate(pattern, 0);*/

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String sifre = sharedpreferences.getString("passwordKey", "");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        final MediaPlayer mPlayer = MediaPlayer.create(EnterPin.this, R.raw.siren);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
        mPlayer.start();
        mPlayer.setLooping(true);
        etSifreGir = (EditText) findViewById(R.id.etEnterPin);
        etSifreGir.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String pin = etSifreGir.getText().toString();
                    if (pin.equals(sifre)) {
                        mPlayer.stop();
                        //vb.cancel();
                        startActivity(new Intent(EnterPin.this, HomeActivity.class));
                        finish();
                        handled = true;
                    } else {
                        etSifreGir.getText().clear();
                        etSifreGir.setError("Yanlış Şifre!");
                        etSifreGir.requestFocus();


                    }

                }
                return handled;
            }
        });

    }

}
