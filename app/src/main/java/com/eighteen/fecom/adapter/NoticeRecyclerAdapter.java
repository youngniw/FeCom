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
        holder.tvAbout.setText(noticeInfoList.get(position).getNoticeAbout());
        holder.tvContent.setText(noticeInfoList.get(position).getNoticeContent());
    }

    @Override
    public int getItemCount() { return noticeInfoList.size(); }

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
                    context.startActivity(new Intent(context, PostListActivity.class));
                    //혹은 데일리톡 화면!
                    //context.startActivity(new Intent(context, DailyTalkActivity.class));
                }
            });
        }
    }
}
