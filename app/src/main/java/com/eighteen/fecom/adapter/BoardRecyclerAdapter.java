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
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.PostListActivity;
import com.eighteen.fecom.R;
import com.eighteen.fecom.data.BoardInfo;

import java.util.ArrayList;

public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.BoardViewHolder> {
    private Context context;
    private ArrayList<BoardInfo> boardInfoList;

    public BoardRecyclerAdapter(ArrayList<BoardInfo> boardInfoList) {
        this.boardInfoList = boardInfoList;
    }

    @NonNull
    @Override
    public BoardRecyclerAdapter.BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_board, parent, false);
        BoardRecyclerAdapter.BoardViewHolder viewHolder = new BoardRecyclerAdapter.BoardViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardRecyclerAdapter.BoardViewHolder holder, int position) {
        if (boardInfoList.get(position).getIsISubscribe())
            holder.ivSubscribe.setColorFilter(ContextCompat.getColor(context, R.color.main_fecom));
        else
            holder.ivSubscribe.setImageResource(R.drawable.icon_subscribe);
        holder.tvBoardName.setText(boardInfoList.get(position).getBoardName());
    }

    @Override
    public int getItemCount() { return boardInfoList.size(); }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSubscribe;
        TextView tvBoardName;

        BoardViewHolder(final View itemView) {
            super(itemView);

            ivSubscribe = itemView.findViewById(R.id.fBoardRow_ivSubscribe);
            tvBoardName = itemView.findViewById(R.id.fBoardRow_tvBoardName);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    //TODO: 해당 게시판으로 넘어감!
                    context.startActivity(new Intent(context, PostListActivity.class));
                }
            });
        }
    }
}
