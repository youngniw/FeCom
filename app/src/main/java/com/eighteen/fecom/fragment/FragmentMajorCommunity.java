package com.eighteen.fecom.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.R;
import com.eighteen.fecom.adapter.CollegeRecyclerAdapter;
import com.eighteen.fecom.data.MajorInfo;

import java.util.ArrayList;

public class FragmentMajorCommunity extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_majorcommunity, container, false);

        SearchView svSearch = rootView.findViewById(R.id.fMajor_sv);
        svSearch.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {        //TODO: 검색 창 포커스 없애기가 안됨!!
                Log.i("확인용", "2");
                svSearch.onActionViewCollapsed();
            }
            else
                Log.i("확인용", "1");
        });

        //기본 게시판 임시 데이터 생성(TODO: 추후 삭제)
        ArrayList<MajorInfo> collegeLists = new ArrayList<>();
        ArrayList<String> departList1 = new ArrayList<>();
            departList1.add("국어국문학과");  departList1.add("독어독문학과");  departList1.add("문예창작학과");  departList1.add("문헌정보학과");
            departList1.add("영어영문학과");  departList1.add("일어일문학과");  departList1.add("중어중문학과");  departList1.add("철학과");
        ArrayList<String> departList2 = new ArrayList<>();
            departList2.add("경영학과");  departList2.add("해양경찰학과");  departList2.add("정치외교학과");  departList2.add("미디어학과");
            departList2.add("아동복지학과");  departList2.add("호텔경영학과");
        ArrayList<String> departList3 = new ArrayList<>();
            departList3.add("가정교육과");  departList3.add("수학교육과");  departList3.add("교육학과");  departList3.add("국어교육학과");
        ArrayList<String> departList4 = new ArrayList<>();
            departList4.add("건축학과");  departList4.add("컴퓨터공학과");  departList4.add("기계공학과");  departList4.add("화학공학과");
        ArrayList<String> departList5 = new ArrayList<>();
        departList5.add("물리학과");  departList5.add("생명공학과");  departList5.add("화학과");
        ArrayList<String> departList6 = new ArrayList<>();
        departList6.add("간호학과");  departList6.add("의예과");
        ArrayList<String> departList7 = new ArrayList<>();
        departList7.add("디자인학과");  departList7.add("경호학과");  departList7.add("무용학과");
        collegeLists.add(new MajorInfo("인문계열", departList1));
        collegeLists.add(new MajorInfo("사회계열", departList2));
        collegeLists.add(new MajorInfo("교육계열", departList3));
        collegeLists.add(new MajorInfo("공학계열", departList4));
        collegeLists.add(new MajorInfo("자연계열", departList5));
        collegeLists.add(new MajorInfo("의약계열", departList6));
        collegeLists.add(new MajorInfo("예체능계열", departList7));

        RecyclerView rvMajorCollege = rootView.findViewById(R.id.fMajor_rvCollege);
        LinearLayoutManager collegeManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvMajorCollege.setLayoutManager(collegeManager);
        CollegeRecyclerAdapter collegeAdapter = new CollegeRecyclerAdapter(collegeLists);
        rvMajorCollege.setAdapter(collegeAdapter);

        return rootView;
    }
}
