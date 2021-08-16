package com.eighteen.fecom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = findViewById(R.id.login_btLogin);
        Button registerBtn = findViewById(R.id.login_btSignUp);


        loginBtn.setOnClickListener(v -> {
            //TODO: 서버에서 로그인 확인 받기:)
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        registerBtn.setOnClickListener( v -> startActivity(new Intent(this, SignUpActivity.class)));
    }
}
