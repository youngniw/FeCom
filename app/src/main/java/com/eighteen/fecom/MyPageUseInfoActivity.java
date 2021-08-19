package com.eighteen.fecom;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

public class MyPageUseInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_info);

        AppCompatImageButton ibBack = findViewById(R.id.mypageUseInfo_btBack);
        ibBack.setOnClickListener(v -> finish());
    }
}
