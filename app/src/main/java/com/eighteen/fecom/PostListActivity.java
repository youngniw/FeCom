package com.eighteen.fecom;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.adapter.PostRecyclerAdapter;
import com.eighteen.fecom.data.PostInfo;

import java.util.ArrayList;
import java.util.Objects;

public class PostListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);

        Toolbar toolbar = findViewById(R.id.postlist_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        View customView = View.inflate(this, R.layout.actionbar_postlist, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);

        //TODO: 맞게 변경되어야 함!(Toolbar 설정)
        TextView tvTab = toolbar.findViewById(R.id.postlist_tab);
        tvTab.setText("전공 커뮤니티");       //혹은 tvTab.setText("전체 게시판");
        TextView tvTopic = toolbar.findViewById(R.id.postlist_topic);
        tvTopic.setText("컴퓨터공학과");      //혹은 tvTopic.setText("비밀게시판");
        ImageView ivSearch = toolbar.findViewById(R.id.postlist_search);
        ivSearch.setOnClickListener(v -> {
            //TODO: 검색 창으로 넘어감
        });
        ImageView ivMenu = toolbar.findViewById(R.id.postlist_menu);
        ivMenu.setOnClickListener(v -> {
            //TODO: 팝업 메뉴가 보임
        });

        //post글 임시 데이터 생성(TODO: 추후 삭제) -> Time은 바꿔야 함!
        ArrayList<PostInfo> postLists = new ArrayList<>();
        postLists.add(new PostInfo(1, "지금배불러", "1시간 전", "같이 강남역 근처에서 C언어 공부할 스터디원 구해요!  일주일에 한 번 세시간 정도로 생각하고 있구요 강의 듣고 돌아가면서 발표하고 궁금한거 서로 물어봐요", true, 1, 12));
        postLists.add(new PostInfo(2, "딸기두비두밥", "1시간 전", "안녕하세요 숙명여자대학교 IT공학과 학생입니다. 저희는 방학기간동안 프로젝트를 진행하고자 합니다.\n"+"현재 디자인팀(2명), 개발팀(4명)으로 구성..", false, 3, 15));
        postLists.add(new PostInfo(3, "포도먹는슈가", "1시간 전", "이번에 정처기 보는 사람 있나요??", false, 13, 7));
        postLists.add(new PostInfo(4, "아이스초코", "2시간 전", "정보보안 업계 취업하는 건 어떤가요??\n"+"전망은 좋다고 알고 있는데\n"+"급여나 워라벨 같은거 궁금합니다!!", true, 8, 0));

        RecyclerView rvPost = findViewById(R.id.postlist_rv);
        LinearLayoutManager postManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvPost.setLayoutManager(postManager);
        PostRecyclerAdapter postAdapter = new PostRecyclerAdapter(postLists);
        rvPost.setAdapter(postAdapter);
        rvPost.addItemDecoration(new DividerItemDecoration(this, 1));
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
