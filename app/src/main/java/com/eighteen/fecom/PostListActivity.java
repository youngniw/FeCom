package com.eighteen.fecom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eighteen.fecom.adapter.PostRecyclerAdapter;
import com.eighteen.fecom.data.PostInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

public class PostListActivity extends AppCompatActivity {
    private boolean isBoardPost = true;
    private int amISubscribe = 0;
    private int boardOrCollegeID = -1;
    private String boardOrCollegeName = "";
    private ArrayList<PostInfo> postList = null;

    private PostRecyclerAdapter postAdapter;
    private SwipeRefreshLayout srlPosts;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);

        isBoardPost = getIntent().hasExtra("boardID");
        if (isBoardPost) {
            amISubscribe = getIntent().getExtras().getInt("amISubscribe");
            boardOrCollegeID = getIntent().getExtras().getInt("boardID");
            boardOrCollegeName = getIntent().getExtras().getString("boardName");
        }
        else {
            boardOrCollegeID = getIntent().getExtras().getInt("collegeID");
            boardOrCollegeName = getIntent().getExtras().getString("collegeName");
        }

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

        tvInfo = findViewById(R.id.postlist_tvInfo);

        srlPosts = findViewById(R.id.postlist_swipeRL);
        srlPosts.setDistanceToTriggerSync(400);
        srlPosts.setOnRefreshListener(() -> updatePostList(true));

        ActivityResultLauncher<Intent> startActivityResultPost = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK)
                        updatePostList(false);
                });

        postList = new ArrayList<>();
        RecyclerView rvPost = findViewById(R.id.postlist_rv);
        LinearLayoutManager postManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvPost.setLayoutManager(postManager);
        postAdapter = new PostRecyclerAdapter(postList, startActivityResultPost);
        rvPost.setAdapter(postAdapter);
        rvPost.addItemDecoration(new DividerItemDecoration(this, 1));

        updatePostList(false);
        addPostSetting();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatImageButton ivBack = toolbar.findViewById(R.id.postlist_back);
        ivBack.setOnClickListener(v -> finish());
        TextView tvTab = toolbar.findViewById(R.id.postlist_tab);
        if (isBoardPost)
            tvTab.setText("전체 게시판");
        else
            tvTab.setText("전공 커뮤니티");
        TextView tvTopic = toolbar.findViewById(R.id.postlist_topic);
        tvTopic.setText(boardOrCollegeName);
        AppCompatImageButton ivSearch = toolbar.findViewById(R.id.postlist_search);
        ivSearch.setOnClickListener(v -> {
            //TODO: 검색 창으로 넘어감
        });
        AppCompatImageButton ivMenu = toolbar.findViewById(R.id.postlist_menu);
        ivMenu.setOnClickListener(v -> {
            PopupMenu setMenu = new PopupMenu(getApplicationContext(), v);
            getMenuInflater().inflate(R.menu.menu_postlist, setMenu.getMenu());

            MenuItem subscribeMenu = setMenu.getMenu().findItem(R.id.menu_postlist_subscribe);
            if (amISubscribe == 1)
                subscribeMenu.setTitle(R.string.board_subscribe_no);
            else
                subscribeMenu.setTitle(R.string.board_subscribe_yes);

            setMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.menu_postlist_refresh)
                    updatePostList(false);

                else {      //TODO: BoardRecyclerAdapter의 내용을 새로고침 해야 함! -> registerForActivityResult
                    if (amISubscribe == 1) {    //"즐겨찾는 게시판 해제" 선택
                        RetrofitClient.getApiService().postDeleteSubscribeB(myInfo.getUserID(), boardOrCollegeID).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                Log.i("PostListActivity 확인용", response.toString());
                                if (response.code() == 200) {
                                    amISubscribe = 0;
                                    subscribeMenu.setTitle(R.string.board_subscribe_yes);
                                }
                                else
                                    Toast.makeText(PostListActivity.this, "다시 한번 시도해 주세요:)", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                Toast.makeText(PostListActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {                      //"즐겨찾는 게시판 등록" 선택
                        RetrofitClient.getApiService().postSubscribeBoard(myInfo.getUserID(), boardOrCollegeID).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                Log.i("PostListActivity 확인용", response.toString());
                                if (response.code() == 200) {
                                    amISubscribe = 1;
                                    subscribeMenu.setTitle(R.string.board_subscribe_no);
                                }
                                else
                                    Toast.makeText(PostListActivity.this, "다시 한번 시도해 주세요.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                Toast.makeText(PostListActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                return false;
            });
            setMenu.show();
        });
    }

    public void addPostSetting() {
        ActivityResultLauncher<Intent> startActivityResultPosting = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {     //TODO: Posting으로 부터 받음(result.getData() -> Intent)
                    if (result.getResultCode() == RESULT_OK)
                        updatePostList(false);
                });

        FloatingActionButton fabAddPost = findViewById(R.id.postlist_fabWrite);
        fabAddPost.setOnClickListener(v -> {
            Intent postingIntent = new Intent(this, PostingActivity.class);
            Bundle bundle = new Bundle();
            if (isBoardPost)
                bundle.putInt("boardID", boardOrCollegeID);
            else
                bundle.putInt("collegeID", boardOrCollegeID);
            postingIntent.putExtras(bundle);
            startActivityResultPosting.launch(postingIntent);
        });
    }

    @Override
    public void finish() {
        //TODO: Main으로 갈 때 update 되게 해야 함!
        super.finish();
    }

    public void updatePostList(boolean isSwipe) {
        postList.clear();
        postAdapter.notifyDataSetChanged();

        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText("게시글을 찾고 있습니다:)");
        RetrofitClient.getApiService().getPosts(myInfo.getUserID(), boardOrCollegeID).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.i("PostListActivity 확인용", response.toString());
                if (response.code() == 200) {
                    try {
                        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONArray jsonPosts = result.getJSONArray("posts");
                        for (int i=0; i<jsonPosts.length(); i++) {
                            JSONObject postObject = jsonPosts.getJSONObject(i);

                            int postID = postObject.getInt("id");
                            int anonymous = postObject.getInt("anonymous");
                            int writerID = postObject.getInt("writer");
                            String writerNick = postObject.getString("writer_nickname");
                            String postTime = postObject.getString("register_datetime");
                            String content = postObject.getString("content");
                            int amILike = postObject.getInt("thumbup");
                            int likeNum = postObject.getInt("like_count");
                            int commentNum = postObject.getInt("comment_count");

                            postList.add(new PostInfo(postID, anonymous, writerID, writerNick, postTime, content, amILike, likeNum, commentNum));
                        }
                    } catch (JSONException e) { e.printStackTrace(); }

                    tvInfo.setVisibility(View.GONE);
                    postAdapter.notifyDataSetChanged();
                }
                else
                    tvInfo.setText("게시글이 아직 없습니다.\n글을 작성해 보세요:)");

                if (isSwipe)
                    srlPosts.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                if (isSwipe)
                    srlPosts.setRefreshing(false);

                tvInfo.setText("게시글 로드 실패\n네트워크를 확인해 주세요.");
            }
        });
    }
}
