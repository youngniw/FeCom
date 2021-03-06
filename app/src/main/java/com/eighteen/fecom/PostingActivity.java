package com.eighteen.fecom;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.eighteen.fecom.data.PostInfo;
import com.google.gson.JsonObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

public class PostingActivity extends AppCompatActivity {
    private boolean isSubmitted = false;
    private boolean isEditing = false;
    private int whereFrom = 0;     //1은 게시판, 2는 데일리톡, 3은 전공 커뮤니티
    private int boardOrCollegeID = -1;
    private PostInfo boardPostInfo = null;

    private CheckBox cbAnonymous;
    private EditText etContent;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        if (getIntent().hasExtra("boardID")) {
            whereFrom = 1;
            boardOrCollegeID = getIntent().getExtras().getInt("boardID");

            if (getIntent().hasExtra("postInfo")) {     //글 수정
                isEditing = true;
                boardPostInfo = getIntent().getParcelableExtra("postInfo");
            }
        }
        else
            whereFrom = 2;

        Toolbar toolbar = findViewById(R.id.posting_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_posting, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        cbAnonymous = findViewById(R.id.posting_cbAnonymous);
        if (whereFrom == 2)
            cbAnonymous.setVisibility(View.GONE);
        etContent = findViewById(R.id.posting_etContent);
        tvError = findViewById(R.id.posting_errorInfo);

        if (whereFrom == 1 && isEditing) {      //게시글 수정일 때 설정!
            cbAnonymous.setChecked(boardPostInfo.getAnonymous() == 1);
            etContent.setText(boardPostInfo.getContent());
        }

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvError.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    public void finish() {
        if (isSubmitted) {
            Intent submittedIntent = new Intent();
            setResult(RESULT_OK, submittedIntent);
        }
        else {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
        }

        super.finish();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatButton btCancel = toolbar.findViewById(R.id.posting_cancel);
        btCancel.setOnClickListener(v -> finish());

        TextView tvTitle = toolbar.findViewById(R.id.posting_topic);
        if (whereFrom == 1)
            tvTitle.setText(R.string.board);
        else
            tvTitle.setText(R.string.tab_dailytalk);

        AppCompatButton btComplete = toolbar.findViewById(R.id.posting_complete);
        btComplete.setOnClickListener(v -> {
            tvError.setVisibility(View.GONE);

            String content = etContent.getText().toString().trim();
            if (content.length() == 0) {
                tvError.setVisibility(View.VISIBLE);
                tvError.setText(R.string.posting_error);
            }
            else {
                if (isEditing) {
                    int anonymous = cbAnonymous.isChecked() ? 1 : 0;

                    if (whereFrom == 1 && (!content.equals(boardPostInfo.getContent()) || anonymous != boardPostInfo.getAnonymous())) {       //게시글 수정 완료
                        JsonObject postData = new JsonObject();
                        postData.addProperty("post_id", boardPostInfo.getPostID());
                        postData.addProperty("content", content);
                        postData.addProperty("anonymous", anonymous);
                        RetrofitClient.getApiService().postEditPostInfo(postData).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                if (response.code() == 200) {
                                    isSubmitted = true;
                                    finish();
                                }
                                else {
                                    tvError.setVisibility(View.VISIBLE);
                                    tvError.setText(R.string.signup_submit_error);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                tvError.setVisibility(View.VISIBLE);
                                tvError.setText(R.string.server_error);
                            }
                        });
                    }
                    else {
                        tvError.setVisibility(View.VISIBLE);
                        tvError.setText("기존 글과 동일합니다.");
                    }
                }
                else {
                    if (whereFrom == 1) {
                        JsonObject postData = new JsonObject();
                        postData.addProperty("board_id", boardOrCollegeID);
                        postData.addProperty("writer", myInfo.getUserID());
                        postData.addProperty("content", content);
                        if (cbAnonymous.isChecked())
                            postData.addProperty("anonymous", 1);
                        else
                            postData.addProperty("anonymous", 0);
                        RetrofitClient.getApiService().postPostInfo(postData).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                if (response.code() == 200) {
                                    isSubmitted = true;
                                    finish();
                                }
                                else {
                                    tvError.setVisibility(View.VISIBLE);
                                    tvError.setText(R.string.signup_submit_error);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                tvError.setVisibility(View.VISIBLE);
                                tvError.setText(R.string.server_error);
                            }
                        });
                    }
                    else {
                        JsonObject talkData = new JsonObject();
                        talkData.addProperty("writer", myInfo.getUserID());
                        talkData.addProperty("content", content);
                        RetrofitClient.getApiService().postRegisterTalk(talkData).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                if (response.code() == 200) {
                                    isSubmitted = true;
                                    finish();
                                }
                                else {
                                    tvError.setVisibility(View.VISIBLE);
                                    tvError.setText(R.string.signup_submit_error);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                tvError.setVisibility(View.VISIBLE);
                                tvError.setText(R.string.server_error);
                            }
                        });
                    }
                }
            }
        });
    }
}
