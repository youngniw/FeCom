package com.eighteen.fecom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.adapter.TalkCommentRecyclerAdapter;
import com.eighteen.fecom.data.CommentInfo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomCommentDialog extends BottomSheetDialogFragment {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.bottom_sheet_comment, container, false);

        //comment글 임시 데이터 생성(TODO: 추후 삭제) -> Time은 바꿔야 함!
        ArrayList<CommentInfo> commentList = new ArrayList<>();
        commentList.add(new CommentInfo(1,  1, "세린", "2021-08-10 20:32:11", "댓글1입니다.", 1, 1));
        commentList.add(new CommentInfo(2,  2, "소은", "2021-08-10 20:42:25", "이거 익명인가?", 1, 38));
        commentList.add(new CommentInfo(3,  2, "영은", "2021-08-11 21:00:14", "나야나!!*_*", 0, 13));
        commentList.add(new CommentInfo(4,  2, "아이스초코", "2021-08-14 21:23:15", "Hello:) Hi", 0, 3));
        commentList.add(new CommentInfo(1,  1, "세린", "2021-08-10 20:32:11", "댓글1입니다.", 1, 1));
        commentList.add(new CommentInfo(2,  2, "소은", "2021-08-10 20:42:25", "이거 익명인가?", 1, 38));
        commentList.add(new CommentInfo(3,  2, "영은", "2021-08-11 21:00:14", "나야나!!*_*", 0, 13));
        commentList.add(new CommentInfo(4,  2, "아이스초코", "2021-08-14 21:23:15", "Hello:) Hi", 0, 3));

        RecyclerView rvTalkComments = rootView.findViewById(R.id.bscomm_rvComments);
        LinearLayoutManager basicManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvTalkComments.setLayoutManager(basicManager);
        TalkCommentRecyclerAdapter commentAdapter = new TalkCommentRecyclerAdapter(commentList);
        rvTalkComments.setAdapter(commentAdapter);
        rvTalkComments.addItemDecoration(new DividerItemDecoration(requireContext(), 1));

        rvTalkComments.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            v.onTouchEvent(event);
            return true;
        });

        return rootView;
    }
}
