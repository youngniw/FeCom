package com.eighteen.fecom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.adapter.TalkCommentRecyclerAdapter;
import com.eighteen.fecom.data.CommentInfo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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

public class BottomCommentDialog extends BottomSheetDialogFragment {
    private int talkID = -1;
    private ArrayList<CommentInfo> commentList = null;
    public boolean isChangedComment = false;

    private AppCompatImageButton ibSubmit;
    private EditText etComment;
    private TalkCommentRecyclerAdapter commentAdapter;
    private TextView tvInfo;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.bottom_sheet_comment, container, false);

        commentList = new ArrayList<>();
        Bundle extra = this.getArguments();
        if (extra != null)
            talkID = extra.getInt("talkID");
        
        tvInfo = rootView.findViewById(R.id.bscomm_commentInfo);
        etComment = rootView.findViewById(R.id.bscomm_etComment);
        ibSubmit = rootView.findViewById(R.id.bscomm_ibCommSubmit);

        RecyclerView rvTalkComments = rootView.findViewById(R.id.bscomm_rvComments);
        LinearLayoutManager basicManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvTalkComments.setLayoutManager(basicManager);
        commentAdapter = new TalkCommentRecyclerAdapter(commentList, this);
        rvTalkComments.setAdapter(commentAdapter);
        rvTalkComments.addItemDecoration(new DividerItemDecoration(requireContext(), 1));

        rvTalkComments.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            v.onTouchEvent(event);
            return true;
        });

        commentClickListener();
        updateTalkComment();

        return rootView;
    }

    private void commentClickListener() {
        ibSubmit.setOnClickListener(v -> {
            etComment.setEnabled(false);

            String comment = etComment.getText().toString().trim();
            if (comment.length() <= 0)
                etComment.setEnabled(true);
            else {
                JsonObject commentData = new JsonObject();
                commentData.addProperty("daily_id", talkID);
                commentData.addProperty("writer", myInfo.getUserID());
                commentData.addProperty("content", comment);
                RetrofitClient.getApiService().postRegisterCommentT(commentData).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("BoardCommentDialog 댓글 확인용", response.toString());
                        if (response.code() == 200) {
                            isChangedComment = true;
                            updateTalkComment();
                            etComment.setText("");
                        }
                        else
                            Toast.makeText(getContext(), "죄송합니다. 다시 한번 댓글을 제출해 주세요:)", Toast.LENGTH_SHORT).show();

                        etComment.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        etComment.setEnabled(true);
                        Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void updateTalkComment() {
        commentList.clear();
        commentAdapter.notifyDataSetChanged();

        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText("댓글을 찾고 있습니다:)");
        RetrofitClient.getApiService().getTalkComments(myInfo.getUserID(), talkID).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.i("BottomCommentDialog 확인용", response.toString());
                if (response.code() == 200) {
                    Log.i("BottomCommentDialog 확인용", response.body());
                    try {
                        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONArray jsonComments = result.getJSONArray("comments");
                        for (int i=0; i<jsonComments.length(); i++) {
                            JSONObject commentObject = jsonComments.getJSONObject(i);
                            int commentID = commentObject.getInt("id");
                            int commenterID = commentObject.getInt("writer");
                            String commenterNick = commentObject.getString("writer_nickname");
                            String commentTime = commentObject.getString("register_datetime");
                            String comment = commentObject.getString("content");
                            int commentAmILike = commentObject.getInt("thumbup");
                            int commentLikeNum = commentObject.getInt("like_count");

                            commentList.add(new CommentInfo(commentID, commenterID, commenterNick, commentTime, comment, commentAmILike, commentLikeNum));
                        }
                    } catch (JSONException e) { e.printStackTrace(); }

                    tvInfo.setVisibility(View.GONE);
                    commentAdapter.notifyDataSetChanged();
                }
                else
                    tvInfo.setText("댓글이 없습니다.\n댓글을 작성해 보세요:)");
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                tvInfo.setText("게시글 로드 실패\n네트워크를 확인해 주세요.");
            }
        });
    }
}
