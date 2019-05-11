package com.example.ipbbookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.graphics.Typeface;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.ipbbookingapp.Common.Common;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int APP_REQUEST_CODE = 7117; //nomor berapa aja

    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.txt_skip)
    TextView txt_skip;

    @OnClick(R.id.btn_login)
    void loginUser(){
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent,APP_REQUEST_CODE);
    }

    @OnClick(R.id.txt_skip)
    void skipLoginJustGoHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Common.IS_LOGIN, false);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == APP_REQUEST_CODE)
        {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if(loginResult.getError() != null)
            {
                Toast.makeText(this,""+loginResult.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
            }
            else if(loginResult.wasCancelled())
            {
                Toast.makeText(this, "Login Canceled", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra(Common.IS_LOGIN, true);
                startActivity(intent);
                finish();
            }
        }
    }

    //    TextView welcome, createAccount;
//    private AutoCompleteTextView userEmail, userPassword;
//    private Button btnLogin;
//
//    private FirebaseAuth mAuth;
//
//    private FirebaseAuth.AuthStateListener mAuthListener;

//    public MainActivity() {
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if(accessToken != null) // jika sudah login
        {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Common.IS_LOGIN, true);
            startActivity(intent);
            finish();
        }
        else
        {
            setContentView(R.layout.activity_main);
            ButterKnife.bind(MainActivity.this);
        }

//
//        mAuth = FirebaseAuth.getInstance();
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if(firebaseAuth.getCurrentUser() != null){
//                    startActivity(new Intent(MainActivity.this, Beranda.class));
//                }
//            }
//        };
//
//        welcome = (TextView)findViewById(R.id.welcome);
//        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Signatura Monoline.ttf");
//        welcome.setTypeface(face);
//
//        createAccount = (TextView)findViewById(R.id.createAccount);
//
//        createAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, Register.class);
//                startActivity(intent);
//            }
//        });
//
//        userEmail=(AutoCompleteTextView) findViewById(R.id.userEmail);
//        userPassword=(AutoCompleteTextView) findViewById(R.id.userPassword);
//        btnLogin = (Button) findViewById(R.id.btnLogin);
//
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startSigIn();
//            }
//        });
    }
    private void printKeyHash(){
        try{
            PackageInfo packageInfo = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES
            );
            for(Signature signature : packageInfo.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    private void startSigIn() {
//        String email = userEmail.getText().toString().trim();
//        String password = userPassword.getText().toString().trim();
//
//        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
//            Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_LONG).show();
//        } else {
//
//            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(!task.isSuccessful()){
//                        Toast.makeText(MainActivity.this, "Sign in problem", Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//        }
//    }

}