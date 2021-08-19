package com.eighteen.fecom.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.BottomDiscussCommentDialog;
import com.eighteen.fecom.MainActivity;
import com.eighteen.fecom.R;
import com.eighteen.fecom.RetrofitClient;
import com.eighteen.fecom.data.DiscussionInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

public class DiscussionRecyclerAdapter extends RecyclerView.Adapter<DiscussionRecyclerAdapter.DiscussionViewHolder> {
    private Context context;
    private ArrayList<DiscussionInfo> discussList;

    public DiscussionRecyclerAdapter(ArrayList<DiscussionInfo> discussList) {
        this.discussList = discussList;
    }

    @NonNull
    @Override
    public DiscussionRecyclerAdapter.DiscussionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_discussion, parent, false);
        DiscussionRecyclerAdapter.DiscussionViewHolder viewHolder = new DiscussionRecyclerAdapter.DiscussionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussionRecyclerAdapter.DiscussionViewHolder holder, int position) {
        int total = discussList.get(position).getProsCount() + discussList.get(position).getConsCount();
        holder.tvParticipantNum.setText(String.valueOf(total));

        LocalDateTime dateNow = LocalDateTime.now();
        LocalDateTime date = LocalDateTime.parse(discussList.get(position).getDiscussTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (ChronoUnit.SECONDS.between(date, dateNow) < 60)
            holder.tvTime.setText(String.valueOf(ChronoUnit.SECONDS.between(date, dateNow)).concat("초 전"));
        else if (ChronoUnit.MINUTES.between(date, dateNow) < 60)
            holder.tvTime.setText(String.valueOf(ChronoUnit.MINUTES.between(date, dateNow)).concat("분 전"));
        else if (ChronoUnit.HOURS.between(date, dateNow) < 24)
            holder.tvTime.setText(String.valueOf(ChronoUnit.HOURS.between(date, dateNow)).concat("시간 전"));
        else
            holder.tvTime.setText(String.valueOf(date.getYear()).substring(2).concat(".").concat(String.valueOf(date.getMonthValue())).concat(".").concat(String.valueOf(date.getDayOfMonth())));

        holder.tvPros.setText(discussList.get(position).getProsContent().replace(" ", "\u00A0"));
        holder.tvCons.setText(discussList.get(position).getConsContent().replace(" ", "\u00A0"));

        holder.tvProsRate.setText(String.valueOf(discussList.get(position).getProsRate()).concat("%"));
        holder.tvConsRate.setText(String.valueOf(discussList.get(position).getConsRate()).concat("%"));

        if (discussList.get(position).getTotalCount() == 0) {
            holder.tvProsRate.setBackgroundResource(R.drawable.bg_pros_none);
            holder.tvConsRate.setBackgroundResource(R.drawable.bg_cons_none);
            holder.tvProsRate.setTextColor(ContextCompat.getColor(context, R.color.blue));
            holder.tvConsRate.setTextColor(ContextCompat.getColor(context, R.color.red));

            LinearLayout.LayoutParams prosParams = (LinearLayout.LayoutParams) holder.tvProsRate.getLayoutParams();
            prosParams.weight = 0.5f;
            holder.tvProsRate.setLayoutParams(prosParams);
            LinearLayout.LayoutParams consParams = (LinearLayout.LayoutParams) holder.tvConsRate.getLayoutParams();
            consParams.weight = 0.5f;
            holder.tvConsRate.setLayoutParams(consParams);
        }
        else {
            holder.tvProsRate.setBackgroundResource(R.drawable.bg_pros);
            holder.tvConsRate.setBackgroundResource(R.drawable.bg_cons);
            holder.tvProsRate.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tvConsRate.setTextColor(ContextCompat.getColor(context, R.color.white));

            LinearLayout.LayoutParams prosParams = (LinearLayout.LayoutParams) holder.tvProsRate.getLayoutParams();
            prosParams.weight = (float) discussList.get(position).getProsRate();
            holder.tvProsRate.setLayoutParams(prosParams);
            LinearLayout.LayoutParams consParams = (LinearLayout.LayoutParams) holder.tvConsRate.getLayoutParams();
            consParams.weight = (float) discussList.get(position).getConsRate();
            holder.tvConsRate.setLayoutParams(consParams);
        }

        holder.tvWriterNick.setText(discussList.get(position).getWriterInfo().getNick());

        if (discussList.get(position).getMyExpression() == -1) {    //반대
            holder.ibPros.setColorFilter(ContextCompat.getColor(context, R.color.black));
            holder.ibCons.setColorFilter(ContextCompat.getColor(context, R.color.red));
        }
        else if (discussList.get(position).getMyExpression() == 0) {
            holder.ibPros.setColorFilter(ContextCompat.getColor(context, R.color.black));
            holder.ibCons.setColorFilter(ContextCompat.getColor(context, R.color.black));
        }
        else {      //찬성
            holder.ibPros.setColorFilter(ContextCompat.getColor(context, R.color.blue));
            holder.ibCons.setColorFilter(ContextCompat.getColor(context, R.color.black));
        }
        holder.ibPros.setOnClickListener(v -> {
            holder.ibPros.setEnabled(false);

            if (discussList.get(position).getMyExpression() == 1) {     //찬성 취소
                RetrofitClient.getApiService().postDeletePro(myInfo.getUserID(), discussList.get(position).getDiscussID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));

                                discussList.get(position).setMyExpression(result.getInt("myexpression"));
                                discussList.get(position).setTotalCount(result.getInt("total_count"));
                                discussList.get(position).setProsCount(result.getInt("pros_count"));
                                discussList.get(position).setConsCount(result.getInt("cons_count"));

                                double prosRate = result.getDouble("pros_rate");
                                prosRate = Math.round(prosRate*10000) / 100.0;
                                discussList.get(position).setProsRate(prosRate);

                                double consRate = result.getDouble("cons_rate");
                                consRate = Math.round(consRate*10000) / 100.0;
                                discussList.get(position).setConsRate(consRate);

                                notifyItemChanged(position);
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else
                            Toast.makeText(context, "다시 한번 시도해 주세요:)", Toast.LENGTH_SHORT).show();
                        holder.ibPros.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        holder.ibPros.setEnabled(true);
                        Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                RetrofitClient.getApiService().postExpressPro(myInfo.getUserID(), discussList.get(position).getDiscussID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));

                                discussList.get(position).setMyExpression(result.getInt("myexpression"));
                                discussList.get(position).setTotalCount(result.getInt("total_count"));
                                discussList.get(position).setProsCount(result.getInt("pros_count"));
                                discussList.get(position).setConsCount(result.getInt("cons_count"));

                                double prosRate = result.getDouble("pros_rate");
                                prosRate = Math.round(prosRate*10000) / 100.0;
                                discussList.get(position).setProsRate(prosRate);

                                double consRate = result.getDouble("cons_rate");
                                consRate = Math.round(consRate*10000) / 100.0;
                                discussList.get(position).setConsRate(consRate);

                                notifyItemChanged(position);
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else
                            Toast.makeText(context, "다시 한번 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        holder.ibPros.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        holder.ibPros.setEnabled(true);
                        Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.tvProsCount.setText(String.valueOf(discussList.get(position).getProsCount()));
        holder.ibCons.setOnClickListener(v -> {
            holder.ibCons.setEnabled(false);

            if (discussList.get(position).getMyExpression() == -1) {
                RetrofitClient.getApiService().postDeleteCon(myInfo.getUserID(), discussList.get(position).getDiscussID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));

                                discussList.get(position).setMyExpression(result.getInt("myexpression"));
                                discussList.get(position).setTotalCount(result.getInt("total_count"));
                                discussList.get(position).setProsCount(result.getInt("pros_count"));
                                discussList.get(position).setConsCount(result.getInt("cons_count"));

                                double prosRate = result.getDouble("pros_rate");
                                prosRate = Math.round(prosRate*10000) / 100.0;
                                discussList.get(position).setProsRate(prosRate);

                                double consRate = result.getDouble("cons_rate");
                                consRate = Math.round(consRate*10000) / 100.0;
                                discussList.get(position).setConsRate(consRate);

                                notifyItemChanged(position);
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else
                            Toast.makeText(context, "다시 한번 시도해 주세요:)", Toast.LENGTH_SHORT).show();
                        holder.ibCons.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        holder.ibCons.setEnabled(true);
                        Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                RetrofitClient.getApiService().postExpressCon(myInfo.getUserID(), discussList.get(position).getDiscussID()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 200) {
                            try {
                                JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));

                                discussList.get(position).setMyExpression(result.getInt("myexpression"));
                                discussList.get(position).setTotalCount(result.getInt("total_count"));
                                discussList.get(position).setProsCount(result.getInt("pros_count"));
                                discussList.get(position).setConsCount(result.getInt("cons_count"));

                                double prosRate = result.getDouble("pros_rate");
                                prosRate = Math.round(prosRate*10000) / 100.0;
                                discussList.get(position).setProsRate(prosRate);

                                double consRate = result.getDouble("cons_rate");
                                consRate = Math.round(consRate*10000) / 100.0;
                                discussList.get(position).setConsRate(consRate);

                                notifyItemChanged(position);
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else
                            Toast.makeText(context, "다시 한번 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        holder.ibCons.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        holder.ibCons.setEnabled(true);
                        Toast.makeText(context, "서버와 연결되지 않았습니다. 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.tvConsCount.setText(String.valueOf(discussList.get(position).getConsCount()));
    }

    @Override
    public int getItemCount() { return discussList.size(); }

    public class DiscussionViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageButton ibPros, ibCons;
        TextView tvParticipantNum, tvTime, tvPros, tvCons, tvProsRate, tvConsRate, tvWriterNick, tvProsCount, tvConsCount;

        DiscussionViewHolder(final View itemView) {
            super(itemView);

            tvParticipantNum = itemView.findViewById(R.id.discussionRow_tvTotal);
            tvTime = itemView.findViewById(R.id.discussionRow_date);
            tvPros = itemView.findViewById(R.id.discussionRow_pros);
            tvCons = itemView.findViewById(R.id.discussionRow_cons);
            tvProsRate = itemView.findViewById(R.id.discussionRow_prosRate);
            tvConsRate = itemView.findViewById(R.id.discussionRow_consRate);
            tvWriterNick = itemView.findViewById(R.id.discussionRow_nick);
            tvProsCount = itemView.findViewById(R.id.discussionRow_prosCount);
            tvConsCount = itemView.findViewById(R.id.discussionRow_consCount);
            ibPros = itemView.findViewById(R.id.discussionRow_ibPros);
            ibCons = itemView.findViewById(R.id.discussionRow_ibCons);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    BottomDiscussCommentDialog dCommentDialog = new BottomDiscussCommentDialog();
                    Bundle bundle = new Bundle();
                        bundle.putInt("discussID", discussList.get(pos).getDiscussID());
                    dCommentDialog.setArguments(bundle);
                    dCommentDialog.show(((MainActivity) context).getSupportFragmentManager(), "discuss_comment_dialog");
                }
            });
        }
    }
}
