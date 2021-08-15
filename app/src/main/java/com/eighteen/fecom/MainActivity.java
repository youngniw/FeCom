package com.eighteen.fecom;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eighteen.fecom.fragment.FragmentBoard;
import com.eighteen.fecom.fragment.FragmentHome;
import com.eighteen.fecom.fragment.FragmentMajorCommunity;
import com.eighteen.fecom.fragment.FragmentMessage;
import com.eighteen.fecom.fragment.FragmentNotice;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private FragmentManager fragmentManager;
    private FragmentBoard fragmentBoard;
    private FragmentHome fragmentHome;
    private FragmentMajorCommunity fragmentMajorCommunity;
    private FragmentNotice fragmentNotice;
    private FragmentMessage fragmentMessage;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_tab_home, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        homeToolbarListener(toolbar);

        fragmentManager = getSupportFragmentManager();
        fragmentBoard = new FragmentBoard();
        fragmentHome = new FragmentHome();
        fragmentMajorCommunity = new FragmentMajorCommunity();
        fragmentNotice = new FragmentNotice();
        fragmentMessage = new FragmentMessage();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_framelayout, fragmentHome).commitAllowingStateLoss();

        tabToolbarListener(toolbar);
    }

    private void homeToolbarListener(Toolbar toolbar) {
        ImageView ivSearch = toolbar.findViewById(R.id.homeTB_search);
        ivSearch.setOnClickListener(v -> {
            //TODO: 검색 창으로 넘어감!
        });

        ImageView ivMyPage = toolbar.findViewById(R.id.homeTB_mypage);
        ivMyPage.setOnClickListener(v -> startActivity(new Intent(this, MyPageActivity.class)));
    }

    @SuppressLint("NonConstantResourceId")
    private void tabToolbarListener(Toolbar toolbar) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.main_tab);
        bottomNavigationView.setSelectedItemId(R.id.tab_home);

        bottomNavigationView.getMenu().getItem(1).setOnMenuItemClickListener(item -> {      //데일리톡 클릭 시
            startActivity(new Intent(MainActivity.this, DailyTalkActivity.class));

            return true;
        });

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            FragmentTransaction transactionNow = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.tab_board: {
                    transactionNow.replace(R.id.main_framelayout, fragmentBoard).commitAllowingStateLoss();

                    View actionBarView = View.inflate(this, R.layout.actionbar_tab_board, null);
                    actionBar.setCustomView(actionBarView, params);
                    boardToolbarListener(toolbar);

                    break;
                }
                case R.id.tab_home: {
                    transactionNow.replace(R.id.main_framelayout, fragmentHome).commitAllowingStateLoss();

                    View actionBarView = View.inflate(this, R.layout.actionbar_tab_home, null);
                    actionBar.setCustomView(actionBarView, params);
                    homeToolbarListener(toolbar);

                    break;
                }
                case R.id.tab_majorCommunity: {
                    transactionNow.replace(R.id.main_framelayout, fragmentMajorCommunity).commitAllowingStateLoss();

                    View actionBarView = View.inflate(this, R.layout.actionbar_tab_major, null);
                    actionBar.setCustomView(actionBarView, params);
                    TextView tvTitle = toolbar.findViewById(R.id.tabTB_title);
                    tvTitle.setText(R.string.tab_majorcommunity);

                    break;
                }
                case R.id.tab_notice: {
                    transactionNow.replace(R.id.main_framelayout, fragmentNotice).commitAllowingStateLoss();

                    View actionBarView = View.inflate(this, R.layout.actionbar_tab_notice, null);
                    actionBar.setCustomView(actionBarView, params);
                    tabNoticeSetting(toolbar);

                    break;
                }
            }
            return true;
        });
    }

    private void boardToolbarListener(Toolbar toolbar) {
        ImageView ivSearch = toolbar.findViewById(R.id.boardTB_search);
        ivSearch.setOnClickListener(v -> {
            //TODO: 검색 창으로 넘어감
        });
        ImageView ivAdd = toolbar.findViewById(R.id.boardTB_add);
        ivAdd.setOnClickListener(v -> {
            //TODO: 게시판 추가
        });
    }

    private void tabNoticeSetting(Toolbar toolbar) {
        TextView tvMessage = toolbar.findViewById(R.id.noticeTB_message);
        TextView tvNotice = toolbar.findViewById(R.id.noticeTB_notice);
        tvNotice.setEnabled(false);

        tvNotice.setOnClickListener(v -> {
            tvNotice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            tvMessage.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white_transparent));
            tvNotice.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_tab_underline));
            tvMessage.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_tab_underline_none));

            FragmentTransaction transactionNow = fragmentManager.beginTransaction();
            transactionNow.replace(R.id.main_framelayout, fragmentNotice).commitAllowingStateLoss();

            tvNotice.setEnabled(false);
            tvMessage.setEnabled(true);
        });

        tvMessage.setOnClickListener(v -> {
            tvNotice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white_transparent));
            tvMessage.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            tvNotice.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_tab_underline_none));
            tvMessage.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_tab_underline));

            FragmentTransaction transactionNow = fragmentManager.beginTransaction();
            transactionNow.replace(R.id.main_framelayout, fragmentMessage).commitAllowingStateLoss();

            tvNotice.setEnabled(true);
            tvMessage.setEnabled(false);
        });
    }
}
