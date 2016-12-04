package com.example.yunus.halisaham;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordTekrarView;
    private EditText mNameSurnameView;
    private DatabaseReference databaseReference;
    public boolean cancel=false;
    private FirebaseAuth firebaseAuth;
    public TextView txtview;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        txtview= (TextView) findViewById(R.id.textView);
        button= (Button) findViewById(R.id.btnKayitOl);
        mEmailView = (EditText) findViewById(R.id.edittextMail);
        mPasswordView = (EditText) findViewById(R.id.edittextSifre);
        mPasswordTekrarView = (EditText) findViewById(R.id.edittextSifreTekrar);
        mNameSurnameView= (EditText) findViewById(R.id.edittextAdSoyad);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSignUp();
            }
        });

        txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });
    }

    private void UserSignUp() {
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        String passwordTekrar=mPasswordTekrarView.getText().toString().trim();
        String nameSurname=mNameSurnameView.getText().toString().trim();

        mEmailView.setError(null);
        mPasswordView.setError(null);


        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.alanzorunlu));
            focusView = mEmailView;
            cancel = true;
        } else if (!EmailDogruMu(email)) {
            mEmailView.setError(getString(R.string.emailerror));
            focusView = mEmailView;
            cancel = true;
        }


        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.alanzorunlu));
            focusView = mPasswordView;
            cancel = true;
        } else if (!SifreDogruMu(password)) {
            mPasswordView.setError(getString(R.string.sifreerror));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(passwordTekrar)) {
            mPasswordTekrarView.setError(getString(R.string.alanzorunlu));
            focusView = mPasswordView;
            cancel = true;
        }
        else if (!SifreDogruMu(passwordTekrar)) {
            mPasswordTekrarView.setError(getString(R.string.sifreerror));
            focusView = mPasswordTekrarView;
            cancel = true;
        }

        if (TextUtils.isEmpty(nameSurname)) {
            mNameSurnameView.setError(getString(R.string.alanzorunlu));
            focusView = mNameSurnameView;
            cancel = true;
        }

        if (!password.equals(passwordTekrar)) {
            mPasswordView.setError(getString(R.string.eslesme));
            focusView = mPasswordView;
            cancel = true;
        }

        if (internetErisimi()) {
            if (cancel == false)
            {

                final ProgressDialog progressDialog=ProgressDialog.show(SignUpActivity.this,"Kayıt işlemi yapılıyor...","İşlem sürdürülüyor",true);

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    progressDialog.dismiss();
                                    Toast.makeText(SignUpActivity.this, "Kayıt başarıyla tamamlandı!", Toast.LENGTH_SHORT).show();
                                    Intent girisgit = new Intent(SignUpActivity.this, KullaniciMenu.class);
                                    startActivity(girisgit);


                                } else
                                    Toast.makeText(SignUpActivity.this, "Kayıt olma işlemi başarız. Lütfen tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        });

            }
        }

        else
        {

            Toast.makeText(SignUpActivity.this, "İnternet bağlantınız bulunamadı!",Toast.LENGTH_SHORT).show();

        }
        cancel=false;




    }
    private boolean EmailDogruMu(String mail) {
        //TODO: Replace this with your own logic

        return mail.contains("@") && mail.contains(".com");
    }

    private boolean SifreDogruMu(String sifre) {
        //TODO: Replace this with your own logic
        return sifre.length() > 5;
    }

    public boolean internetErisimi() {

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null

                && conMgr.getActiveNetworkInfo().isAvailable()

                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;

        } else {

            return false;

        }

    }



}

