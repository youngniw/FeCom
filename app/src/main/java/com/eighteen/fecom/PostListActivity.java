package com.eighteen.fecom;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.adapter.PostRecyclerAdapter;
import com.eighteen.fecom.data.PostInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_postlist, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        //post글 임시 데이터 생성(TODO: 추후 삭제) -> Time은 바꿔야 함!
        ArrayList<PostInfo> postList = new ArrayList<>();
        postList.add(new PostInfo(1, "지금배불러", "1시간 전", "같이 강남역 근처에서 C언어 공부할 스터디원 구해요!  일주일에 한 번 세시간 정도로 생각하고 있구요 강의 듣고 돌아가면서 발표하고 궁금한거 서로 물어봐요", true, 1, 12));
        postList.add(new PostInfo(2, "딸기두비두밥", "1시간 전", "안녕하세요 숙명여자대학교 IT공학과 학생입니다. 저희는 방학기간동안 프로젝트를 진행하고자 합니다.\n"+"현재 디자인팀(2명), 개발팀(4명)으로 구성..", false, 3, 15));
        postList.add(new PostInfo(3, "포도먹는슈가", "1시간 전", "이번에 정처기 보는 사람 있나요??", false, 13, 7));
        postList.add(new PostInfo(4, "아이스초코", "2시간 전", "정보보안 업계 취업하는 건 어떤가요??\n"+"전망은 좋다고 알고 있는데\n"+"급여나 워라벨 같은거 궁금합니다!!", true, 8, 0));

        RecyclerView rvPost = findViewById(R.id.postlist_rv);
        LinearLayoutManager postManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvPost.setLayoutManager(postManager);
        PostRecyclerAdapter postAdapter = new PostRecyclerAdapter(postList);
        rvPost.setAdapter(postAdapter);
        rvPost.addItemDecoration(new DividerItemDecoration(this, 1));

        FloatingActionButton fabAddPost = findViewById(R.id.postlist_fabWrite);
        fabAddPost.setOnClickListener(v -> {    //TODO: 글 추가가 되면 새로고침 되어야 함!
            startActivity(new Intent(this, PostingActivity.class));
            //startActivityForResult(new Intent(this, PostingActivity.class), POSTING_REQUEST);
        });
    }

    private void toolbarListener(Toolbar toolbar) {
        ImageView ivBack = toolbar.findViewById(R.id.postlist_back);
        ivBack.setOnClickListener(v -> finish());
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
            PopupMenu setMenu = new PopupMenu(getApplicationContext(), v);
            getMenuInflater().inflate(R.menu.menu_postlist, setMenu.getMenu());
            setMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.menu_postlist_refresh){
                    //TODO: 새로고침
                }
                else {
                    //TODO: 즐겨찾는 게시판에 추가
                }

                return false;
            });
            setMenu.show();
        });
    }
}
