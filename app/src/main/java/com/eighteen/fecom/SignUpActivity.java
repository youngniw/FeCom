package com.eighteen.fecom;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    Button btCheckNick, btCheckEmail, btSubmit;
    EditText etName, etNick, etEmail, etPW;
    TextView tvNameError, tvNickError, tvEmailError, tvPWError, tvSubmitError;
    boolean isCheckedNick = false, isCheckedEmail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btCheckNick = findViewById(R.id.signup_btCheckNick);
        btCheckEmail = findViewById(R.id.signup_btCheckEmail);
        btSubmit = findViewById(R.id.signup_btSubmit);
        etName = findViewById(R.id.signup_etName);
        etNick = findViewById(R.id.signup_etNick);
        etEmail = findViewById(R.id.signup_etEmail);
        etPW = findViewById(R.id.signup_etPW);
        tvNameError = findViewById(R.id.signup_errorName);
        tvNickError = findViewById(R.id.signup_errorNick);
        tvEmailError = findViewById(R.id.signup_errorEmail);
        tvPWError = findViewById(R.id.signup_errorPW);
        tvSubmitError = findViewById(R.id.signup_errorSubmit);

        signupTextListener();
        signupClickListener();
    }
    public void signupTextListener() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNameError.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        etNick.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isCheckedNick = false;
                tvNickError.setVisibility(View.GONE);
                tvNickError.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.red));
                btCheckNick.setBackground(AppCompatResources.getDrawable(SignUpActivity.this, R.drawable.bg_bt_check));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isCheckedEmail = false;
                tvEmailError.setVisibility(View.GONE);
                tvEmailError.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.red));
                btCheckEmail.setBackground(AppCompatResources.getDrawable(SignUpActivity.this, R.drawable.bg_bt_check));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        etPW.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvPWError.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    public void signupClickListener() {
        btCheckNick.setOnClickListener(v -> {
            etNick.setEnabled(false);
            String nickname = etNick.getText().toString().trim();
            if (nickname.length() == 0) {
                etNick.setEnabled(true);
                tvNickError.setVisibility(View.VISIBLE);
                tvNickError.setText("닉네임을 입력해주세요.");
            }
            else {
                tvNickError.setVisibility(View.GONE);
                RetrofitClient.getApiService().getCheckNick(nickname).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        tvNickError.setVisibility(View.VISIBLE);

                        if (response.code() == 200) {
                            tvNickError.setText("사용 가능한 닉네임입니다.");

                            isCheckedNick = true;
                            etNick.clearFocus();
                            tvNickError.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.light_green));
                            btCheckNick.setBackground(AppCompatResources.getDrawable(SignUpActivity.this, R.drawable.bg_bt_checked));
                        }
                        else
                            tvNickError.setText(R.string.signup_nick_error2);
                        etNick.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        etNick.setEnabled(true);
                        tvNickError.setVisibility(View.VISIBLE);
                        tvNickError.setText("서버와 연결되지 않습니다. 네트워크를 확인해주세요.");
                    }
                });
            }
        });

        btCheckEmail.setOnClickListener(v -> {
            etEmail.setEnabled(false);
            String emailID = etEmail.getText().toString().trim();
            if (emailID.length() == 0) {
                etEmail.setEnabled(true);
                tvEmailError.setVisibility(View.VISIBLE);
                tvEmailError.setText(R.string.login_id_error);
            }
            else {
                tvEmailError.setVisibility(View.GONE);
                RetrofitClient.getApiService().getCheckEmail(emailID).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        tvEmailError.setVisibility(View.VISIBLE);

                        if (response.code() == 200) {
                            tvEmailError.setText(R.string.signup_id_ok);

                            isCheckedEmail = true;
                            etEmail.clearFocus();
                            tvEmailError.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.light_green));
                            btCheckEmail.setBackground(AppCompatResources.getDrawable(SignUpActivity.this, R.drawable.bg_bt_checked));
                        }
                        else
                            tvEmailError.setText(R.string.signup_id_error);
                        etEmail.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        etEmail.setEnabled(true);
                        tvEmailError.setVisibility(View.VISIBLE);
                        tvEmailError.setText("서버와 연결되지 않습니다. 네트워크를 확인해주세요.");
                    }
                });
            }
        });

        btSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String nick = etNick.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String pw = etPW.getText().toString().trim();

            if (name.length() == 0)
                tvNameError.setVisibility(View.VISIBLE);
            else if (!isCheckedNick) {
                tvNickError.setVisibility(View.VISIBLE);
                tvNickError.setText("닉네임 중복 확인해 주세요.");
            }
            else if (!isCheckedEmail) {
                tvEmailError.setVisibility(View.VISIBLE);
                tvEmailError.setText(R.string.signup_id_error2);
            }
            else if (pw.length() == 0)
                tvPWError.setVisibility(View.VISIBLE);
            else {
                tvSubmitError.setVisibility(View.GONE);

                JsonObject userData = new JsonObject();
                userData.addProperty("username", name);
                userData.addProperty("nickname", nick);
                userData.addProperty("email", email);
                userData.addProperty("pw", pw);
                RetrofitClient.getApiService().postUserInfo(userData).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 200) {
                            Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(loginIntent);
                            finish();
                        }
                        else {
                            tvSubmitError.setVisibility(View.VISIBLE);
                            tvSubmitError.setText(R.string.signup_submit_error);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        tvEmailError.setVisibility(View.VISIBLE);
                        tvEmailError.setText("서버와 연결되지 않습니다. 네트워크를 확인해 주세요.");
                    }
                });
            }
        });
    }
}
