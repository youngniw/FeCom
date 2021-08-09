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
import com.eighteen.fecom.adapter.BoardRecyclerAdapter;
import com.eighteen.fecom.data.BoardInfo;

import java.util.ArrayList;

public class FragmentBoard extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_board, container, false);

        //기본 게시판 임시 데이터 생성(TODO: 추후 삭제)
        ArrayList<BoardInfo> bBoardLists = new ArrayList<>();
        bBoardLists.add(new BoardInfo("자유게시판", true));
        bBoardLists.add(new BoardInfo("연애게시판", false));
        bBoardLists.add(new BoardInfo("정보게시판", true));
        bBoardLists.add(new BoardInfo("재학생게시판", true));
        bBoardLists.add(new BoardInfo("휴학생게시판", false));

        RecyclerView rvBasicBoard = rootView.findViewById(R.id.fBoard_rvBasic);      //기본 게시판
        LinearLayoutManager bManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvBasicBoard.setLayoutManager(bManager);
        BoardRecyclerAdapter bAdapter = new BoardRecyclerAdapter(bBoardLists);
        rvBasicBoard.setAdapter(bAdapter);

        //서브 게시판 임시 데이터 생성(TODO: 추후 삭제)
        ArrayList<BoardInfo> subBoardLists = new ArrayList<>();
        subBoardLists.add(new BoardInfo("오늘은 뭐 먹지?", true));
        subBoardLists.add(new BoardInfo("데일리룩", false));
        subBoardLists.add(new BoardInfo("취준생 모여라!", true));
        subBoardLists.add(new BoardInfo("자취생 모여라!", true));
        subBoardLists.add(new BoardInfo("맛집알고시포", false));
        subBoardLists.add(new BoardInfo("주린이탈출하자", false));
        subBoardLists.add(new BoardInfo("코인 인생", true));
        subBoardLists.add(new BoardInfo("알바천국", true));
        subBoardLists.add(new BoardInfo("넷플 중독자", false));

        RecyclerView rvSubBoard = rootView.findViewById(R.id.fBoard_rvSub);          //부가적 게시판
        LinearLayoutManager sManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvSubBoard.setLayoutManager(sManager);
        BoardRecyclerAdapter sAdapter = new BoardRecyclerAdapter(subBoardLists);
        rvSubBoard.setAdapter(sAdapter);

        return rootView;
    }
}
