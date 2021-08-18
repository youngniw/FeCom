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

import com.eighteen.fecom.CollegePostActivity;
import com.eighteen.fecom.R;
import com.eighteen.fecom.data.CollegePostInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class CollegePostRecyclerAdapter extends RecyclerView.Adapter<CollegePostRecyclerAdapter.CollegePostViewHolder> {
    private Context context;
    private ArrayList<CollegePostInfo> collegePostList;
    private ActivityResultLauncher<Intent> startActivityResultPost;

    private HashMap<String, Integer> univMap = new HashMap<String, Integer>() {{
        put("숙명여대", R.drawable.logo_sookmyung);
        put("이화여대", R.drawable.logo_ewah);
        put("성신여대", R.drawable.logo_sungshin);
        put("덕성여대", R.drawable.logo_duksung);
        put("서울여대", R.drawable.logo_seoul);
        put("동덕여대", R.drawable.logo_dongduk);
    }};

    public CollegePostRecyclerAdapter(ArrayList<CollegePostInfo> collegePostList, ActivityResultLauncher<Intent> startActivityResultPost) {
        this.collegePostList = collegePostList;
        this.startActivityResultPost = startActivityResultPost;
    }

    @NonNull
    @Override
    public CollegePostRecyclerAdapter.CollegePostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_post_college, parent, false);
        CollegePostRecyclerAdapter.CollegePostViewHolder viewHolder = new CollegePostRecyclerAdapter.CollegePostViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CollegePostRecyclerAdapter.CollegePostViewHolder holder, int position) {
        holder.ivUnivLogo.setBackgroundResource(univMap.get(collegePostList.get(position).getWriterInfo().getUnivName()));
        holder.tvWriterUniv.setText(collegePostList.get(position).getWriterInfo().getUnivName());
        holder.tvTime.setText(collegePostList.get(position).getPostTime());
        holder.tvContent.setText(collegePostList.get(position).getContent());
        if (collegePostList.get(position).getAmILike() == 1)
            holder.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.red));
        else
            holder.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.black));
        holder.tvLike.setText(String.valueOf(collegePostList.get(position).getLikeNum()));
        holder.tvComment.setText(String.valueOf(collegePostList.get(position).getCommentNum()));
    }

    @Override
    public int getItemCount() { return collegePostList.size(); }

    public class CollegePostViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUnivLogo;
        TextView tvWriterUniv;
        TextView tvTime;
        TextView tvContent;
        ImageView ivLike;
        TextView tvLike;
        TextView tvComment;

        CollegePostViewHolder(final View itemView) {
            super(itemView);

            ivUnivLogo = itemView.findViewById(R.id.cPostRow_logo);
            tvWriterUniv = itemView.findViewById(R.id.cPostRow_univ);
            tvTime = itemView.findViewById(R.id.cPostRow_time);
            tvContent = itemView.findViewById(R.id.cPostRow_content);
            ivLike = itemView.findViewById(R.id.cPostRow_ivLike);
            tvLike = itemView.findViewById(R.id.cPostRow_tvLike);
            tvComment = itemView.findViewById(R.id.cPostRow_tvComment);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Intent showPostIntent = new Intent(context, CollegePostActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("postInfo", collegePostList.get(pos));
                    showPostIntent.putExtras(bundle);
                    startActivityResultPost.launch(showPostIntent);
                }
            });
        }
    }
}
