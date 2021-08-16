package com.eighteen.fecom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.eighteen.fecom.R;
import com.eighteen.fecom.adapter.DailyTalkPagerAdapter;
import com.eighteen.fecom.adapter.HomeRecyclerAdapter;
import com.eighteen.fecom.data.PostInfo;
import com.eighteen.fecom.data.SummaryData;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        //즐겨찾는 게시판 임시 데이터 생성(TODO: 추후 삭제)
        ArrayList<SummaryData> bSumLists = new ArrayList<>();
        bSumLists.add(new SummaryData("자유게시판", "슬기로운 의사생활 하는 날! 호롤롤롤 너무 신나잖아???!! 1가구 1익준 해야한다고...!!!"));
        bSumLists.add(new SummaryData("오늘은 뭐 먹지?", "대박 맛집 발견"));
        bSumLists.add(new SummaryData("데일리룩", "ootd"));
        bSumLists.add(new SummaryData("취준생 모여라!", "드디어 취뽀했다"));
        bSumLists.add(new SummaryData("맛집알고시포", "햄버거 먹고싶은데....."));
        bSumLists.add(new SummaryData("자취 천국", "전신거울 가성비 좋은.."));
        bSumLists.add(new SummaryData("넷플 중독자", "트와일라잇 존잼이구."));

        RecyclerView rvBoard = rootView.findViewById(R.id.fHome_rvBoard);
        LinearLayoutManager bManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvBoard.setLayoutManager(bManager);
        HomeRecyclerAdapter bAdapter = new HomeRecyclerAdapter(bSumLists);
        rvBoard.setAdapter(bAdapter);

        //데일리톡 Top 10 임시 데이터 생성(TODO: 추후 삭제)
        ArrayList<PostInfo> dtalkLists = new ArrayList<>();
        dtalkLists.add(new PostInfo(1, "능소능소", "1시간 전", "오늘 남자친구랑 헤어졌는데.. \n이 친구가 바람을 피웠어요.. \n진짜 열이 받아서.. XX... 오늘 하루만 같이 욕해주세요;;.... 아오.... 이런 거지같은...\n 길가다가 넘어져라...!!", 1, 32, 2));
        dtalkLists.add(new PostInfo(2, "하이하이", "3시간 전", "경영학과 과제 세상 많아.. 진짜 교수님.. 이게 말이 된다고 생각하세요??", 0, 10, 9));
        dtalkLists.add(new PostInfo(3, "능소화", "5시간 전", "오늘 학교 앞 호박떡 집에 갔는데,,\n웬일로 딱 하나 남음:)\n여러분:) 제가 막차 탔어요>_<", 1, 22, 3));
        dtalkLists.add(new PostInfo(4, "하이헬로", "8시간 전", "투데이 한강뷰:) 미쳐버렸다!!!>_<", 1, 9, 3));

        ViewPager2 vpDailyTalk = rootView.findViewById(R.id.fHome_vpDailyTalk);
        DailyTalkPagerAdapter talkAdapter = new DailyTalkPagerAdapter(true, dtalkLists);
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

        return rootView;
    }
}
