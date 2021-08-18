package com.eighteen.fecom.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.BottomCommentDialog;
import com.eighteen.fecom.DailyTalkActivity;
import com.eighteen.fecom.R;
import com.eighteen.fecom.data.PostInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

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
            holder.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.red));
        else
            holder.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.black));
        holder.tvLikeNum.setText(String.valueOf(talkList.get(position).getLikeNum()));
        holder.tvCommentNum.setText(String.valueOf(talkList.get(position).getCommentNum()));

        //like 이미지 클릭 시!
    }

    @Override
    public int getItemCount() { return talkList.size(); }

    public class TalkViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llComment;
        TextView tvWriterName, tvTalkTime, tvContent, tvLikeNum, tvCommentNum;
        ImageView ivLike;

        TalkViewHolder(final View itemView) {
            super(itemView);

            if (isInHome) {
                tvWriterName = itemView.findViewById(R.id.hTalkPage_writer);
                tvTalkTime = itemView.findViewById(R.id.hTalkPage_time);
                tvContent = itemView.findViewById(R.id.hTalkPage_content);
                ivLike = itemView.findViewById(R.id.hTalkPage_ivLike);
                tvLikeNum = itemView.findViewById(R.id.hTalkPage_tvLike);
                tvCommentNum = itemView.findViewById(R.id.hTalkPage_comment);

                itemView.setOnClickListener(v -> {
                    //TODO: 데일리 톡으로 넘어감!
                    context.startActivity(new Intent(context, DailyTalkActivity.class));
                });
            }
            else {
                tvWriterName = itemView.findViewById(R.id.talkPage_writer);
                tvTalkTime = itemView.findViewById(R.id.talkPage_time);
                tvContent = itemView.findViewById(R.id.talkPage_content);
                ivLike = itemView.findViewById(R.id.talkPage_ivLike);
                tvLikeNum = itemView.findViewById(R.id.talkPage_tvLike);
                llComment = itemView.findViewById(R.id.talkPage_llComment);
                tvCommentNum = itemView.findViewById(R.id.talkPage_comment);
                
                llComment.setOnClickListener(v -> {
                    //TODO: 댓글이 보여짐
                    BottomCommentDialog commentDialog = new BottomCommentDialog();
                    commentDialog.show(((DailyTalkActivity) context).getSupportFragmentManager(), "commentSheet");
                });
            }
        }
    }
}
