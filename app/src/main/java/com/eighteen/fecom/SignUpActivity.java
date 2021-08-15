package com.eighteen.fecom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button btNext = findViewById(R.id.signup_btNext);

        btNext.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUp2Activity.class));
        });

    }
}
