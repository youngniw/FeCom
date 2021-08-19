package com.eighteen.fecom;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

public class MyPageNoticeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_notice);

        AppCompatImageButton ibBack = findViewById(R.id.mypageNotice_btBack);
        ibBack.setOnClickListener(v -> finish());
    }
}
