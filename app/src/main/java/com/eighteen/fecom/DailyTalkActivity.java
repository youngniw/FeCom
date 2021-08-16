package com.eighteen.fecom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.eighteen.fecom.adapter.DailyTalkPagerAdapter;
import com.eighteen.fecom.data.PostInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
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

        //데일리톡 Top 10 임시 데이터 생성(TODO: 추후 삭제)
        ArrayList<PostInfo> dtalkLists = new ArrayList<>();
        dtalkLists.add(new PostInfo(1, "능소능소", "1시간 전", "오늘 남자친구랑 헤어졌는데.. \n이 친구가 바람을 피웠어요.. \n진짜 열이 받아서.. XX... 오늘 하루만 같이 욕해주세요;;.... 아오.... 이런 거지같은...\n 길가다가 넘어져라...!!", 1, 32, 2));
        dtalkLists.add(new PostInfo(2, "하이하이", "3시간 전", "경영학과 과제 세상 많아.. 진짜 교수님.. 이게 말이 된다고 생각하세요??", 0, 10, 9));
        dtalkLists.add(new PostInfo(3, "능소화", "5시간 전", "오늘 학교 앞 호박떡 집에 갔는데,,\n웬일로 딱 하나 남음:)\n여러분:) 제가 막차 탔어요>_<", 1, 22, 3));
        dtalkLists.add(new PostInfo(4, "하이헬로", "8시간 전", "투데이 한강뷰:) 미쳐버렸다!!!>_<", 1, 9, 3));

        ViewPager2 vpDailyTalk = findViewById(R.id.dailyTalk_vp);
        DailyTalkPagerAdapter talkAdapter = new DailyTalkPagerAdapter(false, dtalkLists);
        vpDailyTalk.setAdapter(talkAdapter);
        vpDailyTalk.setOffscreenPageLimit(3);
        vpDailyTalk.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(8));
        transformer.addTransformer((page, position) -> {
            float v = 1 - Math.abs(position);
            page.setScaleY(0.8f + v * 0.2f);
        });
        vpDailyTalk.setPageTransformer(transformer);

        FloatingActionButton fabAddPost = findViewById(R.id.dailyTalk_fabWrite);
        fabAddPost.setOnClickListener(v -> {
            Intent addTalk = new Intent(this, PostingActivity.class);
            Bundle bundle = new Bundle();
                bundle.putBoolean("isDailyTalk", true);
            addTalk.putExtras(bundle);
            startActivity(addTalk);     //TODO: 익명 안되게!!
            //startActivityForResult(new Intent(this, PostingActivity.class), POSTING_REQUEST);
        });
    }

    private void toolbarListener(Toolbar toolbar) {
        ImageView ivBack = toolbar.findViewById(R.id.dailyTalk_back);
        ivBack.setOnClickListener(v -> finish());
    }
}
