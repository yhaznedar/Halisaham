package com.example.yunus.halisaham;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    // UI referansları
    private TextView textView;
    private EditText mEmailView;
    private EditText mPasswordView;
    public boolean cancel=false;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private GoogleApiClient client;
    private Button mEmailSignInButton;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        progressDialog=new ProgressDialog(this);
        textView= (TextView) findViewById(R.id.textView);
        firebaseAuth = FirebaseAuth.getInstance();
        mEmailView = (EditText) findViewById(R.id.edittextMail);
        mPasswordView = (EditText) findViewById(R.id.edittextSifre);
        mEmailSignInButton = (Button) findViewById(R.id.btnKayitOl);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Kullanıcı oturumu açtı
                    Log.d("MainActivity:onCreate","onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // Kullanıcı oturumu kapattı.
                    Log.d("MainActivity:onCreate", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLogin();
            }
        });

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });



}


    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }




    private void UserLogin() {
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

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

        if (internetErisimi()) {
            if (cancel == false)
            {
                progressDialog.setMessage("Giriş yapılıyor...");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //kullanıcı girdiyse eğer buradan ve doğru ise herşey
                            finish();
                            Intent i = new Intent(LoginActivity.this, KullaniciMenu.class);
                            i.putExtra("Email",firebaseAuth.getCurrentUser().getEmail());
                            startActivity(i);
                        }
                        else
                            Toast.makeText(LoginActivity.this, "Girdiğin e-posta ve şifre kayıtlarımızdakiyle eşleşmedi.", Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();
                    }
                });

            }





        }

        else

        {

            Toast.makeText(LoginActivity.this, "İnternet bağlantınız bulunamadı!",Toast.LENGTH_SHORT).show();

        }
        cancel=false;


    }

    private boolean EmailDogruMu(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.contains(".com");
    }

    private boolean SifreDogruMu(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

