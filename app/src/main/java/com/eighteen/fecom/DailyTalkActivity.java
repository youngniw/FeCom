package com.eighteen.fecom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.eighteen.fecom.adapter.DailyTalkPagerAdapter;
import com.eighteen.fecom.data.PostInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

public class DailyTalkActivity extends AppCompatActivity {
    private ArrayList<PostInfo> dtalkLists = null;

    private DailyTalkPagerAdapter talkAdapter;
    private TextView tvInfo;
    private ViewPager2 vpDailyTalk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailytalk);

        int showID = -1;

        dtalkLists = new ArrayList<>();
        if (getIntent().hasExtra("showTalkID"))
            showID = getIntent().getExtras().getInt("showTalkID");

        Toolbar toolbar = findViewById(R.id.dailyTalk_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_dailytalk, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        tvInfo = findViewById(R.id.dailyTalk_info);

        vpDailyTalk = findViewById(R.id.dailyTalk_vp);
        talkAdapter = new DailyTalkPagerAdapter(false, dtalkLists);
        vpDailyTalk.setAdapter(talkAdapter);
        vpDailyTalk.setOffscreenPageLimit(3);
        vpDailyTalk.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(8));
        transformer.addTransformer((page, position) -> {
            float v = 1 - Math.abs(position);
            page.setScaleY(0.8f + v * 0.2f);
        });
        vpDailyTalk.setPageTransformer(transformer);

        ActivityResultLauncher<Intent> startActivityResultPosting = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK)
                        updateTalkList(false, -1);
                });
        FloatingActionButton fabAddPost = findViewById(R.id.dailyTalk_fabWrite);
        fabAddPost.setOnClickListener(v -> {
            Intent addTalk = new Intent(this, PostingActivity.class);
            Bundle bundle = new Bundle();
                bundle.putBoolean("isDailyTalk", true);
            addTalk.putExtras(bundle);
            startActivityResultPosting.launch(addTalk);
        });

        updateTalkList(true, showID);
    }

    private void toolbarListener(Toolbar toolbar) {
        ImageView ivBack = toolbar.findViewById(R.id.dailyTalk_back);
        ivBack.setOnClickListener(v -> finish());

        AppCompatImageButton ibRefresh = findViewById(R.id.dailyTalk_refresh);
        ibRefresh.setOnClickListener(v -> updateTalkList(false, -1));
    }

    public void updateTalkList(boolean initialSet, int showID) {
        dtalkLists.clear();
        talkAdapter.notifyDataSetChanged();

        tvInfo.setVisibility(View.VISIBLE);
        tvInfo.setText(R.string.dailyTalk_load);
        RetrofitClient.getApiService().getDailyTalks(myInfo.getUserID()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.i("DailyTalkActivity 확인용", response.toString());
                if (response.code() == 200) {
                    int showFirstPost = 0;
                    try {
                        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONArray jsonPosts = result.getJSONArray("dailytalks");
                        for (int i=0; i<jsonPosts.length(); i++) {
                            JSONObject postObject = jsonPosts.getJSONObject(i);

                            int talkID = postObject.getInt("id");
                            int writerID = postObject.getInt("writer");
                            String writerNick = postObject.getString("writer_nickname");
                            String postTime = postObject.getString("register_datetime");
                            String content = postObject.getString("content");
                            int amILike = postObject.getInt("thumbup");
                            int likeNum = postObject.getInt("like_count");
                            int commentNum = postObject.getInt("comment_count");

                            dtalkLists.add(new PostInfo(talkID, 0, writerID, writerNick, postTime, content, amILike, likeNum, commentNum));

                            if (showID == talkID)
                                showFirstPost = i;
                        }
                    } catch (JSONException e) { e.printStackTrace(); }

                    tvInfo.setVisibility(View.GONE);
                    talkAdapter.notifyDataSetChanged();

                    if (initialSet && showID != -1)
                        vpDailyTalk.setCurrentItem(showFirstPost);
                }
                else
                    tvInfo.setText("Daily Talk이 아직 없습니다.\n한번 작성해 보세요:)");
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                tvInfo.setText("Daily Talk 로드 실패\n네트워크를 확인해 주세요.");
            }
        });
    }
}
