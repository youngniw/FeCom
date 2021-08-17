package com.eighteen.fecom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.PostActivity;
import com.eighteen.fecom.R;
import com.eighteen.fecom.data.PostInfo;

import java.util.ArrayList;

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
        if (postList.get(position).getAnonymous() == 1)
            holder.tvWriterNick.setText(R.string.anonymous);
        else
            holder.tvWriterNick.setText(postList.get(position).getWriterInfo().getNick());
        holder.tvTime.setText(postList.get(position).getPostTime());
        holder.tvContent.setText(postList.get(position).getContent());
        if (postList.get(position).getAmILike() == 1)
            holder.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.red));
        else
            holder.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.black));
        holder.tvLike.setText(String.valueOf(postList.get(position).getLikeNum()));
        holder.tvComment.setText(String.valueOf(postList.get(position).getCommentNum()));
    }

    @Override
    public int getItemCount() { return postList.size(); }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvWriterNick;
        TextView tvTime;
        TextView tvContent;
        ImageView ivLike;
        TextView tvLike;
        TextView tvComment;

        PostViewHolder(final View itemView) {
            super(itemView);

            tvWriterNick = itemView.findViewById(R.id.postRow_name);
            tvTime = itemView.findViewById(R.id.postRow_time);
            tvContent = itemView.findViewById(R.id.postRow_content);
            ivLike = itemView.findViewById(R.id.postRow_ivLike);
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
