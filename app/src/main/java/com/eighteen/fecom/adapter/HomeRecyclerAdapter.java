package com.eighteen.fecom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.R;
import com.eighteen.fecom.data.SummaryData;

import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<SummaryData> summaryDataList;

    public HomeRecyclerAdapter(ArrayList<SummaryData> summaryDataList) {
        this.summaryDataList = summaryDataList;
    }

    @NonNull
    @Override
    public HomeRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_home_summary, parent, false);
        HomeRecyclerAdapter.ItemViewHolder viewHolder = new HomeRecyclerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.tvTopic.setText(summaryDataList.get(position).getTopic());
        holder.tvContent.setText(summaryDataList.get(position).getContent());
    }

    @Override
    public int getItemCount() { return summaryDataList.size(); }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTopic;
        TextView tvContent;

        ItemViewHolder(final View itemView) {
            super(itemView);

            tvTopic = itemView.findViewById(R.id.homeSummaryRow_topic);
            tvContent = itemView.findViewById(R.id.homeSummaryRow_content);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    //TODO: 해당 게시판 혹은 데일리톡으로 넘어감!
                }
            });
        }
    }
}
