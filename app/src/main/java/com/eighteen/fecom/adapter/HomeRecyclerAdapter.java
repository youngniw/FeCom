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

import com.eighteen.fecom.PostListActivity;
import com.eighteen.fecom.R;
import com.eighteen.fecom.data.SimpleBoardInfo;

import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<SimpleBoardInfo> simpleBoardList;

    public HomeRecyclerAdapter(ArrayList<SimpleBoardInfo> simpleBoardList) {
        this.simpleBoardList = simpleBoardList;
    }

    @NonNull
    @Override
    public HomeRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_home_board, parent, false);
        HomeRecyclerAdapter.ItemViewHolder viewHolder = new HomeRecyclerAdapter.ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.tvBoardName.setText(simpleBoardList.get(position).getName());
        holder.tvContent.setText(simpleBoardList.get(position).getContent());
    }

    @Override
    public int getItemCount() { return simpleBoardList.size(); }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvBoardName;
        TextView tvContent;

        ItemViewHolder(final View itemView) {
            super(itemView);

            tvBoardName = itemView.findViewById(R.id.fHomeBoardRow_boardName);
            tvContent = itemView.findViewById(R.id.fHomeBoardRow_content);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Intent showPostList = new Intent(context, PostListActivity.class);
                    Bundle bundle = new Bundle();
                        bundle.putInt("boardID", simpleBoardList.get(pos).getBoardID());
                        bundle.putString("boardName", simpleBoardList.get(pos).getName());
                    showPostList.putExtras(bundle);
                    context.startActivity(showPostList);
                }
            });
        }
    }
}
