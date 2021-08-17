package com.eighteen.fecom.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.PostActivity;
import com.eighteen.fecom.R;
import com.eighteen.fecom.RetrofitClient;
import com.eighteen.fecom.data.CommentInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentViewHolder> {
    private Context context;
    private ArrayList<CommentInfo> commentList;

    public CommentRecyclerAdapter(ArrayList<CommentInfo> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentRecyclerAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_comment, parent, false);
        CommentRecyclerAdapter.CommentViewHolder viewHolder = new CommentRecyclerAdapter.CommentViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.CommentViewHolder holder, int position) {
        if (commentList.get(position).getAnonymous() == 1) {     //TODO: 나중에 익명2와 같이 순서로 변경해야 함!
            holder.tvWriterNick.setText(R.string.anonymous);
            holder.tvWriterNick.setTextColor(ContextCompat.getColor(context, R.color.grey));
        }
        else {
            holder.tvWriterNick.setText(commentList.get(position).getCommenterInfo().getNick());
            holder.tvWriterNick.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        LocalDateTime dateNow = LocalDateTime.now();
        LocalDateTime date = LocalDateTime.parse(commentList.get(position).getCommentTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (ChronoUnit.SECONDS.between(date, dateNow) < 60)
            holder.tvTime.setText(String.valueOf(ChronoUnit.SECONDS.between(date, dateNow)).concat("초 전"));
        else if (ChronoUnit.MINUTES.between(date, dateNow) < 60)
            holder.tvTime.setText(String.valueOf(ChronoUnit.MINUTES.between(date, dateNow)).concat("분 전"));
        else if (ChronoUnit.HOURS.between(date, dateNow) < 24)
            holder.tvTime.setText(String.valueOf(ChronoUnit.HOURS.between(date, dateNow)).concat("시간 전"));
        else if (ChronoUnit.DAYS.between(date, dateNow) < 7)
            holder.tvTime.setText(String.valueOf(ChronoUnit.DAYS.between(date, dateNow)).concat("일 전"));
        else if (ChronoUnit.YEARS.between(date, dateNow) < 1)
            holder.tvTime.setText(String.valueOf(date.getMonthValue()).concat("/").concat(String.valueOf(date.getDayOfMonth())));
        else
            holder.tvTime.setText(String.valueOf(date.getYear()).substring(2).concat("/").concat(String.valueOf(date.getMonthValue())).concat("/").concat(String.valueOf(date.getDayOfMonth())));

        if (myInfo.getUserID() != commentList.get(position).getCommenterInfo().getUserID())
            holder.ibtDelete.setVisibility(View.GONE);

        if (commentList.get(position).getContent().equals("")) {
            holder.tvContent.setText("X 삭제된 댓글입니다 X");
            holder.tvContent.setTextColor(ContextCompat.getColor(context, R.color.grey));
        }
        else {
            holder.tvContent.setText(commentList.get(position).getContent());
            holder.tvContent.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        if (commentList.get(position).getAmILike() == 1)
            holder.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.red));
        else
            holder.ivLike.setColorFilter(ContextCompat.getColor(context, R.color.black));
        holder.tvLike.setText(String.valueOf(commentList.get(position).getLikeNum()));
    }

    @Override
    public int getItemCount() { return commentList.size(); }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageButton ibtDelete;
        TextView tvWriterNick, tvTime, tvContent, tvLike;
        ImageView ivLike;

        CommentViewHolder(final View itemView) {
            super(itemView);

            tvWriterNick = itemView.findViewById(R.id.commentRow_nick);
            tvTime = itemView.findViewById(R.id.commentRow_time);
            ibtDelete = itemView.findViewById(R.id.commentRow_delete);
            tvContent = itemView.findViewById(R.id.commentRow_content);
            ivLike = itemView.findViewById(R.id.commentRow_ivLike);
            tvLike = itemView.findViewById(R.id.commentRow_tvLike);

            ibtDelete.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {      //TODO: 삭제 메시지를 보여줌
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("댓글 삭제").setMessage("현재 댓글을 삭제하시겠습니까?");
                    builder.setPositiveButton("삭제", (dialog, which) ->
                            RetrofitClient.getApiService().postDeleteComment(commentList.get(pos).getCommentID()).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                    Log.i("CommentRecycler 확인용", response.toString());
                                    if (response.code() == 200)
                                        ((PostActivity) context).updatePostInfo();
                                    else
                                        Toast.makeText(context, "해당 댓글 삭제에 문제가 생겼습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                    Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요:)", Toast.LENGTH_SHORT).show();
                                }
                            }));
                    builder.setNegativeButton("취소", (dialog, id) -> dialog.cancel());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setOnShowListener(dialogInterface -> {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.main_fecom));
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.main_fecom));
                    });
                    alertDialog.show();
                }
            });
        }
    }
}
