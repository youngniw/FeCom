package com.eighteen.fecom.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.eighteen.fecom.R;
import com.eighteen.fecom.RetrofitClient;
import com.eighteen.fecom.adapter.DailyTalkPagerAdapter;
import com.eighteen.fecom.adapter.HomeRecyclerAdapter;
import com.eighteen.fecom.data.PostInfo;
import com.eighteen.fecom.data.SimpleBoardInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

public class FragmentHome extends Fragment {
    ArrayList<PostInfo> dtalkLists = null;
    ArrayList<SimpleBoardInfo> bSubLists = null;

    AppCompatImageButton ibRefreshTalk, ibRefreshBoard;
    DailyTalkPagerAdapter talkAdapter;
    HomeRecyclerAdapter boardAdapter;
    ImageView ivTalkError, ivBoardError;
    LinearLayout llTalkError, llBoardError;
    TextView tvTalkError, tvNoTalk, tvBoardError, tvNoBoard;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        dtalkLists = new ArrayList<>();
        bSubLists = new ArrayList<>();

        //데일리톡 Top 10 임시 데이터 생성(TODO: 추후 삭제)
        dtalkLists.add(new PostInfo(1, 0, 1, "능소능소", "1시간 전", "오늘 남자친구랑 헤어졌는데.. \n이 친구가 바람을 피웠어요.. \n진짜 열이 받아서.. XX... 오늘 하루만 같이 욕해주세요;;.... 아오.... 이런 거지같은...\n 길가다가 넘어져라...!!", 1, 32, 2));
        dtalkLists.add(new PostInfo(2, 0, 2, "하이하이", "3시간 전", "경영학과 과제 세상 많아.. 진짜 교수님.. 이게 말이 된다고 생각하세요??", 0, 10, 9));
        dtalkLists.add(new PostInfo(3, 0,3, "능소화", "5시간 전", "오늘 학교 앞 호박떡 집에 갔는데,,\n웬일로 딱 하나 남음:)\n여러분:) 제가 막차 탔어요>_<", 1, 22, 3));
        dtalkLists.add(new PostInfo(4, 0,4, "하이헬로", "8시간 전", "투데이 한강뷰:) 미쳐버렸다!!!>_<", 1, 9, 3));

        ibRefreshTalk = rootView.findViewById(R.id.fHome_refreshTalk);
        llTalkError = rootView.findViewById(R.id.fHome_llDailyError);
        ivTalkError = rootView.findViewById(R.id.fHome_ivTalkError);
        tvTalkError = rootView.findViewById(R.id.fHome_tvTalkError);
        tvNoTalk = rootView.findViewById(R.id.fHome_tvNoTalk);
        ViewPager2 vpDailyTalk = rootView.findViewById(R.id.fHome_vpDailyTalk);
        talkAdapter = new DailyTalkPagerAdapter(true, dtalkLists);
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

        ibRefreshBoard = rootView.findViewById(R.id.fHome_refreshBoard);
        llBoardError = rootView.findViewById(R.id.fHome_llBoardError);
        ivBoardError = rootView.findViewById(R.id.fHome_ivBoardError);
        tvBoardError = rootView.findViewById(R.id.fHome_tvBoardError);
        tvNoBoard = rootView.findViewById(R.id.fHome_tvNoBoard);
        RecyclerView rvBoard = rootView.findViewById(R.id.fHome_rvBoard);
        LinearLayoutManager bManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvBoard.setLayoutManager(bManager);
        boardAdapter = new HomeRecyclerAdapter(bSubLists);
        rvBoard.setAdapter(boardAdapter);

        setTopTalkPager();
        setSubscribeBoard();

        homeClickListener();

        return rootView;
    }

    private void homeClickListener() {
        llTalkError.setOnClickListener(v -> setTopTalkPager());
        ibRefreshTalk.setOnClickListener(v -> setTopTalkPager());

        llBoardError.setOnClickListener(v -> setSubscribeBoard());
        ibRefreshBoard.setOnClickListener(v -> setSubscribeBoard());
    }

    private void setTopTalkPager() {
        dtalkLists.clear();
        talkAdapter.notifyDataSetChanged();

        llTalkError.setVisibility(View.GONE);
        ibRefreshTalk.setVisibility(View.INVISIBLE);
        RetrofitClient.getApiService().getTop10Talks(myInfo.getUserID()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.i("FragmentHome 확인용1", response.toString());
                if (response.code() == 200) {
                    Log.i("FragmentHome 확인용1", response.body());
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
                        }
                    } catch (JSONException e) { e.printStackTrace(); }
                    talkAdapter.notifyDataSetChanged();
                    ibRefreshTalk.setVisibility(View.VISIBLE);
                }
                else if (response.code() == 400) {
                    llTalkError.setVisibility(View.VISIBLE);
                    ivTalkError.setVisibility(View.GONE);
                    tvTalkError.setVisibility(View.GONE);
                    tvNoTalk.setVisibility(View.VISIBLE);
                }
                else {
                    llTalkError.setVisibility(View.VISIBLE);
                    ivTalkError.setVisibility(View.VISIBLE);
                    tvTalkError.setVisibility(View.VISIBLE);
                    tvNoTalk.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                llTalkError.setVisibility(View.VISIBLE);
                ivTalkError.setVisibility(View.VISIBLE);
                tvTalkError.setVisibility(View.VISIBLE);
                tvNoTalk.setVisibility(View.GONE);
            }
        });
    }

    private void setSubscribeBoard() {
        bSubLists.clear();
        llBoardError.setVisibility(View.GONE);
        ibRefreshBoard.setVisibility(View.INVISIBLE);
        RetrofitClient.getApiService().getSubscribeBoard(myInfo.getUserID()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.i("FragmentHome 확인용2", response.toString());
                if (response.code() == 200) {
                    Log.i("FragmentHome 확인용2", response.body());
                    try {
                        JSONObject boardsInfo = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONArray jsonPosts = boardsInfo.getJSONArray("boards");
                        for (int i=0; i<jsonPosts.length(); i++) {
                            JSONObject boardObject = jsonPosts.getJSONObject(i);
                            if (boardObject.isNull("latestContent"))
                                bSubLists.add(new SimpleBoardInfo(boardObject.getInt("id"), boardObject.getString("board_name"), null));
                            else
                                bSubLists.add(new SimpleBoardInfo(boardObject.getInt("id"), boardObject.getString("board_name"), boardObject.getString("latestContent")));
                        }
                    } catch (JSONException e) { e.printStackTrace(); }
                    boardAdapter.notifyDataSetChanged();
                    ibRefreshBoard.setVisibility(View.VISIBLE);
                }
                else if (response.code() == 400) {      //구독한 게시판이 없을 때
                    llBoardError.setVisibility(View.VISIBLE);
                    ivBoardError.setVisibility(View.GONE);
                    tvBoardError.setVisibility(View.GONE);
                    tvNoBoard.setVisibility(View.VISIBLE);
                }
                else {
                    llBoardError.setVisibility(View.VISIBLE);
                    ivBoardError.setVisibility(View.VISIBLE);
                    tvBoardError.setVisibility(View.VISIBLE);
                    tvNoBoard.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                llBoardError.setVisibility(View.VISIBLE);
                ivBoardError.setVisibility(View.VISIBLE);
                tvBoardError.setVisibility(View.VISIBLE);
                tvNoBoard.setVisibility(View.GONE);
            }
        });
    }
}
