package com.eighteen.fecom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.PostListActivity;
import com.eighteen.fecom.R;
import com.eighteen.fecom.RetrofitClient;
import com.eighteen.fecom.data.BoardInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

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
        if (boardInfoList.get(position).getAmISubscribe() == 1)
            holder.ivSubscribe.setColorFilter(ContextCompat.getColor(context, R.color.main_fecom));
        else
            holder.ivSubscribe.setImageResource(R.drawable.icon_subscribe);

        holder.ivSubscribe.setOnClickListener(v -> {
            if (boardInfoList.get(position).getAmISubscribe() == 1) {
                RetrofitClient.getApiService().postDeleteSubscribeB(myInfo.getUserID(), boardInfoList.get(position).getBoardID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("BoardRecycler 확인용1", response.toString());
                        if (response.code() == 200) {
                            boardInfoList.get(position).setAmISubscribe(0);
                            notifyItemChanged(position);
                        }
                        else
                            Toast.makeText(context, "다시 한번 시도해 주세요:)", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                RetrofitClient.getApiService().postSubscribeBoard(myInfo.getUserID(), boardInfoList.get(position).getBoardID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        Log.i("BoardRecycler 확인용2", response.toString());
                        if (response.code() == 200) {
                            boardInfoList.get(position).setAmISubscribe(1);
                            notifyItemChanged(position);
                        }
                        else
                            Toast.makeText(context, "다시 한번 시도해 주세요.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.tvBoardName.setText(boardInfoList.get(position).getBoardName());
    }

    @Override
    public int getItemCount() { return boardInfoList.size(); }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageButton ivSubscribe;
        TextView tvBoardName;

        BoardViewHolder(final View itemView) {
            super(itemView);

            ivSubscribe = itemView.findViewById(R.id.fBoardRow_ivSubscribe);
            tvBoardName = itemView.findViewById(R.id.fBoardRow_tvBoardName);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    //TODO: 해당 게시판으로 넘어감!
                    Intent showPostList = new Intent(context, PostListActivity.class);
                    Bundle bundle = new Bundle();
                        bundle.putInt("boardID", boardInfoList.get(pos).getBoardID());
                        bundle.putString("boardName", boardInfoList.get(pos).getBoardName());
                        bundle.putInt("amISubscribe", boardInfoList.get(pos).getAmISubscribe());
                    showPostList.putExtras(bundle);
                    context.startActivity(showPostList);
                }
            });
        }
    }
}
