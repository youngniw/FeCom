package com.eighteen.fecom;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.adapter.CommentRecyclerAdapter;
import com.eighteen.fecom.data.CommentInfo;
import com.eighteen.fecom.data.PostInfo;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

public class PostActivity extends AppCompatActivity {
    private boolean isWriter = false;
    private boolean isDeleted = false, isChangedLike = false, isChangedComment = false;

    private PostInfo postInfo;
    private ArrayList<CommentInfo> commentList = null;

    private AppCompatImageButton ibLike, ibCommSubmit;
    private CheckBox cbAnonymous;
    private CommentRecyclerAdapter commentAdapter;
    private EditText etComment;
    private NestedScrollView nsvPost;
    private TextView tvInfo, tvWriterNick, tvTime, tvContent, tvLikeNum, tvCommentNum, tvCommentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postInfo = getIntent().getParcelableExtra("postInfo");
        if (myInfo.getUserID() == postInfo.getWriterInfo().getUserID())
            isWriter = true;
        commentList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.post_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_post, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        nsvPost = findViewById(R.id.post_nsv);
        tvInfo = findViewById(R.id.post_tvInfo);
        tvWriterNick = findViewById(R.id.post_writerName);
        tvTime = findViewById(R.id.post_time);
        tvContent = findViewById(R.id.post_content);
        ibLike = findViewById(R.id.post_ibLike);
        tvLikeNum = findViewById(R.id.post_likeNum);
        tvCommentNum = findViewById(R.id.post_commentNum);
        tvCommentInfo = findViewById(R.id.post_commentInfo);
        cbAnonymous = findViewById(R.id.post_cbAnonymous);
        etComment = findViewById(R.id.post_etComment);
        ibCommSubmit = findViewById(R.id.post_ibCommSubmit);

        showPostInfo();
        postListener();

        RecyclerView rvComment = findViewById(R.id.post_rvComments);
        LinearLayoutManager commentManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvComment.setLayoutManager(commentManager);
        commentAdapter = new CommentRecyclerAdapter(commentList);
        rvComment.setAdapter(commentAdapter);
        rvComment.addItemDecoration(new DividerItemDecoration(this, 1));

