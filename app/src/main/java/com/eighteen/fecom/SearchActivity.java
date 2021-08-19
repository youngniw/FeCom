package com.eighteen.fecom;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.adapter.BoardRecyclerAdapter;
import com.eighteen.fecom.adapter.BoardPostRecyclerAdapter;
import com.eighteen.fecom.data.BoardInfo;
import com.eighteen.fecom.data.PostInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;
import static com.eighteen.fecom.MainActivity.myInfo;

public class SearchActivity extends AppCompatActivity {
    private int whichTopic = 0;     //1은 홈, 2는 게시판, 3은 게시판의 게시물, 4는 단과대학의 게시글
    private int boardID = -1;
    private String presentKeyword = "";
    private ArrayList<BoardInfo> boardList = null;
    private ArrayList<PostInfo> postList = null;

    private AppCompatImageButton ibtBack, ibtSearch;
    private BoardRecyclerAdapter boardAdapter;
    private BoardPostRecyclerAdapter postAdapter;
    private EditText etSearch;
    private LinearLayout llInfo;
    private TextView tvNoResult;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        whichTopic = getIntent().getExtras().getInt("whichTopic");

        ibtBack = findViewById(R.id.search_back);
        ibtSearch = findViewById(R.id.search_ibSearch);
        llInfo = findViewById(R.id.search_llInfo);
        etSearch = findViewById(R.id.search_etSearch);
        if (whichTopic == 1)
            etSearch.setHint("게시판 검색");
        else if (whichTopic == 2) {
            etSearch.setHint("게시물 검색");
            boardID = getIntent().getExtras().getInt("boardID");
        }
        else {
            etSearch.setHint("단과대학 게시글 검색");
            boardID = getIntent().getExtras().getInt("collegeID");
        }
        tvNoResult = findViewById(R.id.search_tvNoPost);

        RecyclerView rvSearch = findViewById(R.id.search_rv);
        LinearLayoutManager basicManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvSearch.setLayoutManager(basicManager);
        rvSearch.addItemDecoration(new DividerItemDecoration(this, 1));

        if (whichTopic == 1) {
            boardList = new ArrayList<>();
            boardAdapter = new BoardRecyclerAdapter(boardList);
            rvSearch.setAdapter(boardAdapter);
        }
        else if (whichTopic == 2) {
            postList = new ArrayList<>();
            ActivityResultLauncher<Intent> startActivityResultPost2 = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK)
                            setSearchPostList(presentKeyword);
                    });
            postAdapter = new BoardPostRecyclerAdapter(postList, boardID, startActivityResultPost2);
            rvSearch.setAdapter(postAdapter);
        }
        else
            ;   //TODO: 단과대학의 게시글 검색

        searchListener();
    }

    private void searchListener() {
        ibtBack.setOnClickListener(v -> finish());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNoResult.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            switch (actionId) {
                case IME_ACTION_SEARCH : {
                    etSearch.setEnabled(false);

                    String searchKeyword = etSearch.getText().toString().trim();
                    if (searchKeyword.length() <= 0)
                        etSearch.setEnabled(true);
                    else {
                        if (whichTopic == 1)
                            setSearchBoardList(searchKeyword);
                        else if (whichTopic == 2)
                            setSearchPostList(searchKeyword);
                        else
                            setSearchCollegePostList(searchKeyword);
                    }
                    break;
                }
            }
            return true;
        });

        ibtSearch.setOnClickListener(v -> {
            etSearch.setEnabled(false);

            String searchKeyword = etSearch.getText().toString().trim();
            if (searchKeyword.length() <= 0)
                etSearch.setEnabled(true);
            else {
                if (whichTopic == 1)
                    setSearchBoardList(searchKeyword);
                else if (whichTopic == 2)
                    setSearchPostList(searchKeyword);
                else
                    setSearchCollegePostList(searchKeyword);
            }
        });
    }

    private void setSearchBoardList(String keyword) {
        //TODO: 게시판 검색
    }

    private void setSearchPostList(String keyword) {
        postList.clear();
        postAdapter.notifyDataSetChanged();

        tvNoResult.setVisibility(View.GONE);
        llInfo.setVisibility(View.VISIBLE);
        RetrofitClient.getApiService().getSearchPosts(myInfo.getUserID(), boardID, keyword).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.i("SearchActivity 확인용2", response.toString());
                if (response.code() == 200) {
                    presentKeyword = keyword;
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
                    llInfo.setVisibility(View.GONE);
                    postAdapter.notifyDataSetChanged();
                }
                else {
                    llInfo.setVisibility(View.GONE);
                    tvNoResult.setVisibility(View.VISIBLE);
                    tvNoResult.setText("입력한 키워드를\n포함한 게시글이 없습니다.");
                }
                etSearch.setEnabled(true);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                etSearch.setEnabled(true);
                llInfo.setVisibility(View.GONE);
                tvNoResult.setVisibility(View.VISIBLE);
                tvNoResult.setText("게시글 로드 실패\n네트워크를 확인해 주세요.");
            }
        });
    }

    private void setSearchCollegePostList(String keyword) {
        //TODO: 단과대학의 게시글 검색
    }
}
