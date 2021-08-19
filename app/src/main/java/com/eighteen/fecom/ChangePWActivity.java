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

public class ChangePWActivity extends AppCompatActivity {
    private AppCompatImageButton ibBack;
    private Button btSubmit;
    private EditText etPW, etPWCheck;
    private TextView tvPWError, tvSubmitError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nick);

        ibBack = findViewById(R.id.chPW_btBack);
        etPW = findViewById(R.id.chPW_etPW);
        etPWCheck = findViewById(R.id.chPW_etPW_check);
        tvPWError = findViewById(R.id.chPW_errorPW);
        btSubmit = findViewById(R.id.chPW_btSubmit);
        tvSubmitError = findViewById(R.id.chPW_errorSubmit);

        chNickListener();
    }

    private void chNickListener() {
        ibBack.setOnClickListener(v -> finish());

        btSubmit.setOnClickListener(v -> {
            String newPW = etPW.getText().toString().trim();
            String newCheckPW = etPWCheck.getText().toString().trim();
            if (newPW.length() == 0 || newCheckPW.length() == 0) {
                tvPWError.setVisibility(View.VISIBLE);
                tvPWError.setText(R.string.login_pw_error);
            }
            else if (!newPW.equals(newCheckPW)) {
                tvPWError.setVisibility(View.VISIBLE);
                tvPWError.setText("입력하신 비밀번호가 다릅니다.");
            }

            else {
                tvSubmitError.setVisibility(View.GONE);

                RetrofitClient.getApiService().postChangePW(myInfo.getUserID(), newPW).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 200) {
                            Toast.makeText(ChangePWActivity.this, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
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