        updatePostInfo();
    }

    @Override
    public void finish() {
        if (isDeleted || isChangedLike || isChangedComment) {
            Intent deletedIntent = new Intent();
            setResult(RESULT_OK, deletedIntent);
        }
        else {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
        }

        super.finish();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatImageButton ivBack = toolbar.findViewById(R.id.post_back);
        ivBack.setOnClickListener(v -> finish());

        AppCompatImageButton ivDelete = toolbar.findViewById(R.id.post_delete);
        if (isWriter) {
            ivDelete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                builder.setTitle("글 삭제").setMessage("현재 글을 삭제하시겠습니까?");
                builder.setPositiveButton("삭제", (dialog, which) ->
                        RetrofitClient.getApiService().postDeletePost(postInfo.getPostID()).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                Log.i("PostActivity 확인용1", response.toString());

                                if (response.code() == 200) {
                                    isDeleted = true;
                                    finish();
                                }
                                else
                                    Toast.makeText(PostActivity.this, "해당 글 삭제에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                Toast.makeText(PostActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }));
                builder.setNegativeButton("취소", (dialog, id) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.setOnShowListener(dialogInterface -> {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.main_fecom));
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.main_fecom));
                });
                alertDialog.show();
            });
        }
        else
            ivDelete.setVisibility(View.GONE);

        AppCompatImageButton ivRefresh = toolbar.findViewById(R.id.post_refresh);
        ivRefresh.setOnClickListener(v -> updatePostInfo());
    }

    private void postListener() {
        ibLike.setOnClickListener(v -> {
            ibLike.setEnabled(false);

            if (postInfo.getAmILike() == 1) {
                RetrofitClient.getApiService().postDeleteLikeP(myInfo.getUserID(), postInfo.getPostID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("PostActivity 확인용", response.toString());
                        if (response.code() == 200) {
                            isChangedLike = true;
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));

                                postInfo.setAmILike(0);
                                postInfo.setLikeNum(result.getInt("like_count"));
                                ibLike.setColorFilter(ContextCompat.getColor(PostActivity.this, R.color.black));
                                tvLikeNum.setText(String.valueOf(postInfo.getLikeNum()));
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else
                            Toast.makeText(PostActivity.this, "다시 한번 시도해 주세요:)", Toast.LENGTH_SHORT).show();
                        ibLike.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        ibLike.setEnabled(true);
                        Toast.makeText(PostActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                RetrofitClient.getApiService().postRegisterLikeP(myInfo.getUserID(), postInfo.getPostID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("PostRecycler 확인용", response.toString());
                        if (response.code() == 200) {
                            isChangedLike = true;
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));

                                postInfo.setAmILike(1);
                                postInfo.setLikeNum(result.getInt("like_count"));
                                ibLike.setColorFilter(ContextCompat.getColor(PostActivity.this, R.color.red));
                                tvLikeNum.setText(String.valueOf(postInfo.getLikeNum()));
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else
                            Toast.makeText(PostActivity.this, "다시 한번 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        ibLike.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        ibLike.setEnabled(true);
                        Toast.makeText(PostActivity.this, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ibCommSubmit.setOnClickListener(v -> {
            etComment.setEnabled(false);

            String comment = etComment.getText().toString().trim();
            if (comment.length() <= 0)
                etComment.setEnabled(true);
            else {
                JsonObject commentData = new JsonObject();
                commentData.addProperty("post_id", postInfo.getPostID());
                commentData.addProperty("writer", myInfo.getUserID());
                commentData.addProperty("content", comment);
                if (cbAnonymous.isChecked())
                    commentData.addProperty("anonymous", 1);
                else
                    commentData.addProperty("anonymous", 0);
                RetrofitClient.getApiService().postRegisterComment(commentData).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("PostRecycler 댓글 확인용", response.toString());
                        etComment.setEnabled(true);

                        if (response.code() == 200) {
                            isChangedComment = true;
                            updatePostInfo();
                            etComment.setText("");
                            nsvPost.post(() -> nsvPost.fullScroll(View.FOCUS_DOWN));
                        }
                        else
                            Toast.makeText(PostActivity.this, "죄송합니다. 다시 한번 댓글을 전송해 주세요:)", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        etComment.setEnabled(true);
                        Toast.makeText(PostActivity.this, "서버와 연결되지 않습니다. 네트워크를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void showPostInfo() {
        if (postInfo.getAnonymous() == 1)
            tvWriterNick.setText(R.string.anonymous);
        else
            tvWriterNick.setText(postInfo.getWriterInfo().getNick());
        tvTime.setText(postInfo.getPostTime());
        tvContent.setText(postInfo.getContent());
        if (postInfo.getAmILike() == 1)
            ibLike.setColorFilter(ContextCompat.getColor(this, R.color.red));
        else
            ibLike.setColorFilter(ContextCompat.getColor(this, R.color.black));
        tvLikeNum.setText(String.valueOf(postInfo.getLikeNum()));
        tvCommentNum.setText(String.valueOf(postInfo.getCommentNum()));

        if (commentList.size() == 0)
            tvCommentInfo.setVisibility(View.VISIBLE);
    }

    public void updatePostInfo() {
        commentList.clear();
        commentAdapter.notifyDataSetChanged();

        nsvPost.setVisibility(View.INVISIBLE);
        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText("글을 불러오고 있습니다:)");
        tvCommentInfo.setVisibility(View.GONE);
        RetrofitClient.getApiService().getPostInfo(myInfo.getUserID(), postInfo.getPostID()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.i("PostActivity 확인용", response.toString());
                if (response.code() == 200) {
                    Log.i("PostActivity 확인용", response.body());
                    try {
                        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));

                        JSONObject postObject = result.getJSONObject("post");
                        int postID = postObject.getInt("id");
                        int postAnonymous = postObject.getInt("anonymous");
                        int writerID = postObject.getInt("writer");
                        String writerNick = postObject.getString("writer_nickname");
                        String postTime = postObject.getString("register_datetime");
                        String content = postObject.getString("content");
                        int amILike = postObject.getInt("thumbup");
                        int likeNum = postObject.getInt("like_count");
                        int commentNum = postObject.getInt("comment_count");
                        postInfo = new PostInfo(postID, postAnonymous, writerID, writerNick, postTime, content, amILike, likeNum, commentNum);

                        if (result.has("comments")) {
                            JSONArray jsonComments = result.getJSONArray("comments");
                            for (int i=0; i<jsonComments.length(); i++) {
                                JSONObject commentObject = jsonComments.getJSONObject(i);
                                int commentID = commentObject.getInt("id");
                                int commentAnonymous = commentObject.getInt("anonymous");

                                int anonymousNum = -1;
                                if (!commentObject.isNull("anonymous_num"))
                                    anonymousNum = commentObject.getInt("anonymous_num");

                                int commenterID = commentObject.getInt("writer");
                                String commenterNick = commentObject.getString("writer_nickname");
                                String commentTime = commentObject.getString("register_datetime");
                                String comment = commentObject.getString("content");
                                int commentAmILike = commentObject.getInt("thumbup");
                                int commentLikeNum = commentObject.getInt("comment_like_count");

                                commentList.add(new CommentInfo(commentID, commentAnonymous, anonymousNum, commenterID, commenterNick, commentTime, comment, commentAmILike, commentLikeNum));
                            }
                        }
                    } catch (JSONException e) { e.printStackTrace(); }

                    tvInfo.setVisibility(View.GONE);
                    nsvPost.setVisibility(View.VISIBLE);
                    showPostInfo();

                    commentAdapter.notifyDataSetChanged();
                }
                else if (response.code() == 400)
                    tvInfo.setText("삭제된 글입니다. :<");
                else
                    tvInfo.setText("게시글 로드 실패\n네트워크를 확인해주세요.");
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                tvInfo.setText("게시글 로드 실패\n네트워크를 확인해주세요.");
            }
        });
    }
}
