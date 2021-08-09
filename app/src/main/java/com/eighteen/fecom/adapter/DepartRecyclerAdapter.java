package com.eighteen.fecom.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.PostListActivity;
import com.eighteen.fecom.R;

import java.util.ArrayList;

public class DepartRecyclerAdapter extends RecyclerView.Adapter<DepartRecyclerAdapter.DepartmentViewHolder> {
    private Context context;
    private ArrayList<String> departmentList;

    public DepartRecyclerAdapter(ArrayList<String> departmentList) {
        this.departmentList = departmentList;
    }

    @NonNull
    @Override
    public DepartRecyclerAdapter.DepartmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_major_department, parent, false);
        DepartRecyclerAdapter.DepartmentViewHolder viewHolder = new DepartRecyclerAdapter.DepartmentViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DepartRecyclerAdapter.DepartmentViewHolder holder, int position) {
        holder.tvCollegeName.setText(departmentList.get(position));
    }

    @Override
    public int getItemCount() { return departmentList.size(); }

    public class DepartmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvCollegeName;

        DepartmentViewHolder(final View itemView) {
            super(itemView);

            tvCollegeName = itemView.findViewById(R.id.fDepartmentRow_tv);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    //TODO: 해당 학과의 글 목록이 보여아함(Intent)
                    context.startActivity(new Intent(context, PostListActivity.class));
                }
            });
        }
    }
}
