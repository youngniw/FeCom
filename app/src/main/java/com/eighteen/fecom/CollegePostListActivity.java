package com.eighteen.fecom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

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

import com.eighteen.fecom.adapter.CollegePostRecyclerAdapter;
import com.eighteen.fecom.data.CollegePostInfo;
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

public class CollegePostListActivity extends AppCompatActivity {
    private int amISubscribe = 0;
    private int collegeID = -1;
    private String collegeName = "";
    private ArrayList<CollegePostInfo> collegePostList = null;

    private CollegePostRecyclerAdapter collegePostAdapter;
    private SwipeRefreshLayout srlPosts;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlist);

        collegeID = getIntent().getExtras().getInt("collegeID");
        collegeName = getIntent().getExtras().getString("collegeName");

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
        srlPosts.setOnRefreshListener(() -> updateCollegePostList(true));

        ActivityResultLauncher<Intent> startActivityResultPost = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK)
                        updateCollegePostList(false);
                });

        collegePostList = new ArrayList<>();
        RecyclerView rvPost = findViewById(R.id.postlist_rv);
        LinearLayoutManager postManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvPost.setLayoutManager(postManager);
        collegePostAdapter = new CollegePostRecyclerAdapter(collegePostList, startActivityResultPost);
        rvPost.setAdapter(collegePostAdapter);
        rvPost.addItemDecoration(new DividerItemDecoration(this, 1));

        updateCollegePostList(false);
        addCollegePostSetting();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatImageButton ivBack = toolbar.findViewById(R.id.postlist_back);
        ivBack.setOnClickListener(v -> finish());

        TextView tvTab = toolbar.findViewById(R.id.postlist_tab);
        tvTab.setText("전공 커뮤니티");

        TextView tvTopic = toolbar.findViewById(R.id.postlist_topic);
        tvTopic.setText(collegeName);

        AppCompatImageButton ivSearch = toolbar.findViewById(R.id.postlist_search);
        ivSearch.setOnClickListener(v -> {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("whichTopic", 3);     //게시물 검색
                bundle.putInt("collegeID", collegeID);
            searchIntent.putExtras(bundle);
            startActivity(searchIntent);
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
                    updateCollegePostList(false);

                else {      
                    //TODO: 메뉴가 다름
                }

                return false;
            });
            setMenu.show();
        });
    }

    public void addCollegePostSetting() {
        ActivityResultLauncher<Intent> startActivityResultPosting = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {     //TODO: Posting으로 부터 받음(result.getData() -> Intent)
                    if (result.getResultCode() == RESULT_OK)
                        updateCollegePostList(false);
                });

        FloatingActionButton fabAddPost = findViewById(R.id.postlist_fabWrite);
        fabAddPost.setOnClickListener(v -> {
            Intent postingIntent = new Intent(this, PostingActivity.class);
            Bundle bundle = new Bundle();
                bundle.putInt("collegeID", collegeID);
            postingIntent.putExtras(bundle);
            startActivityResultPosting.launch(postingIntent);
        });
    }

    @Override
    public void finish() {
        //TODO: Main으로 갈 때 update 되게 해야 함!
        super.finish();
    }

    public void updateCollegePostList(boolean isSwipe) {
        collegePostList.clear();
        collegePostAdapter.notifyDataSetChanged();

        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText("글을 찾고 있습니다:)");
        //TODO: api 이름 바꿔야 함!
        RetrofitClient.getApiService().getBoardPosts(myInfo.getUserID(), collegeID).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.i("CollegePostListActivity 확인용", response.toString());
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

                            collegePostList.add(new CollegePostInfo(postID, anonymous, writerID, writerNick, postTime, content, amILike, likeNum, commentNum));
                        }
                    } catch (JSONException e) { e.printStackTrace(); }

                    tvInfo.setVisibility(View.GONE);
                    collegePostAdapter.notifyDataSetChanged();
                }
                else
                    tvInfo.setText("글이 아직 없습니다.\n글을 작성해 보세요:)");

                if (isSwipe)
                    srlPosts.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                if (isSwipe)
                    srlPosts.setRefreshing(false);

                tvInfo.setText("글 로드 실패\n네트워크를 확인해 주세요.");
            }
        });
    }
}
