package com.eighteen.fecom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.BoardPostActivity;
import com.eighteen.fecom.R;
import com.eighteen.fecom.data.NoticeInfo;

import java.util.ArrayList;

public class NoticeRecyclerAdapter extends RecyclerView.Adapter<NoticeRecyclerAdapter.NoticeViewHolder> {
    private Context context;
    private ArrayList<NoticeInfo> noticeInfoList;

    public NoticeRecyclerAdapter(ArrayList<NoticeInfo> noticeInfoList) {
        this.noticeInfoList = noticeInfoList;
    }

    @NonNull
    @Override
    public NoticeRecyclerAdapter.NoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_notice, parent, false);
        NoticeRecyclerAdapter.NoticeViewHolder viewHolder = new NoticeRecyclerAdapter.NoticeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeRecyclerAdapter.NoticeViewHolder holder, int position) {
        holder.tvTime.setText(noticeInfoList.get(position).getNoticeTime());

        String simplePost;
        if (noticeInfoList.get(position).getPostContent().length() > 7)
            simplePost = noticeInfoList.get(position).getPostContent().substring(0, 7);
        else
            simplePost = noticeInfoList.get(position).getPostContent();
        String about = "게시글 ".concat(simplePost).concat("...");
        holder.tvAbout.setText(about);

        String simpleComment;
        if (noticeInfoList.get(position).getCommentContent().length() > 7)
            simpleComment = noticeInfoList.get(position).getCommentContent().substring(0, 7);
        else
            simpleComment = noticeInfoList.get(position).getCommentContent();
        String content = simpleComment.concat("... 댓글이 달렸습니다.");
        holder.tvContent.setText(content);
    }

    @Override
    public int getItemCount() { return noticeInfoList.size(); }

    public class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvAbout, tvContent;

        NoticeViewHolder(final View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.noticeRow_time);
            tvAbout = itemView.findViewById(R.id.noticeRow_about);
            tvContent = itemView.findViewById(R.id.noticeRow_content);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Intent showPost = new Intent(context, BoardPostActivity.class);
                    Bundle bundle = new Bundle();
                        bundle.putBoolean("isNotice", true);
                        bundle.putInt("postID", noticeInfoList.get(pos).getPostID());
                    showPost.putExtras(bundle);
                    context.startActivity(showPost);
                }
            });
        }
    }
}
