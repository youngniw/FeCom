package com.eighteen.fecom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.R;
import com.eighteen.fecom.adapter.HomeRecyclerAdapter;
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

        RecyclerView rvBoard = rootView.findViewById(R.id.home_rvBoard);
        LinearLayoutManager bManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvBoard.setLayoutManager(bManager);
        HomeRecyclerAdapter bAdapter = new HomeRecyclerAdapter(bSumLists);
        rvBoard.setAdapter(bAdapter);

        //데일리톡 Top 10 임시 데이터 생성(TODO: 추후 삭제)
        ArrayList<SummaryData> dtSumLists = new ArrayList<>();
        dtSumLists.add(new SummaryData("능소화", "오늘 남자친구랑 헤어졌는데.. 이 친구가 바람을 피웠어요.."));
        dtSumLists.add(new SummaryData("경영뿌셔", "경영학과 과제 세상 많아.. 진짜 교수님.. 이게 말이 된다고 생각하세요??"));
        dtSumLists.add(new SummaryData("데이뚜데이뚜", "오늘 썸남이랑 첫 데이뚜.. 옷 뭐 입을지 추천요!"));
        dtSumLists.add(new SummaryData("맑은하늘", "투데이 한강뷰:) 미쳐버렸다!!!>_<"));

        RecyclerView rvDailyTalk = rootView.findViewById(R.id.home_rvDailyTalk);
        LinearLayoutManager dtManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvDailyTalk.setLayoutManager(dtManager);
        HomeRecyclerAdapter dtAdapter = new HomeRecyclerAdapter(dtSumLists);
        rvDailyTalk.setAdapter(dtAdapter);

        return rootView;
    }
}
