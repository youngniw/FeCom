package com.eighteen.fecom.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.BottomDiscussCommentDialog;
import com.eighteen.fecom.R;
import com.eighteen.fecom.RetrofitClient;
import com.eighteen.fecom.data.DiscussCommentInfo;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

public class DiscussCommentRecyclerAdapter extends RecyclerView.Adapter<DiscussCommentRecyclerAdapter.DCommentViewHolder> {
    private Context context;
    private ArrayList<DiscussCommentInfo> commentList;
    private BottomDiscussCommentDialog bottomDiscussCommentDialog;

    public DiscussCommentRecyclerAdapter(ArrayList<DiscussCommentInfo> commentList, BottomDiscussCommentDialog bottomDiscussCommentDialog) {
        this.commentList = commentList;
        this.bottomDiscussCommentDialog = bottomDiscussCommentDialog;
    }

    @NonNull
    @Override
    public DiscussCommentRecyclerAdapter.DCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_comment, parent, false);
        DiscussCommentRecyclerAdapter.DCommentViewHolder viewHolder = new DiscussCommentRecyclerAdapter.DCommentViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussCommentRecyclerAdapter.DCommentViewHolder holder, int position) {
        holder.tvWriterNick.setText(commentList.get(position).getCommenterInfo().getNick());
        holder.tvTime.setText(commentList.get(position).getCommentTime());
        holder.etContent.setText(commentList.get(position).getContent());

        if (commentList.get(position).getAmILike() == -1) {
            holder.ibtLike.setColorFilter(ContextCompat.getColor(context, R.color.black));
            holder.ibtNotLike.setColorFilter(ContextCompat.getColor(context, R.color.red));
        }
        else if (commentList.get(position).getAmILike() == 0) {
            holder.ibtLike.setColorFilter(ContextCompat.getColor(context, R.color.black));
            holder.ibtNotLike.setColorFilter(ContextCompat.getColor(context, R.color.black));
        }
        else {      //1일 때
            holder.ibtLike.setColorFilter(ContextCompat.getColor(context, R.color.red));
            holder.ibtNotLike.setColorFilter(ContextCompat.getColor(context, R.color.black));
        }
        holder.ibtLike.setOnClickListener(v -> {
            holder.ibtLike.setEnabled(false);

            if (commentList.get(position).getAmILike() == 1) {
                RetrofitClient.getApiService().postDeleteLikeDC(myInfo.getUserID(), commentList.get(position).getCommentID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("DiscussCommentRecycler 확인용1", response.toString());
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                                commentList.get(position).setAmILike(0);
                                commentList.get(position).setLikeNum(result.getInt("like_count"));
                                commentList.get(position).setNotLikeNum(result.getInt("dislike_count"));
                                notifyItemChanged(position);
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else
                            Toast.makeText(context, "다시 한번 시도해 주세요:)", Toast.LENGTH_SHORT).show();
                        holder.ibtLike.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        holder.ibtLike.setEnabled(true);
                        Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                RetrofitClient.getApiService().postRegisterLikeDC(myInfo.getUserID(), commentList.get(position).getCommentID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("DiscussCommentRecycler 확인용2", response.toString());
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                                commentList.get(position).setAmILike(1);
                                commentList.get(position).setLikeNum(result.getInt("like_count"));
                                commentList.get(position).setNotLikeNum(result.getInt("dislike_count"));
                                notifyItemChanged(position);
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else
                            Toast.makeText(context, "다시 한번 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        holder.ibtLike.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        holder.ibtLike.setEnabled(true);
                        Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.tvLike.setText(String.valueOf(commentList.get(position).getLikeNum()));
        holder.ibtNotLike.setOnClickListener(v -> {
            holder.ibtNotLike.setEnabled(false);

            if (commentList.get(position).getAmILike() == -1) {
                RetrofitClient.getApiService().postDeleteNotLikeDC(myInfo.getUserID(), commentList.get(position).getCommentID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("DiscussCommentRecycler 확인용3", response.toString());
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                                commentList.get(position).setAmILike(0);
                                commentList.get(position).setLikeNum(result.getInt("like_count"));
                                commentList.get(position).setNotLikeNum(result.getInt("dislike_count"));
                                notifyItemChanged(position);
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else
                            Toast.makeText(context, "다시 한번 시도해 주세요:)", Toast.LENGTH_SHORT).show();
                        holder.ibtNotLike.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        holder.ibtNotLike.setEnabled(true);
                        Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                RetrofitClient.getApiService().postRegisterNotLikeDC(myInfo.getUserID(), commentList.get(position).getCommentID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("DiscussCommentRecycler 확인용4", response.toString());
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                                commentList.get(position).setAmILike(-1);
                                commentList.get(position).setLikeNum(result.getInt("like_count"));
                                commentList.get(position).setNotLikeNum(result.getInt("dislike_count"));
                                notifyItemChanged(position);
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else
                            Toast.makeText(context, "다시 한번 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        holder.ibtNotLike.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        holder.ibtNotLike.setEnabled(true);
                        Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.tvNotLike.setText(String.valueOf(commentList.get(position).getNotLikeNum()));

        if (myInfo.getUserID() != commentList.get(position).getCommenterInfo().getUserID())
            holder.llToWriter.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() { return commentList.size(); }

    public class DCommentViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageButton ibtLike, ibtNotLike, ibtEdit, ibtDelete, ibtCancel, ibtSubmit;
        LinearLayout llMenu, llToWriter, llEdit;
        TextView tvWriterNick, tvTime, tvLike, tvNotLike;
        EditText etContent;

        DCommentViewHolder(final View itemView) {
            super(itemView);

            tvWriterNick = itemView.findViewById(R.id.commentRow_nick);
            tvTime = itemView.findViewById(R.id.commentRow_time);
            etContent = itemView.findViewById(R.id.commentRow_content);
            llMenu = itemView.findViewById(R.id.commentRow_llMenu);
            llToWriter = itemView.findViewById(R.id.comment_llToWriter);
            ibtLike = itemView.findViewById(R.id.commentRow_ibLike);
            tvLike = itemView.findViewById(R.id.commentRow_tvLike);
            ibtNotLike = itemView.findViewById(R.id.commentRow_ibNotLike);
            tvNotLike = itemView.findViewById(R.id.commentRow_tvNotLike);
            ibtEdit = itemView.findViewById(R.id.commentRow_ibEdit);
            ibtDelete = itemView.findViewById(R.id.commentRow_ibDelete);
            llEdit = itemView.findViewById(R.id.commentRow_llEdit);
            ibtCancel = itemView.findViewById(R.id.commentRow_ibEditCancel);
            ibtSubmit = itemView.findViewById(R.id.commentRow_ibEditSubmit);

            ibtDelete.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("댓글 삭제").setMessage("현재 댓글을 삭제하시겠습니까?");
                    builder.setPositiveButton("삭제", (dialog, which) ->
                            RetrofitClient.getApiService().postDeleteCommentD(commentList.get(pos).getCommentID()).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                    Log.i("DiscussCommentRecycler 확인용", response.toString());
                                    if (response.code() == 200)
                                        bottomDiscussCommentDialog.updateTalkComment();
                                    else
                                        Toast.makeText(context, "해당 댓글 삭제에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                    Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }));
                    builder.setNegativeButton("취소", (dialog, id) -> dialog.cancel());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setOnShowListener(dialogInterface -> {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.main_fecom));
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.main_fecom));
                    });
                    alertDialog.show();
                }
            });

            ibtEdit.setOnClickListener(v -> {
                llMenu.setVisibility(View.GONE);
                llEdit.setVisibility(View.VISIBLE);
                etContent.setEnabled(true);
                etContent.setBackgroundResource(R.drawable.bg_board);
            });

            ibtCancel.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    llMenu.setVisibility(View.VISIBLE);
                    llEdit.setVisibility(View.GONE);
                    etContent.setEnabled(false);
                    etContent.setText(commentList.get(pos).getContent());
                    etContent.setBackgroundColor(Color.TRANSPARENT);
                }
            });

            ibtSubmit.setOnClickListener(v -> {
                etContent.setEnabled(false);

                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    String comment = etContent.getText().toString().trim();
                    if (comment.length() <= 0)
                        etContent.setEnabled(true);
                    else {
                        JsonObject commentData = new JsonObject();
                        commentData.addProperty("comment_id", commentList.get(pos).getCommentID());
                        commentData.addProperty("content", comment);
                        RetrofitClient.getApiService().postEditCommentD(commentData).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                Log.i("DiscussCommentRecycler 댓글수정 확인용", response.toString());
                                if (response.code() == 200) {
                                    commentList.get(pos).setContent(comment);

                                    llMenu.setVisibility(View.VISIBLE);
                                    llEdit.setVisibility(View.GONE);
                                    etContent.setText(commentList.get(pos).getContent());
                                    etContent.setBackgroundColor(Color.TRANSPARENT);

                                    notifyItemChanged(pos);
                                } else {
                                    etContent.setEnabled(true);
                                    Toast.makeText(context, "죄송합니다. 다시 한번 전송해 주세요:)", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                etContent.setEnabled(true);
                                Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
}
