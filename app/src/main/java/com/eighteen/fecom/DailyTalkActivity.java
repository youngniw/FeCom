package com.eighteen.fecom;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
            actionBar.setDisplayHomeAsUpEnabled(true);
        View customView = View.inflate(this, R.layout.actionbar_tab, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);

        TextView tvTitle = toolbar.findViewById(R.id.tabTB_title);
        tvTitle.setText(R.string.tab_dailytalk);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
