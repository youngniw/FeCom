package com.eighteen.fecom;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class PostingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        boolean isDailyTalk = getIntent().hasExtra("isDailyTalk");

        Toolbar toolbar = findViewById(R.id.posting_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_posting, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        CheckBox cbAnonymous = findViewById(R.id.posting_cbAnonymous);
        if (isDailyTalk)
            cbAnonymous.setVisibility(View.GONE);
        //TODO: 익명에 체크일 시 익명으로 서버에 저장 -> if (cbAnonymous.isChecked() == true)
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatButton btCancel = toolbar.findViewById(R.id.posting_cancel);
        btCancel.setOnClickListener(v -> finish());

        AppCompatButton btComplete = toolbar.findViewById(R.id.posting_complete);
        btComplete.setOnClickListener(v -> {
            //TODO: posting 성공! -> 서버로 값 전달(startActivityForResult로 인해 결과 전달)
            finish();
        });
    }
}
