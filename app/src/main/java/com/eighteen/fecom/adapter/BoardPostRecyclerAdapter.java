package com.eighteen.fecom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.BoardPostActivity;
import com.eighteen.fecom.R;
import com.eighteen.fecom.RetrofitClient;
import com.eighteen.fecom.data.PostInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

public class BoardPostRecyclerAdapter extends RecyclerView.Adapter<BoardPostRecyclerAdapter.PostViewHolder> {
    private Context context;
    int boardID;
    private ArrayList<PostInfo> postList;
    private ActivityResultLauncher<Intent> startActivityResultPost;

    public BoardPostRecyclerAdapter(ArrayList<PostInfo> postList, int boardID, ActivityResultLauncher<Intent> startActivityResultPost) {
        this.postList = postList;
        this.boardID = boardID;
        this.startActivityResultPost = startActivityResultPost;
    }

    @NonNull
    @Override
    public BoardPostRecyclerAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_post_board, parent, false);
        BoardPostRecyclerAdapter.PostViewHolder viewHolder = new BoardPostRecyclerAdapter.PostViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardPostRecyclerAdapter.PostViewHolder holder, int position) {
        if (postList.get(position).getAnonymous() == 1) {
            holder.tvWriterNick.setText(R.string.anonymous);
            holder.tvWriterNick.setTextColor(ContextCompat.getColor(context, R.color.grey));
        }
        else {
            holder.tvWriterNick.setText(postList.get(position).getWriterInfo().getNick());
            holder.tvWriterNick.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        LocalDateTime dateNow = LocalDateTime.now();
        LocalDateTime date = LocalDateTime.parse(postList.get(position).getPostTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (ChronoUnit.SECONDS.between(date, dateNow) < 60)
            holder.tvTime.setText(String.valueOf(ChronoUnit.SECONDS.between(date, dateNow)).concat("??? ???"));
        else if (ChronoUnit.MINUTES.between(date, dateNow) < 60)
            holder.tvTime.setText(String.valueOf(ChronoUnit.MINUTES.between(date, dateNow)).concat("??? ???"));
        else if (ChronoUnit.HOURS.between(date, dateNow) < 24)
            holder.tvTime.setText(String.valueOf(ChronoUnit.HOURS.between(date, dateNow)).concat("?????? ???"));
        else if (ChronoUnit.DAYS.between(date, dateNow) < 7)
            holder.tvTime.setText(String.valueOf(ChronoUnit.DAYS.between(date, dateNow)).concat("??? ???"));
        else if (ChronoUnit.YEARS.between(date, dateNow) < 1)
            holder.tvTime.setText(String.valueOf(date.getMonthValue()).concat("/").concat(String.valueOf(date.getDayOfMonth())));
        else
            holder.tvTime.setText(String.valueOf(date.getYear()).substring(2).concat("/").concat(String.valueOf(date.getMonthValue())).concat("/").concat(String.valueOf(date.getDayOfMonth())));

        holder.tvContent.setText(postList.get(position).getContent().replace(" ", "\u00A0"));

        if (postList.get(position).getAmILike() == 1)
            holder.ibtLike.setColorFilter(ContextCompat.getColor(context, R.color.red));
        else
            holder.ibtLike.setColorFilter(ContextCompat.getColor(context, R.color.black));
        holder.ibtLike.setOnClickListener(v -> {
            holder.ibtLike.setEnabled(false);

            if (postList.get(position).getAmILike() == 1) {
                RetrofitClient.getApiService().postDeleteLikeP(myInfo.getUserID(), postList.get(position).getPostID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("PostRecycler ?????????1", response.toString());
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                                postList.get(position).setAmILike(0);
                                postList.get(position).setLikeNum(result.getInt("like_count"));
                                notifyItemChanged(position);
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else
                            Toast.makeText(context, "?????? ?????? ????????? ?????????:)", Toast.LENGTH_SHORT).show();
                        holder.ibtLike.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        holder.ibtLike.setEnabled(true);
                        Toast.makeText(context, "????????? ???????????? ???????????????. ????????? ?????????.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                RetrofitClient.getApiService().postRegisterLikeP(myInfo.getUserID(), postList.get(position).getPostID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("PostRecycler ?????????2", response.toString());
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                                postList.get(position).setAmILike(1);
                                postList.get(position).setLikeNum(result.getInt("like_count"));
                                notifyItemChanged(position);
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else
                            Toast.makeText(context, "?????? ?????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                        holder.ibtLike.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        holder.ibtLike.setEnabled(true);
                        Toast.makeText(context, "????????? ???????????? ???????????????. ????????? ?????????.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.tvLike.setText(String.valueOf(postList.get(position).getLikeNum()));

        holder.tvComment.setText(String.valueOf(postList.get(position).getCommentNum()));
    }

    @Override
    public int getItemCount() { return postList.size(); }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageButton ibtLike;
        TextView tvWriterNick, tvTime, tvContent, tvLike, tvComment;

        PostViewHolder(final View itemView) {
            super(itemView);

            tvWriterNick = itemView.findViewById(R.id.bPostRow_name);
            tvTime = itemView.findViewById(R.id.bPostRow_time);
            tvContent = itemView.findViewById(R.id.bPostRow_content);
            ibtLike = itemView.findViewById(R.id.bPostRow_ibLike);
            tvLike = itemView.findViewById(R.id.bPostRow_tvLike);
            tvComment = itemView.findViewById(R.id.bPostRow_tvComment);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Intent showPostIntent = new Intent(context, BoardPostActivity.class);
                    Bundle bundle = new Bundle();
                        bundle.putInt("boardID", boardID);
                        bundle.putParcelable("postInfo", postList.get(pos));
                    showPostIntent.putExtras(bundle);
                    startActivityResultPost.launch(showPostIntent);
                }
            });
        }
    }
}
