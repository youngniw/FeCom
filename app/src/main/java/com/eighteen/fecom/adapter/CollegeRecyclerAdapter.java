package com.eighteen.fecom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.R;
import com.eighteen.fecom.data.MajorData;

import java.util.ArrayList;

public class CollegeRecyclerAdapter extends RecyclerView.Adapter<CollegeRecyclerAdapter.CollegeViewHolder> {
    private Context context;
    private ArrayList<MajorData> collegeList;

    public CollegeRecyclerAdapter(ArrayList<MajorData> collegeList) {
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
        RecyclerView rvDepartment;

        CollegeViewHolder(final View itemView) {
            super(itemView);

            ivIsClicked = itemView.findViewById(R.id.collegeRow_ivCollege);
            tvCollegeName = itemView.findViewById(R.id.collegeRow_tvCollege);
            rvDepartment = itemView.findViewById(R.id.collegeRow_rvDepartment);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    if (tvCollegeName.getCurrentTextColor() == ContextCompat.getColor(context, R.color.black)) {
                        ivIsClicked.setImageResource(R.drawable.icon_opened);
                        tvCollegeName.setTextColor(ContextCompat.getColor(context, R.color.main_fecom));

                        if (collegeList.get(pos).getDepartmentLists().size() > 0) {
                            rvDepartment.setVisibility(View.VISIBLE);
                            DepartRecyclerAdapter adapter = new DepartRecyclerAdapter(collegeList.get(pos).getDepartmentLists());
                            rvDepartment.setHasFixedSize(true);
                            rvDepartment.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                            rvDepartment.setAdapter(adapter);
                        }
                    }
                    else {
                        ivIsClicked.setImageResource(R.drawable.icon_closed);
                        tvCollegeName.setTextColor(ContextCompat.getColor(context, R.color.black));

                        rvDepartment.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
