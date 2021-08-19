package com.eighteen.fecom.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.R;
import com.eighteen.fecom.RetrofitClient;
import com.eighteen.fecom.adapter.NoticeRecyclerAdapter;
import com.eighteen.fecom.data.NoticeInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

public class FragmentNotice extends Fragment {
    private ArrayList<NoticeInfo> noticeLists;

    private NoticeRecyclerAdapter noticeAdapter;
    private TextView tvInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notice, container, false);

        noticeLists = new ArrayList<>();

        tvInfo = rootView.findViewById(R.id.fNotice_tvInfo);

        RecyclerView rvNotice = rootView.findViewById(R.id.fNotice_rv);
        LinearLayoutManager noticeManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvNotice.setLayoutManager(noticeManager);
        noticeAdapter = new NoticeRecyclerAdapter(noticeLists);
        rvNotice.setAdapter(noticeAdapter);
        rvNotice.addItemDecoration(new DividerItemDecoration(requireContext(), 1));

        updateNoticeList();

        return rootView;
    }

    public void updateNoticeList() {
        noticeLists.clear();
        noticeAdapter.notifyDataSetChanged();

        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText(R.string.notice_load);
        RetrofitClient.getApiService().getNotice(myInfo.getUserID()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.i("FragmentNotice 확인용", response.toString());
                if (response.code() == 200) {
                    Log.i("FragmentNotice 확인용", response.body());
                    try {
                        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONArray jsonNotices = result.getJSONArray("notifications");
                        for (int i=0; i<jsonNotices.length(); i++) {
                            JSONObject noticeObject = jsonNotices.getJSONObject(i);

                            noticeLists.add(new NoticeInfo(noticeObject.getInt("post_id"), noticeObject.getString("post_content"),
                                    noticeObject.getInt("comment_id"), noticeObject.getString("comment_content"), noticeObject.getString("datetime")));
                        }
                    } catch (JSONException e) { e.printStackTrace(); }

                    tvInfo.setVisibility(View.GONE);
                    noticeAdapter.notifyDataSetChanged();
                }
                else if (response.code() == 400)
                    tvInfo.setText("알림이 없습니다:)");
                else
                    tvInfo.setText("알림 로딩 실패\n 다시 조회해 주세요:)");
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                tvInfo.setText(R.string.server_error);
            }
        });
    }
}
