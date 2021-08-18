package com.eighteen.fecom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eighteen.fecom.data.UserInfo;
import com.eighteen.fecom.fragment.FragmentBoard;
import com.eighteen.fecom.fragment.FragmentHome;
import com.eighteen.fecom.fragment.FragmentMajorCommunity;
import com.eighteen.fecom.fragment.FragmentMessage;
import com.eighteen.fecom.fragment.FragmentNotice;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static UserInfo myInfo = null;
    private ActionBar actionBar;
    private FragmentManager fragmentManager;
    private FragmentBoard fragmentBoard;
    private FragmentHome fragmentHome;
    private FragmentMajorCommunity fragmentMajorCommunity;
    private FragmentNotice fragmentNotice;
    private FragmentMessage fragmentMessage;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_tab_home, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        homeToolbarListener(toolbar);

        fragmentManager = getSupportFragmentManager();
        fragmentBoard = new FragmentBoard();
        fragmentHome = new FragmentHome();
        fragmentMajorCommunity = new FragmentMajorCommunity();
        fragmentNotice = new FragmentNotice();
        fragmentMessage = new FragmentMessage();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_framelayout, fragmentHome).commitAllowingStateLoss();

        tabToolbarListener(toolbar);
    }

    private void homeToolbarListener(Toolbar toolbar) {
        AppCompatImageButton ivSearch = toolbar.findViewById(R.id.homeTB_search);
        ivSearch.setOnClickListener(v -> {
            //TODO: 검색 창으로 넘어감!
        });

        AppCompatImageButton ivMyPage = toolbar.findViewById(R.id.homeTB_mypage);
        ivMyPage.setOnClickListener(v -> startActivity(new Intent(this, MyPageActivity.class)));
    }

    @SuppressLint("NonConstantResourceId")
    private void tabToolbarListener(Toolbar toolbar) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.main_tab);
        bottomNavigationView.setSelectedItemId(R.id.tab_home);

        bottomNavigationView.getMenu().getItem(1).setOnMenuItemClickListener(item -> {      //데일리톡 클릭 시
            startActivity(new Intent(MainActivity.this, DailyTalkActivity.class));

            return true;
        });

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            FragmentTransaction transactionNow = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.tab_board: {
                    Bundle bundle = new Bundle();
                        bundle.putInt("userID", myInfo.getUserID());
                    fragmentBoard.setArguments(bundle);
                    transactionNow.replace(R.id.main_framelayout, fragmentBoard).commitAllowingStateLoss();

                    View actionBarView = View.inflate(this, R.layout.actionbar_tab_board, null);
                    actionBar.setCustomView(actionBarView, params);
                    boardToolbarListener(toolbar);

                    break;
                }
                case R.id.tab_home: {
                    transactionNow.replace(R.id.main_framelayout, fragmentHome).commitAllowingStateLoss();

                    View actionBarView = View.inflate(this, R.layout.actionbar_tab_home, null);
                    actionBar.setCustomView(actionBarView, params);
                    homeToolbarListener(toolbar);

                    break;
                }
                case R.id.tab_majorCommunity: {
                    transactionNow.replace(R.id.main_framelayout, fragmentMajorCommunity).commitAllowingStateLoss();

                    View actionBarView = View.inflate(this, R.layout.actionbar_tab_major, null);
                    actionBar.setCustomView(actionBarView, params);
                    TextView tvTitle = toolbar.findViewById(R.id.tabTB_title);
                    tvTitle.setText(R.string.tab_majorcommunity);

                    break;
                }
                case R.id.tab_notice: {
                    transactionNow.replace(R.id.main_framelayout, fragmentNotice).commitAllowingStateLoss();

                    View actionBarView = View.inflate(this, R.layout.actionbar_tab_notice, null);
                    actionBar.setCustomView(actionBarView, params);
                    tabNoticeSetting(toolbar);

                    break;
                }
            }
            return true;
        });
    }

    private void boardToolbarListener(Toolbar toolbar) {
        AppCompatImageButton ivSearch = toolbar.findViewById(R.id.boardTB_search);
        ivSearch.setOnClickListener(v -> {
            //TODO: 검색 창으로 넘어감
        });

        AppCompatImageButton ivAdd = toolbar.findViewById(R.id.boardTB_add);
        ivAdd.setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_board, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getRealSize(size);
            int width = size.x;
            params.width = (int) (width*0.75);
            alertDialog.getWindow().setAttributes(params);

            TextView tvError = dialogView.findViewById(R.id.dialog_board_error);
            tvError.setVisibility(View.GONE);

            Button btAddComplete = dialogView.findViewById(R.id.dialog_board_btComplete);
            btAddComplete.setOnClickListener(view -> {
                EditText etBoardName = dialogView.findViewById(R.id.dialog_board_etName);

                String boardName = etBoardName.getText().toString().trim();
                if (boardName.length() == 0) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(R.string.signup_name_error);
                }
                else {
                    JsonObject boardData = new JsonObject();
                    boardData.addProperty("board_name", boardName);
                    boardData.addProperty("essential", 0);
                    RetrofitClient.getApiService().postAddBoard(boardName, 0).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            Log.i("MainActivity 확인용", response.toString());
                            if (response.code() == 200) {
                                alertDialog.dismiss();
                                fragmentBoard.setBoardList();
                            }
                            else {
                                tvError.setVisibility(View.VISIBLE);
                                tvError.setText("이미 해당 이름의 게시판이 존재합니다.");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            tvError.setVisibility(View.VISIBLE);
                            tvError.setText("서버와 연결되지 않습니다. 네트워크를 확인해 주세요.");
                        }
                    });
                }
            });

            Button btAddCancel = dialogView.findViewById(R.id.dialog_board_btCancel);
            btAddCancel.setOnClickListener(view -> alertDialog.dismiss());
        });
    }

    private void tabNoticeSetting(Toolbar toolbar) {
        TextView tvMessage = toolbar.findViewById(R.id.noticeTB_message);
        TextView tvNotice = toolbar.findViewById(R.id.noticeTB_notice);
        tvNotice.setEnabled(false);

        tvNotice.setOnClickListener(v -> {
            tvNotice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            tvMessage.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white_transparent));
            tvNotice.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_tab_underline));
            tvMessage.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_tab_underline_none));

            FragmentTransaction transactionNow = fragmentManager.beginTransaction();
            transactionNow.replace(R.id.main_framelayout, fragmentNotice).commitAllowingStateLoss();

            tvNotice.setEnabled(false);
            tvMessage.setEnabled(true);
        });

        tvMessage.setOnClickListener(v -> {
            tvNotice.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white_transparent));
            tvMessage.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            tvNotice.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_tab_underline_none));
            tvMessage.setBackground(AppCompatResources.getDrawable(this, R.drawable.bg_tab_underline));

            FragmentTransaction transactionNow = fragmentManager.beginTransaction();
            transactionNow.replace(R.id.main_framelayout, fragmentMessage).commitAllowingStateLoss();

            tvNotice.setEnabled(true);
            tvMessage.setEnabled(false);
        });
    }
}
