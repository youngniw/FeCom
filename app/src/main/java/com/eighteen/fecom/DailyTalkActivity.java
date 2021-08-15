package com.eighteen.fecom;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class DailyTalkActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailytalk);

        Toolbar toolbar = findViewById(R.id.dailyTalk_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_dailytalk, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        FloatingActionButton fabAddPost = findViewById(R.id.dailyTalk_fabWrite);
        fabAddPost.setOnClickListener(v -> {    //TODO: 글 추가가 되면 화면 바뀌어야 함!
            startActivity(new Intent(this, PostingActivity.class));     //익명 안되게!!
            //startActivityForResult(new Intent(this, PostingActivity.class), POSTING_REQUEST);
        });
    }

    private void toolbarListener(Toolbar toolbar) {
        ImageView ivBack = toolbar.findViewById(R.id.dailyTalk_back);
        ivBack.setOnClickListener(v -> finish());
    }
}
