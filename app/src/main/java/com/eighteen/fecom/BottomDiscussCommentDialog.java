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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.adapter.DiscussCommentRecyclerAdapter;
import com.eighteen.fecom.data.DiscussCommentInfo;
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

public class BottomDiscussCommentDialog extends BottomSheetDialogFragment {
    private int discussID = -1;
    private boolean isLatestOrderBy = true;
    private ArrayList<DiscussCommentInfo> commentList = null;

    private AppCompatButton ibChangeOrderBy;
    private AppCompatImageButton ibSubmit;
    private EditText etComment;
    private DiscussCommentRecyclerAdapter commentAdapter;
    private TextView tvInfo;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.bottom_sheet_comment_discuss, container, false);

        commentList = new ArrayList<>();
        Bundle extra = this.getArguments();
        if (extra != null)
            discussID = extra.getInt("discussID");

        ibChangeOrderBy = rootView.findViewById(R.id.bscDiscuss_orderBy);
        tvInfo = rootView.findViewById(R.id.bscDiscuss_commentInfo);
        etComment = rootView.findViewById(R.id.bscDiscuss_etComment);
        ibSubmit = rootView.findViewById(R.id.bscDiscuss_ibCommSubmit);

        RecyclerView rvTalkComments = rootView.findViewById(R.id.bscDiscuss_rvComments);
        LinearLayoutManager basicManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvTalkComments.setLayoutManager(basicManager);
        commentAdapter = new DiscussCommentRecyclerAdapter(commentList, this);
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
        ibChangeOrderBy.setOnClickListener(v -> {
            isLatestOrderBy = !isLatestOrderBy;

            if (isLatestOrderBy)
                ibChangeOrderBy.setText("->??????????????? ??????");
            else
                ibChangeOrderBy.setText("->??????????????? ??????");

            updateTalkComment();
        });

        ibSubmit.setOnClickListener(v -> {
            etComment.setEnabled(false);

            String comment = etComment.getText().toString().trim();
            if (comment.length() <= 0)
                etComment.setEnabled(true);
            else {
                JsonObject commentData = new JsonObject();
                commentData.addProperty("discussion_id", discussID);
                commentData.addProperty("writer", myInfo.getUserID());
                commentData.addProperty("content", comment);
                RetrofitClient.getApiService().postRegisterCommentD(commentData).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("BottomDiscussCommentDialog ?????? ?????????", response.toString());
                        if (response.code() == 200) {
                            updateTalkComment();
                            etComment.setText("");
                        }
                        else
                            Toast.makeText(getContext(), "???????????????. ?????? ?????? ????????? ????????? ?????????:)", Toast.LENGTH_SHORT).show();

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
        tvInfo.setText("????????? ?????? ????????????:)");
        if (isLatestOrderBy) {
            RetrofitClient.getApiService().getLatestDiscussComments(myInfo.getUserID(), discussID).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    Log.i("BottomDiscussCommentDialog ?????????1", response.toString());
                    if (response.code() == 200) {
                        Log.i("BottomDiscussCommentDialog ?????????1", response.body());
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
                                int commentAmILike = commentObject.getInt("thumb");
                                int commentLikeNum = commentObject.getInt("like_count");
                                int commentNotLikeNum = commentObject.getInt("dislike_count");

                                commentList.add(new DiscussCommentInfo(commentID, commenterID, commenterNick, commentTime, comment, commentAmILike, commentLikeNum, commentNotLikeNum));
                            }
                        } catch (JSONException e) { e.printStackTrace(); }

                        tvInfo.setVisibility(View.GONE);
                        commentAdapter.notifyDataSetChanged();
                    }
                    else
                        tvInfo.setText("????????? ????????????.\n????????? ????????? ?????????:)");
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    tvInfo.setText("????????? ?????? ??????\n??????????????? ????????? ?????????.");
                }
            });
        }
        else {
            RetrofitClient.getApiService().getPopularDiscussComments(myInfo.getUserID(), discussID).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    Log.i("BottomDiscussCommentDialog ?????????2", response.toString());
                    if (response.code() == 200) {
                        Log.i("BottomDiscussCommentDialog ?????????2", response.body());
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
                                int commentAmILike = commentObject.getInt("thumb");
                                int commentLikeNum = commentObject.getInt("like_count");
                                int commentNotLikeNum = commentObject.getInt("dislike_count");

                                commentList.add(new DiscussCommentInfo(commentID, commenterID, commenterNick, commentTime, comment, commentAmILike, commentLikeNum, commentNotLikeNum));
                            }
                        } catch (JSONException e) { e.printStackTrace(); }

                        tvInfo.setVisibility(View.GONE);
                        commentAdapter.notifyDataSetChanged();
                    }
                    else
                        tvInfo.setText("????????? ????????????.\n????????? ????????? ?????????:)");
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    tvInfo.setText("????????? ?????? ??????\n??????????????? ????????? ?????????.");
                }
            });
        }
    }
}
