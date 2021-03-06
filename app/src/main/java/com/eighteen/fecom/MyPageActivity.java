package com.eighteen.fecom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.eighteen.fecom.data.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

public class MyPageActivity extends AppCompatActivity {
    private TextView tvName, tvNick, tvID, tvUnivAuth, tvChNick, tvChPW, tvWritePosts, tvLikePosts, tvVersion, tvAnnouncement, tvUseInfo;
    private Button btLogout;
    private ActivityResultLauncher<Intent> startChangeActivityResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Toolbar toolbar = findViewById(R.id.mypage_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_mypage, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        tvName = findViewById(R.id.mypage_name);
            tvName.setText(myInfo.getName());
        tvNick = findViewById(R.id.mypage_nick);
            tvNick.setText(myInfo.getNick());
        tvID = findViewById(R.id.mypage_ID);
            tvID.setText(myInfo.getEmail());
        tvUnivAuth = findViewById(R.id.mypage_univAuth);
        tvChNick = findViewById(R.id.mypage_chNick);
        tvChPW = findViewById(R.id.mypage_chPW);
        //tvWritePosts = findViewById(R.id.mypage_writePosts);
        //tvLikePosts = findViewById(R.id.mypage_likePosts);

        tvVersion = findViewById(R.id.mypage_version);
        tvVersion.setText(BuildConfig.VERSION_NAME);
        tvAnnouncement = findViewById(R.id.mypage_announcement);
        tvUseInfo = findViewById(R.id.mypage_useInfo);
        btLogout = findViewById(R.id.mypage_btLogout);

        startChangeActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK);
                        updateMyInfo();
                });

        updateMyInfo();
        mypageClickListener();
    }

    private void toolbarListener(Toolbar toolbar) {
        ImageView ivBack = toolbar.findViewById(R.id.mypage_back);
        ivBack.setOnClickListener(v -> finish());
    }

    private void updateMyInfo() {
        RetrofitClient.getApiService().getUserInfoByID(myInfo.getUserID()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    Log.i("MyPageActivity ?????????", response.body());
                    try {
                        JSONObject userInfo = new JSONObject(Objects.requireNonNull(response.body()));
                        JSONObject user = userInfo.getJSONObject("user");

                        int univCode = -1;
                        if (!user.isNull("univ_code"))
                            univCode = user.getInt("univ_code");

                        String univName = "";
                        if (!user.isNull("univ_name"))
                            univName = user.getString("univ_name");

                        myInfo = new UserInfo(user.getInt("id"), user.getString("username"), user.getString("nickname"),
                                user.getString("email"), univCode, univName);
                    } catch (JSONException e) { e.printStackTrace(); }
                    tvName.setText(myInfo.getName());
                    tvNick.setText(myInfo.getNick());
                    tvID.setText(myInfo.getEmail());
                }
                else
                    Toast.makeText(MyPageActivity.this, "?????? ????????? ???????????? ???????????????:(", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(MyPageActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mypageClickListener() {
        tvUnivAuth.setOnClickListener(v -> {
            //TODO: ?????? ?????? ???????????? ?????????
            startActivity(new Intent(this, SignUp2Activity.class));
        });

        tvChNick.setOnClickListener(v -> startChangeActivityResult.launch(new Intent(this, ChangeNickActivity.class)));

        tvChPW.setOnClickListener(v -> startChangeActivityResult.launch(new Intent(this, ChangePWActivity.class)));

        /*
        tvWritePosts.setOnClickListener(v -> {
            //TODO: ?????? ????????? ??? ???????????? ?????????
            //startActivity(new Intent(this, .class));
        });

        tvLikePosts.setOnClickListener(v -> {
            //TODO: ?????? ???????????? ??? ???????????? ?????????
            //startActivity(new Intent(this, .class));
        });

         */

        tvAnnouncement.setOnClickListener(v -> startActivity(new Intent(this, MyPageNoticeActivity.class)));

        tvUseInfo.setOnClickListener(v -> startActivity(new Intent(this, MyPageUseInfoActivity.class)));

        btLogout.setOnClickListener(v -> {
            Intent logoutIntent = new Intent(this, LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logoutIntent);
            finish();
        });
    }
}
