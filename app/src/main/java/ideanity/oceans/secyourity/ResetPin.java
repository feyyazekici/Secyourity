package ideanity.oceans.secyourity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static ideanity.oceans.secyourity.SetPin.MyPREFERENCES;
import static ideanity.oceans.secyourity.SetPin.Password;

public class ResetPin extends AppCompatActivity {
    EditText etEskiSifre, etYeniSifre, etOnaySifre;
    Button btnAyarlaSifre, btnGeri;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aktivite_reset_sifre);

        etEskiSifre = (EditText)findViewById(R.id.etOldPin);
        etYeniSifre = (EditText)findViewById(R.id.etSetPin);
        etOnaySifre = (EditText)findViewById(R.id.etConfirmPin);
        btnAyarlaSifre = (Button)findViewById(R.id.btnSetPin);
        btnGeri = (Button)findViewById(R.id.btnGeri);

        btnGeri.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPin.this, HomeActivity.class));
            }
        }));
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String password = sharedpreferences.getString("passwordKey", "");
        btnAyarlaSifre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eskiSifre = etEskiSifre.getText().toString();
                String Sifre = etYeniSifre.getText().toString();
                String onaySifre = etOnaySifre.getText().toString();
                if(TextUtils.isEmpty(eskiSifre) || eskiSifre.length()<4) {
                    etEskiSifre.setError("Şifre en az 4 haneli olmalıdır!");
                    etEskiSifre.requestFocus();
                    return;
                }

                else if(TextUtils.isEmpty(Sifre)|| Sifre.length()<4) {
                    etYeniSifre.setError("Şifre en az 4 haneli olmalıdır!");
                    etYeniSifre.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(onaySifre) || onaySifre.length()<4) {
                    etOnaySifre.setError("Şifre en az 4 haneli olmalıdır!");
                    etOnaySifre.requestFocus();
                    return;
                }
                if(eskiSifre.equals(password)){
                    if(Sifre.equals(onaySifre)){
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Password, onaySifre);
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "Şifre Yenileme Başarılı!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetPin.this, HomeActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Şifreler Eşleşmiyor!", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    etEskiSifre.getText().clear();
                    etEskiSifre.setError("Yanlış Şifre!");
                    etEskiSifre.requestFocus();
                }
            }
        });

    }

}
