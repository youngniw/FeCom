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
import com.eighteen.fecom.adapter.NoticeRecyclerAdapter;
import com.eighteen.fecom.data.NoticeData;

import java.util.ArrayList;

public class FragmentNotice extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notice, container, false);

        //기본 게시판 임시 데이터 생성(TODO: 추후 삭제)
        ArrayList<NoticeData> noticeLists = new ArrayList<>();
        noticeLists.add(new NoticeData("IT공학전공", "새로운 댓글이 달렸습니다."));
        noticeLists.add(new NoticeData("넷플 중독자", "새로운 댓글이 달렸습니다."));
        noticeLists.add(new NoticeData("코인 인생", "새로운 댓글이 달렸습니다."));
        noticeLists.add(new NoticeData("오늘은 뭐 먹지?", "새로운 댓글이 달렸습니다."));
        noticeLists.add(new NoticeData("알바천국", "새로운 댓글이 달렸습니다."));
        noticeLists.add(new NoticeData("취준생 모여라!", "새로운 댓글이 달렸습니다."));

        RecyclerView rvNotice = rootView.findViewById(R.id.notice_rv);
        LinearLayoutManager noticeManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvNotice.setLayoutManager(noticeManager);
        NoticeRecyclerAdapter noticeAdapter = new NoticeRecyclerAdapter(noticeLists);
        rvNotice.setAdapter(noticeAdapter);
        rvNotice.addItemDecoration(new DividerItemDecoration(getContext(), 1));

        return rootView;
    }
}
