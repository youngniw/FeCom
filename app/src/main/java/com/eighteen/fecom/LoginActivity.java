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

        Button loginBtn = findViewById(R.id.loginBtn);
        Button registerBtn = findViewById(R.id.registerBtn);


        loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        registerBtn.setOnClickListener( v -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });
    }
}
