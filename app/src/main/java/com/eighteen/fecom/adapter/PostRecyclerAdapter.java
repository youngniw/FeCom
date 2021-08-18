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

import com.eighteen.fecom.PostActivity;
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

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.PostViewHolder> {
    private Context context;
    private ArrayList<PostInfo> postList;
    private ActivityResultLauncher<Intent> startActivityResultPost;

    public PostRecyclerAdapter(ArrayList<PostInfo> postList, ActivityResultLauncher<Intent> startActivityResultPost) {
        this.postList = postList;
        this.startActivityResultPost = startActivityResultPost;
    }

    @NonNull
    @Override
    public PostRecyclerAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_post, parent, false);
        PostRecyclerAdapter.PostViewHolder viewHolder = new PostRecyclerAdapter.PostViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostRecyclerAdapter.PostViewHolder holder, int position) {
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
            holder.tvTime.setText(String.valueOf(ChronoUnit.SECONDS.between(date, dateNow)).concat("초 전"));
        else if (ChronoUnit.MINUTES.between(date, dateNow) < 60)
            holder.tvTime.setText(String.valueOf(ChronoUnit.MINUTES.between(date, dateNow)).concat("분 전"));
        else if (ChronoUnit.HOURS.between(date, dateNow) < 24)
            holder.tvTime.setText(String.valueOf(ChronoUnit.HOURS.between(date, dateNow)).concat("시간 전"));
        else if (ChronoUnit.DAYS.between(date, dateNow) < 7)
            holder.tvTime.setText(String.valueOf(ChronoUnit.DAYS.between(date, dateNow)).concat("일 전"));
        else if (ChronoUnit.YEARS.between(date, dateNow) < 1)
            holder.tvTime.setText(String.valueOf(date.getMonthValue()).concat("/").concat(String.valueOf(date.getDayOfMonth())));
        else
            holder.tvTime.setText(String.valueOf(date.getYear()).substring(2).concat("/").concat(String.valueOf(date.getMonthValue())).concat("/").concat(String.valueOf(date.getDayOfMonth())));

        holder.tvContent.setText(postList.get(position).getContent());

        //TODO: 이미지버튼으로 바꾸자!!!! -> padding 넣어야 함!!
        if (postList.get(position).getAmILike() == 1)
            holder.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.red));
        else
            holder.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.black));
        holder.ivLike.setOnClickListener(v -> {
            if (postList.get(position).getAmILike() == 1) {
                RetrofitClient.getApiService().postDeleteLikeP(myInfo.getUserID(), postList.get(position).getPostID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("PostRecycler 확인용1", response.toString());
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                                postList.get(position).setAmILike(0);
                                postList.get(position).setLikeNum(result.getInt("like_count"));
                                notifyItemChanged(position);
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else
                            Toast.makeText(context, "다시 한번 시도해 주세요:)", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                RetrofitClient.getApiService().postRegisterLikeP(myInfo.getUserID(), postList.get(position).getPostID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("PostRecycler 확인용2", response.toString());
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                                postList.get(position).setAmILike(1);
                                postList.get(position).setLikeNum(result.getInt("like_count"));
                                notifyItemChanged(position);
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else
                            Toast.makeText(context, "다시 한번 시도해 주세요.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
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
        AppCompatImageButton ivLike;
        TextView tvWriterNick, tvTime, tvContent, tvLike, tvComment;

        PostViewHolder(final View itemView) {
            super(itemView);

            tvWriterNick = itemView.findViewById(R.id.postRow_name);
            tvTime = itemView.findViewById(R.id.postRow_time);
            tvContent = itemView.findViewById(R.id.postRow_content);
            ivLike = itemView.findViewById(R.id.postRow_ibLike);
            tvLike = itemView.findViewById(R.id.postRow_tvLike);
            tvComment = itemView.findViewById(R.id.postRow_tvComment);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    //TODO: 게시판글/전공글으로 넘어감!
                    Intent showPostIntent = new Intent(context, PostActivity.class);
                    Bundle bundle = new Bundle();
                        bundle.putParcelable("postInfo", postList.get(pos));
                    showPostIntent.putExtras(bundle);
                    startActivityResultPost.launch(showPostIntent);
                }
            });
        }
    }
}
