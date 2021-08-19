package com.eighteen.fecom;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

public class ChangeNickActivity extends AppCompatActivity {
    private boolean isCheckedNick = false;

    private AppCompatImageButton ibBack;
    private AppCompatButton ibCheck;
    private Button btSubmit;
    private EditText etNick;
    private TextView tvNickError, tvSubmitError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nick);

        ibBack = findViewById(R.id.chNick_ibBack);
        etNick = findViewById(R.id.chNick_etName);
        ibCheck = findViewById(R.id.chNick_ibCheck);
        tvNickError = findViewById(R.id.chNick_errorNick);
        btSubmit = findViewById(R.id.chNick_btSubmit);
        tvSubmitError = findViewById(R.id.chNick_errorSubmit);

        chNickListener();
    }

    private void chNickListener() {
        ibBack.setOnClickListener(v -> finish());

        etNick.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isCheckedNick = false;
                tvNickError.setVisibility(View.GONE);
                tvNickError.setTextColor(ContextCompat.getColor(ChangeNickActivity.this, R.color.red));
                ibCheck.setBackground(AppCompatResources.getDrawable(ChangeNickActivity.this, R.drawable.bg_bt_check));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        ibCheck.setOnClickListener(v -> {
            etNick.setEnabled(false);

            String nickname = etNick.getText().toString().trim();
            if (nickname.length() == 0) {
                etNick.setEnabled(true);
                tvNickError.setVisibility(View.VISIBLE);
                tvNickError.setText(R.string.signup_nick_error);
            }
            else {
                tvNickError.setVisibility(View.GONE);
                RetrofitClient.getApiService().getCheckNick(nickname).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        tvNickError.setVisibility(View.VISIBLE);

                        if (response.code() == 200) {
                            isCheckedNick = true;
                            etNick.clearFocus();
                            tvNickError.setText("사용 가능한 닉네임입니다.");
                            tvNickError.setTextColor(ContextCompat.getColor(ChangeNickActivity.this, R.color.light_green));
                            ibCheck.setBackground(AppCompatResources.getDrawable(ChangeNickActivity.this, R.drawable.bg_bt_checked));
                        }
                        else
                            tvNickError.setText(R.string.signup_nick_error2);
                        etNick.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        etNick.setEnabled(true);
                        tvNickError.setVisibility(View.VISIBLE);
                        tvNickError.setText(R.string.server_error);
                    }
                });
            }
        });

        btSubmit.setOnClickListener(v -> {
            String nick = etNick.getText().toString().trim();
            if (!isCheckedNick) {
                tvNickError.setVisibility(View.VISIBLE);
                tvNickError.setText("닉네임 중복 확인해 주세요.");
            }
            else {
                tvSubmitError.setVisibility(View.GONE);

                RetrofitClient.getApiService().postChangeNick(myInfo.getUserID(), nick).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 200) {
                            Toast.makeText(ChangeNickActivity.this, "닉네임 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            tvSubmitError.setVisibility(View.VISIBLE);
                            tvSubmitError.setText(R.string.signup_submit_error);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        tvSubmitError.setVisibility(View.VISIBLE);
                        tvSubmitError.setText(R.string.server_error);
                    }
                });
            }
        });
    }
}
