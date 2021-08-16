package com.eighteen.fecom.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.PostListActivity;
import com.eighteen.fecom.R;
import com.eighteen.fecom.data.MajorInfo;

import java.util.ArrayList;

public class CollegeRecyclerAdapter extends RecyclerView.Adapter<CollegeRecyclerAdapter.CollegeViewHolder> {
    private Context context;
    private ArrayList<MajorInfo> collegeList;

    public CollegeRecyclerAdapter(ArrayList<MajorInfo> collegeList) {
        this.collegeList = collegeList;
    }

    @NonNull
    @Override
    public CollegeRecyclerAdapter.CollegeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_major_college, parent, false);
        CollegeRecyclerAdapter.CollegeViewHolder viewHolder = new CollegeRecyclerAdapter.CollegeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CollegeRecyclerAdapter.CollegeViewHolder holder, int position) {
        holder.tvCollegeName.setText(collegeList.get(position).getCollegeName());
    }

    @Override
    public int getItemCount() { return collegeList.size(); }

    public class CollegeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIsClicked;
        TextView tvCollegeName;

        CollegeViewHolder(final View itemView) {
            super(itemView);

            ivIsClicked = itemView.findViewById(R.id.fCollegeRow_ivCollege);
            tvCollegeName = itemView.findViewById(R.id.fCollegeRow_tvCollege);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    //TODO: 해당 계열의 글 목록이 보여아함(Intent)
                    context.startActivity(new Intent(context, PostListActivity.class));
                }
            });
        }
    }
}
