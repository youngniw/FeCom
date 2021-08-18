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

import com.eighteen.fecom.R;
import com.eighteen.fecom.adapter.CollegeRecyclerAdapter;
import com.eighteen.fecom.data.CollegeInfo;

import java.util.ArrayList;

public class FragmentCollegeCommunity extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_collegecommunity, container, false);

        //기본 게시판 임시 데이터 생성(TODO: 추후 삭제)
        ArrayList<CollegeInfo> collegeLists = new ArrayList<>();
        collegeLists.add(new CollegeInfo(1, "인문계열"));
        collegeLists.add(new CollegeInfo(2, "사회계열"));
        collegeLists.add(new CollegeInfo(3, "교육계열"));
        collegeLists.add(new CollegeInfo(4, "공학계열"));
        collegeLists.add(new CollegeInfo(5, "자연계열"));
        collegeLists.add(new CollegeInfo(6, "의약계열"));
        collegeLists.add(new CollegeInfo(7, "예체능계열"));

        RecyclerView rvCollege = rootView.findViewById(R.id.fCollege_rvCollege);
        LinearLayoutManager collegeManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvCollege.setLayoutManager(collegeManager);
        CollegeRecyclerAdapter collegeAdapter = new CollegeRecyclerAdapter(collegeLists);
        rvCollege.setAdapter(collegeAdapter);

        return rootView;
    }
}
