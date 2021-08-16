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

import com.eighteen.fecom.data.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;

public class LoginActivity extends AppCompatActivity {
    Button btLogin, btSignUp;
    EditText etEmail, etPW;
    TextView tvEmailError, tvPWError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.login_etEmail);
        etPW = findViewById(R.id.login_etPW);
        tvEmailError = findViewById(R.id.login_errorEmail);
        tvPWError = findViewById(R.id.login_errorPW);
        btLogin = findViewById(R.id.login_btLogin);
        btSignUp = findViewById(R.id.login_btSignUp);

        loginTextListener();
        loginClickListener();
    }

    public void loginTextListener() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvEmailError.setVisibility(View.GONE);
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

    public void loginClickListener() {
        btLogin.setOnClickListener(v -> {
            tvEmailError.setVisibility(View.GONE);
            tvPWError.setVisibility(View.GONE);

            String email = etEmail.getText().toString();
            String pw = etPW.getText().toString();
            if (email.trim().length() == 0) {
                tvEmailError.setVisibility(View.VISIBLE);
                tvEmailError.setText(R.string.login_id_error);
            }
            else if (pw.trim().length() == 0) {
                tvPWError.setVisibility(View.VISIBLE);
                tvPWError.setText(R.string.login_pw_error);
            }
            else {
                RetrofitClient.getApiService().getUserInfo(email, pw).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 200) {
                            try {
                                JSONObject userInfo = new JSONObject(Objects.requireNonNull(response.body()));
                                JSONObject user = userInfo.getJSONObject("user");
                                myInfo = new UserInfo(user.getInt("id"), user.getString("username"), user.getString("nickname"),
                                        user.getString("email"), user.getInt("univ_code"), user.getString("univ_name"));
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } catch (JSONException e) { e.printStackTrace(); }
                        }
                        else if (response.code() == 401) {
                            tvEmailError.setVisibility(View.VISIBLE);
                            tvEmailError.setText(R.string.login_id_error2);
                        }
                        else {      //response.code() == 402
                            tvPWError.setVisibility(View.VISIBLE);
                            tvPWError.setText(R.string.login_pw_error2);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        tvPWError.setVisibility(View.VISIBLE);
                        tvPWError.setText("서버와 연결되지 않습니다. 네트워크를 확인해주세요:)");
                    }
                });
            }
        });

        btSignUp.setOnClickListener( v -> startActivity(new Intent(this, SignUpActivity.class)));
    }
}
