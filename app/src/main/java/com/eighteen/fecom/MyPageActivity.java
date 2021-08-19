package com.eighteen.fecom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class MyPageActivity extends AppCompatActivity {
    private TextView tvName, tvNick, tvID, tvUnivAuth, tvChNick, tvChPW, tvWritePosts, tvLikePosts, tvVersion, tvAnnouncement, tvUseInfo;
    private Button btLogout;
    private ActivityResultLauncher<Intent> startChNickActivityResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        //TODO: 회원 조회 필요!!

        Toolbar toolbar = findViewById(R.id.mypage_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_mypage, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        tvName = findViewById(R.id.mypage_name);
        tvNick = findViewById(R.id.mypage_nick);
        tvID = findViewById(R.id.mypage_ID);
        tvUnivAuth = findViewById(R.id.mypage_univAuth);
        tvChNick = findViewById(R.id.mypage_chNick);
        tvChPW = findViewById(R.id.mypage_chPW);
        tvWritePosts = findViewById(R.id.mypage_writePosts);
        tvLikePosts = findViewById(R.id.mypage_likePosts);

        tvVersion = findViewById(R.id.mypage_version);
        tvVersion.setText(BuildConfig.VERSION_NAME);
        tvAnnouncement = findViewById(R.id.mypage_announcement);
        tvUseInfo = findViewById(R.id.mypage_useInfo);
        btLogout = findViewById(R.id.mypage_btLogout);

        startChNickActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK);
                        updateMyInfo();
                });

        updateMyInfo();
        mypageClickListener();
    }

    private void toolbarListener(Toolbar toolbar) {
        ImageView ivBack = toolbar.findViewById(R.id.mypage_back);
        ivBack.setOnClickListener(v -> finish());
    }

    private void updateMyInfo() {
        //TODO: 사용자 조회
    }

    private void mypageClickListener() {
        tvUnivAuth.setOnClickListener(v -> {
            //TODO: 학교 인증 화면으로 넘어감
            //startActivity(new Intent(this, .class));
        });

        tvChNick.setOnClickListener(v -> startChNickActivityResult.launch(new Intent(this, CheckNickActivity.class)));

        tvChPW.setOnClickListener(v -> {
            //TODO: 비밀번호 변경 화면으로 넘어감
            //startActivityForResult(new Intent(this, .class), CHANGE_PW_REQUEST);
        });

        tvWritePosts.setOnClickListener(v -> {
            //TODO: 내가 작성한 글 화면으로 넘어감
            //startActivity(new Intent(this, .class));
        });

        tvLikePosts.setOnClickListener(v -> {
            //TODO: 내가 좋아요한 글 화면으로 넘어감
            //startActivity(new Intent(this, .class));
        });

        tvAnnouncement.setOnClickListener(v -> {
            //TODO: 공지사항 화면으로 넘어감
            //startActivity(new Intent(this, .class));
        });

        tvUseInfo.setOnClickListener(v -> {
            //TODO: 이용안내 화면으로 넘어감
            //startActivity(new Intent(this, .class));
        });

        btLogout.setOnClickListener(v -> {
            Intent logoutIntent = new Intent(this, LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logoutIntent);
            finish();
        });
    }
}
