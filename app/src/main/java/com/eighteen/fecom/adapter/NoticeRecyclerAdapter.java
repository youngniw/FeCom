package com.eighteen.fecom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.R;
import com.eighteen.fecom.data.NoticeData;

import java.util.ArrayList;

public class NoticeRecyclerAdapter extends RecyclerView.Adapter<NoticeRecyclerAdapter.NoticeViewHolder> {
    private Context context;
    private ArrayList<NoticeData> noticeDataList;

    public NoticeRecyclerAdapter(ArrayList<NoticeData> noticeDataList) {
        this.noticeDataList = noticeDataList;
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
        holder.tvAbout.setText(noticeDataList.get(position).getNoticeAbout());
        holder.tvContent.setText(noticeDataList.get(position).getNoticeContent());
    }

    @Override
    public int getItemCount() { return noticeDataList.size(); }

    public class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView tvAbout;
        TextView tvContent;

        NoticeViewHolder(final View itemView) {
            super(itemView);

            tvAbout = itemView.findViewById(R.id.noticeRow_about);
            tvContent = itemView.findViewById(R.id.noticeRow_content);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    //TODO: 게시판글/전공글/데일리톡으로 넘어감!(알림에 대한)
                }
            });
        }
    }
}
