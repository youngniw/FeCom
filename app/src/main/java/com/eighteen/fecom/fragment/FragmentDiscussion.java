package com.eighteen.fecom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.ApplyingActivity;
import com.eighteen.fecom.R;
import com.eighteen.fecom.RetrofitClient;
import com.eighteen.fecom.adapter.DiscussionRecyclerAdapter;
import com.eighteen.fecom.data.DiscussionInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.eighteen.fecom.MainActivity.myInfo;

public class FragmentDiscussion extends Fragment {
    private boolean isOrderByLatest = true;
    private ArrayList<DiscussionInfo> discussList = null;

    private AppCompatButton btAddDiscuss, btChangeOrder;
    private DiscussionRecyclerAdapter discussAdapter;
    private TextView tvInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_discussion, container, false);

        discussList = new ArrayList<>();

        btAddDiscuss =  rootView.findViewById(R.id.fDiscussion_addDiscuss);
        btChangeOrder = rootView.findViewById(R.id.fDiscussion_changeOrder);
        tvInfo = rootView.findViewById(R.id.fDiscussion_tvInfo);

        RecyclerView rvDiscussion = rootView.findViewById(R.id.fDiscussion_rv);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvDiscussion.setLayoutManager(manager);
        discussAdapter = new DiscussionRecyclerAdapter(discussList);
        rvDiscussion.setAdapter(discussAdapter);
        rvDiscussion.addItemDecoration(new DividerItemDecoration(requireContext(), 1));

        discussionClickListener();
        updateDiscussList();

        return rootView;
    }

    private void discussionClickListener() {
        ActivityResultLauncher<Intent> startActivityResultPosting = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK)
                        updateDiscussList();
                });
        btAddDiscuss.setOnClickListener(v -> startActivityResultPosting.launch(new Intent(getContext(), ApplyingActivity.class)));

        btChangeOrder.setOnClickListener(v -> {
            isOrderByLatest = !isOrderByLatest;

            if (isOrderByLatest)
                btChangeOrder.setText(R.string.discussion_changeOrder1);
            else
                btChangeOrder.setText(R.string.discussion_changeOrder2);
            updateDiscussList();
        });
    }

    public void updateDiscussList() {
        discussList.clear();
        discussAdapter.notifyDataSetChanged();

        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText(R.string.discussion_load);
        if (isOrderByLatest) {
            RetrofitClient.getApiService().getLatestDiscussList(myInfo.getUserID()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    Log.i("FragmentDiscussion 확인용1", response.toString());
                    if (response.code() == 200) {
                        try {
                            JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                            JSONArray jsonDiscussions = result.getJSONArray("discussions");
                            for (int i=0; i<jsonDiscussions.length(); i++) {
                                JSONObject discussionObject = jsonDiscussions.getJSONObject(i);

                                int discussID = discussionObject.getInt("id");
                                int writerID = discussionObject.getInt("writer");
                                String writerNick = discussionObject.getString("writer_nickname");
                                String discussTime = discussionObject.getString("register_datetime");
                                String prosContent = discussionObject.getString("pros");
                                String consContent = discussionObject.getString("cons");
                                int myExpression = discussionObject.getInt("myexpression");
                                int totalCount = discussionObject.getInt("total_count");
                                int prosCount = discussionObject.getInt("pros_count");
                                int consCount = discussionObject.getInt("cons_count");

                                double prosRate = discussionObject.getDouble("pros_rate");
                                prosRate = Math.round(prosRate*10000) / 100.0;
                                double consRate = discussionObject.getDouble("cons_rate");
                                consRate = Math.round(consRate*10000) / 100.0;

                                discussList.add(new DiscussionInfo(discussID, writerID, writerNick, discussTime, prosContent, consContent,
                                        myExpression, totalCount, prosCount, consCount, prosRate, consRate));
                            }
                        } catch (JSONException e) { e.printStackTrace(); }

                        tvInfo.setVisibility(View.GONE);
                        discussAdapter.notifyDataSetChanged();
                    }
                    else
                        tvInfo.setText("대결이 아직 없습니다.\n대결을 신청해 보세요:)");
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    tvInfo.setText("대결 로드 실패\n네트워크를 확인해 주세요.");
                }
            });
        }
        else {      //인기순
            RetrofitClient.getApiService().getPopularDiscussList(myInfo.getUserID()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    Log.i("FragmentDiscussion 확인용2", response.toString());
                    if (response.code() == 200) {
                        try {
                            JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                            JSONArray jsonDiscussions = result.getJSONArray("discussions");
                            for (int i=0; i<jsonDiscussions.length(); i++) {
                                JSONObject discussionObject = jsonDiscussions.getJSONObject(i);

                                int discussID = discussionObject.getInt("id");
                                int writerID = discussionObject.getInt("writer");
                                String writerNick = discussionObject.getString("writer_nickname");
                                String discussTime = discussionObject.getString("register_datetime");
                                String prosContent = discussionObject.getString("pros");
                                String consContent = discussionObject.getString("cons");
                                int myExpression = discussionObject.getInt("myexpression");
                                int totalCount = discussionObject.getInt("total_count");
                                int prosCount = discussionObject.getInt("pros_count");
                                int consCount = discussionObject.getInt("cons_count");

                                double prosRate = discussionObject.getDouble("pros_rate");
                                prosRate = Math.round(prosRate*10000) / 100.0;
                                double consRate = discussionObject.getDouble("cons_rate");
                                consRate = Math.round(consRate*10000) / 100.0;

                                discussList.add(new DiscussionInfo(discussID, writerID, writerNick, discussTime, prosContent, consContent,
                                        myExpression, totalCount, prosCount, consCount, prosRate, consRate));
                            }
                        } catch (JSONException e) { e.printStackTrace(); }

                        tvInfo.setVisibility(View.GONE);
                        discussAdapter.notifyDataSetChanged();
                    }
                    else
                        tvInfo.setText("대결이 아직 없습니다.\n대결을 신청해 보세요:)");
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    tvInfo.setText("대결 로드 실패\n네트워크를 확인해 주세요.");
                }
            });
        }
    }
}
