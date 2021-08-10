package com.eighteen.fecom;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.adapter.CommentRecyclerAdapter;
import com.eighteen.fecom.data.CommentInfo;

import java.util.ArrayList;
import java.util.Objects;

public class PostActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar = findViewById(R.id.post_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        View customView = View.inflate(this, R.layout.actionbar_post, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);

        //comment글 임시 데이터 생성(TODO: 추후 삭제) -> Time은 바꿔야 함!
        ArrayList<CommentInfo> commentList = new ArrayList<>();
        commentList.add(new CommentInfo(1, "세린", "2021.08.10 20:32:11", "댓글1입니다.", true, 1));
        commentList.add(new CommentInfo(2, "소은", "2021.08.10 20:42:25", "이거 익명인가?", true, 38));
        commentList.add(new CommentInfo(3, "영은", "2021.08.10 21:00:14", "나야나!!*_*", false, 13));
        commentList.add(new CommentInfo(4, "아이스초코", "2021.08.10 21:23:15", "Hello:) Hi", true, 3));

        RecyclerView rvComment = findViewById(R.id.post_rvComments);
        LinearLayoutManager commentManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvComment.setLayoutManager(commentManager);
        CommentRecyclerAdapter commentAdapter = new CommentRecyclerAdapter(commentList);
        rvComment.setAdapter(commentAdapter);
        rvComment.addItemDecoration(new DividerItemDecoration(this, 1));
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
