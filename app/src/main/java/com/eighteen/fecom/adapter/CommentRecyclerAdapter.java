package com.eighteen.fecom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.R;
import com.eighteen.fecom.data.CommentInfo;

import java.util.ArrayList;

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
        holder.tvWriterName.setText(commentList.get(position).getWriterName());
        holder.tvTime.setText(commentList.get(position).getCommentTime());

        holder.tvDelete.setVisibility(View.GONE);   //TODO: 작성자일 때만 보여줘야 함!

        holder.tvContent.setText(commentList.get(position).getContent());
        if (commentList.get(position).getIsILike())
            holder.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.red));
        else
            holder.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.black));
        holder.tvLike.setText(String.valueOf(commentList.get(position).getLikeNum()));
    }

    @Override
    public int getItemCount() { return commentList.size(); }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvWriterName, tvTime, tvDelete, tvContent, tvLike;
        ImageView ivLike;

        CommentViewHolder(final View itemView) {
            super(itemView);

            tvWriterName = itemView.findViewById(R.id.commentRow_name);
            tvTime = itemView.findViewById(R.id.commentRow_time);
            tvDelete = itemView.findViewById(R.id.commentRow_delete);
            tvContent = itemView.findViewById(R.id.commentRow_content);
            ivLike = itemView.findViewById(R.id.commentRow_ivLike);
            tvLike = itemView.findViewById(R.id.commentRow_tvLike);

            tvDelete.setOnClickListener(v -> {
                //TODO: 삭제 메시지를 보여줌
            });
        }
    }
}
