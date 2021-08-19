package com.eighteen.fecom.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.BottomCommentDialog;
import com.eighteen.fecom.DailyTalkActivity;
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

public class DailyTalkPagerAdapter extends RecyclerView.Adapter<DailyTalkPagerAdapter.TalkViewHolder> {
    private Context context;
    private boolean isInHome;
    private ArrayList<PostInfo> talkList;

    public DailyTalkPagerAdapter(boolean isInHome, ArrayList<PostInfo> talkList) {
        this.isInHome = isInHome;
        this.talkList = talkList;
    }

    @NonNull
    @Override
    public DailyTalkPagerAdapter.TalkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;
        if (isInHome)
            view = inflater.inflate(R.layout.item_home_dailytalk, parent, false);
        else
            view = inflater.inflate(R.layout.item_dailytalk, parent, false);
        DailyTalkPagerAdapter.TalkViewHolder viewHolder = new DailyTalkPagerAdapter.TalkViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyTalkPagerAdapter.TalkViewHolder holder, int position) {
        if (!isInHome && (myInfo.getUserID() == talkList.get(position).getWriterInfo().getUserID()))
            holder.ibtDelete.setVisibility(View.VISIBLE);

        holder.tvWriterName.setText(talkList.get(position).getWriterInfo().getNick());

        LocalDateTime dateNow = LocalDateTime.now();
        LocalDateTime date = LocalDateTime.parse(talkList.get(position).getPostTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (ChronoUnit.SECONDS.between(date, dateNow) < 60)
            holder.tvTalkTime.setText(String.valueOf(ChronoUnit.SECONDS.between(date, dateNow)).concat("초 전"));
        else if (ChronoUnit.MINUTES.between(date, dateNow) < 60)
            holder.tvTalkTime.setText(String.valueOf(ChronoUnit.MINUTES.between(date, dateNow)).concat("분 전"));
        else if (ChronoUnit.HOURS.between(date, dateNow) <= 24)
            holder.tvTalkTime.setText(String.valueOf(ChronoUnit.HOURS.between(date, dateNow)).concat("시간 전"));

        holder.tvContent.setText(talkList.get(position).getContent());

        if (talkList.get(position).getAmILike() == 1)
            holder.ibtLike.setColorFilter(ContextCompat.getColor(context, R.color.red));
        else
            holder.ibtLike.setColorFilter(ContextCompat.getColor(context, R.color.black));
        holder.ibtLike.setOnClickListener(v -> {
            holder.ibtLike.setEnabled(false);

            if (talkList.get(position).getAmILike() == 1) {
                RetrofitClient.getApiService().postDeleteLikeT(myInfo.getUserID(), talkList.get(position).getPostID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("DailyTalkPager 확인용1", response.toString());
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                                talkList.get(position).setAmILike(0);
                                talkList.get(position).setLikeNum(result.getInt("like_count"));
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
                RetrofitClient.getApiService().postRegisterLikeT(myInfo.getUserID(), talkList.get(position).getPostID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("DailyTalkPager 확인용2", response.toString());
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                                talkList.get(position).setAmILike(1);
                                talkList.get(position).setLikeNum(result.getInt("like_count"));
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
        holder.tvLikeNum.setText(String.valueOf(talkList.get(position).getLikeNum()));

        holder.tvCommentNum.setText(String.valueOf(talkList.get(position).getCommentNum()));
    }

    @Override
    public int getItemCount() { return talkList.size(); }

    public class TalkViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageButton ibtDelete, ibtLike;
        LinearLayout llComment;
        TextView tvWriterName, tvTalkTime, tvContent, tvLikeNum, tvCommentNum;

        TalkViewHolder(final View itemView) {
            super(itemView);

            if (isInHome) {
                tvWriterName = itemView.findViewById(R.id.hTalkPage_writer);
                tvTalkTime = itemView.findViewById(R.id.hTalkPage_time);
                tvContent = itemView.findViewById(R.id.hTalkPage_content);
                    tvContent.setMovementMethod(new ScrollingMovementMethod());
                ibtLike = itemView.findViewById(R.id.hTalkPage_ibLike);
                tvLikeNum = itemView.findViewById(R.id.hTalkPage_tvLike);
                tvCommentNum = itemView.findViewById(R.id.hTalkPage_comment);

                itemView.setOnClickListener(v -> {
                    //TODO: 데일리 톡으로 넘어감!    -> 확인 요망!       -> TextView 누르면 안넘어감!
                    context.startActivity(new Intent(context, DailyTalkActivity.class));
                });
            }
            else {
                ibtDelete = itemView.findViewById(R.id.talkPage_ibDelete);
                tvWriterName = itemView.findViewById(R.id.talkPage_writer);
                tvTalkTime = itemView.findViewById(R.id.talkPage_time);
                tvContent = itemView.findViewById(R.id.talkPage_content);
                    tvContent.setMovementMethod(new ScrollingMovementMethod());
                ibtLike = itemView.findViewById(R.id.talkPage_ibLike);
                tvLikeNum = itemView.findViewById(R.id.talkPage_tvLike);
                llComment = itemView.findViewById(R.id.talkPage_llComment);
                tvCommentNum = itemView.findViewById(R.id.talkPage_comment);
                
                llComment.setOnClickListener(v -> {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        BottomCommentDialog commentDialog = new BottomCommentDialog();
                        Bundle bundle = new Bundle();
                            bundle.putInt("talkID", talkList.get(pos).getPostID());
                        commentDialog.setArguments(bundle);
                        commentDialog.show(((DailyTalkActivity) context).getSupportFragmentManager(), "comment_dialog");
                    }
                });

                ibtDelete.setOnClickListener(v -> {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("데일리톡 삭제").setMessage("현재 데일리톡을 삭제하시겠습니까?");
                        builder.setPositiveButton("삭제", (dialog, which) ->
                                RetrofitClient.getApiService().postDeleteTalk(talkList.get(pos).getPostID()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                        Log.i("DailyTalkPager 확인용", response.toString());
                                        if (response.code() == 200)
                                            notifyItemRemoved(pos);
                                        else
                                            Toast.makeText(context, "데일리톡 삭제에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
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
            }
        }
    }
}
