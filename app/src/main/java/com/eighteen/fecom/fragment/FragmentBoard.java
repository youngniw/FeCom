package com.eighteen.fecom.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eighteen.fecom.R;
import com.eighteen.fecom.RetrofitClient;
import com.eighteen.fecom.adapter.BoardRecyclerAdapter;
import com.eighteen.fecom.data.BoardInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentBoard extends Fragment {
    private int userID = -1;
    ArrayList<BoardInfo> basicBoardLists = null, subBoardLists = null;
    BoardRecyclerAdapter basicAdapter, subAdapter;
    NestedScrollView nsvBoard;
    TextView tvInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_board, container, false);

        userID = getArguments().getInt("userID");

        basicBoardLists = new ArrayList<>();
        subBoardLists = new ArrayList<>();

        nsvBoard = rootView.findViewById(R.id.fBoard_nsv);
        nsvBoard.setVisibility(View.GONE);
        tvInfo = rootView.findViewById(R.id.fBoard_info);

        RecyclerView rvBasicBoard = rootView.findViewById(R.id.fBoard_rvBasic);      //기본 게시판
        LinearLayoutManager basicManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvBasicBoard.setLayoutManager(basicManager);
        basicAdapter = new BoardRecyclerAdapter(basicBoardLists);
        rvBasicBoard.setAdapter(basicAdapter);

        RecyclerView rvSubBoard = rootView.findViewById(R.id.fBoard_rvSub);          //부가적 게시판
        LinearLayoutManager subManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvSubBoard.setLayoutManager(subManager);
        subAdapter = new BoardRecyclerAdapter(subBoardLists);
        rvSubBoard.setAdapter(subAdapter);

        setBoardList();

        return rootView;
    }

    public void setBoardList() {
        RetrofitClient.getApiService().getBoard(userID).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.i("FragmentBoard 확인용1", response.toString());
                Log.i("FragmentBoard 확인용2", response.body());
                if (response.code() == 200) {
                    basicBoardLists.clear();
                    subBoardLists.clear();

                    tvInfo.setVisibility(View.GONE);
                    nsvBoard.setVisibility(View.VISIBLE);

                    try {
                        JSONObject boardsInfo = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONArray jsonPosts = boardsInfo.getJSONArray("boards");
                        for (int i=0; i<jsonPosts.length(); i++) {
                            JSONObject boardObject = jsonPosts.getJSONObject(i);
                            if (boardObject.getInt("essential") == 1)
                                basicBoardLists.add(new BoardInfo(boardObject.getInt("id"), boardObject.getString("board_name"), 1, boardObject.getInt("subscribe")));
                            else
                                subBoardLists.add(new BoardInfo(boardObject.getInt("id"), boardObject.getString("board_name"), 0, boardObject.getInt("subscribe")));
                        }
                        basicAdapter.notifyDataSetChanged();
                        subAdapter.notifyDataSetChanged();
                    } catch (JSONException e) { e.printStackTrace(); }
                }
                else {
                    tvInfo.setVisibility(View.VISIBLE);
                    tvInfo.setText("게시판 로딩 실패\n 다시 조회해 주세요:)");
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                tvInfo.setVisibility(View.VISIBLE);
                tvInfo.setText("게시판 로딩 실패\n 다시 조회해 주세요:)");
            }
        });
    }
}
