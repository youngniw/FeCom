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

import com.eighteen.fecom.PostActivity;
import com.eighteen.fecom.R;
import com.eighteen.fecom.RetrofitClient;
import com.eighteen.fecom.data.CommentInfo;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentViewHolder> {
    private Context context;
    private ArrayList<CommentInfo> commentList;

    public CommentRecyclerAdapter(ArrayList<CommentInfo> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentRecyclerAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_comment, parent, false);
        CommentRecyclerAdapter.CommentViewHolder viewHolder = new CommentRecyclerAdapter.CommentViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.CommentViewHolder holder, int position) {
        if (commentList.get(position).getAnonymous() == 1) {
            holder.tvWriterNick.setText("익명".concat(String.valueOf(commentList.get(position).getAnonymousNum())));
            holder.tvWriterNick.setTextColor(ContextCompat.getColor(context, R.color.grey));
        }
        else {
            holder.tvWriterNick.setText(commentList.get(position).getCommenterInfo().getNick());
            holder.tvWriterNick.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        holder.tvTime.setText(commentList.get(position).getCommentTime());
        holder.etContent.setText(commentList.get(position).getContent());

        if (commentList.get(position).getAmILike() == 1)
            holder.ibtLike.setColorFilter(ContextCompat.getColor(context, R.color.red));
        else
            holder.ibtLike.setColorFilter(ContextCompat.getColor(context, R.color.black));
        holder.ibtLike.setOnClickListener(v -> {
            //TODO: 데일리톡일 때는 이 함수 사용 안됨!
            holder.ibtLike.setEnabled(false);

            if (commentList.get(position).getAmILike() == 1) {
                RetrofitClient.getApiService().postDeleteLikeC(myInfo.getUserID(), commentList.get(position).getCommentID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("CommentRecycler 확인용1", response.toString());
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                                commentList.get(position).setAmILike(0);
                                commentList.get(position).setLikeNum(result.getInt("like_count"));
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
                RetrofitClient.getApiService().postRegisterLikeC(myInfo.getUserID(), commentList.get(position).getCommentID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("CommentRecycler 확인용2", response.toString());
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                                commentList.get(position).setAmILike(1);
                                commentList.get(position).setLikeNum(result.getInt("like_count"));
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

        if (myInfo.getUserID() != commentList.get(position).getCommenterInfo().getUserID())
            holder.llToWriter.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() { return commentList.size(); }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageButton ibtLike, ibtEdit, ibtDelete, ibtCancel, ibtSubmit;
        LinearLayout llMenu, llToWriter, llEdit;
        TextView tvWriterNick, tvTime, tvLike;
        EditText etContent;

        CommentViewHolder(final View itemView) {
            super(itemView);

            tvWriterNick = itemView.findViewById(R.id.commentRow_nick);
            tvTime = itemView.findViewById(R.id.commentRow_time);
            etContent = itemView.findViewById(R.id.commentRow_content);
            llMenu = itemView.findViewById(R.id.commentRow_llMenu);
            llToWriter = itemView.findViewById(R.id.comment_llToWriter);
            ibtLike = itemView.findViewById(R.id.commentRow_ibLike);
            tvLike = itemView.findViewById(R.id.commentRow_tvLike);
            ibtEdit = itemView.findViewById(R.id.commentRow_ibEdit);
            ibtDelete = itemView.findViewById(R.id.commentRow_ibDelete);
            llEdit = itemView.findViewById(R.id.commentRow_llEdit);
            ibtCancel = itemView.findViewById(R.id.commentRow_ibEditCancel);
            ibtSubmit = itemView.findViewById(R.id.commentRow_ibEditSubmit);

            ibtDelete.setOnClickListener(v -> {
                //TODO: 데일리톡일 때는 이 함수 사용 안됨!
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("댓글 삭제").setMessage("현재 댓글을 삭제하시겠습니까?");
                    builder.setPositiveButton("삭제", (dialog, which) ->
                            RetrofitClient.getApiService().postDeleteComment(commentList.get(pos).getCommentID()).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                    Log.i("CommentRecycler 확인용", response.toString());
                                    if (response.code() == 200)
                                        ((PostActivity) context).updatePostInfo(false);
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
                //TODO: 데일리톡일 때는 이 함수 사용 안됨!
                llMenu.setVisibility(View.GONE);
                llEdit.setVisibility(View.VISIBLE);
                etContent.setEnabled(true);
                etContent.setBackgroundResource(R.drawable.bg_board);
            });

            ibtCancel.setOnClickListener(v -> {
                //TODO: 데일리톡일 때는 이 함수 사용 안됨!
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
                //TODO: 데일리톡일 때는 이 함수 사용 안됨!
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
                        RetrofitClient.getApiService().postEditComment(commentData).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                Log.i("CommentRecycler 댓글 확인용", response.toString());
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
                                Toast.makeText(context, "서버와 연결되지 않습니다. 네트워크를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
}
