package com.eighteen.fecom;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eighteen.fecom.MainActivity.myInfo;


public class ApplyingActivity extends AppCompatActivity {
    private boolean isSubmitted = false;

    private EditText etProsContent, etConsContent;
    private TextView tvProsError, tvConsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applying);

        Toolbar toolbar = findViewById(R.id.applying_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        View customView = View.inflate(this, R.layout.actionbar_applying, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        toolbarListener(toolbar);

        etProsContent = findViewById(R.id.applying_prosContent);
        tvProsError = findViewById(R.id.applying_prosError);
        etConsContent = findViewById(R.id.applying_consContent);
        tvConsError = findViewById(R.id.applying_consError);
        applyingListener();
    }

    @Override
    public void finish() {
        if (isSubmitted) {
            Intent submittedIntent = new Intent();
            setResult(RESULT_OK, submittedIntent);
        }
        else {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
        }

        super.finish();
    }

    private void toolbarListener(Toolbar toolbar) {
        AppCompatButton btCancel = toolbar.findViewById(R.id.applying_cancel);
        btCancel.setOnClickListener(v -> finish());

        AppCompatButton btComplete = toolbar.findViewById(R.id.applying_complete);
        btComplete.setOnClickListener(v -> {
            String prosContent = etProsContent.getText().toString().trim();
            String consContent = etConsContent.getText().toString().trim();
            if (prosContent.length() == 0) {
                tvProsError.setVisibility(View.VISIBLE);
                tvProsError.setText(R.string.applying_prosError);
            }
            else if (consContent.length() == 0) {
                tvConsError.setVisibility(View.VISIBLE);
                tvConsError.setText(R.string.applying_consError);
            }
            else {
                JsonObject discussData = new JsonObject();
                discussData.addProperty("writer", myInfo.getUserID());
                discussData.addProperty("pros", prosContent);
                discussData.addProperty("cons", consContent);
                RetrofitClient.getApiService().postRegisterDiscuss(discussData).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == 200) {
                            isSubmitted = true;
                            finish();
                        }
                        else
                            Toast.makeText(ApplyingActivity.this, R.string.signup_submit_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Toast.makeText(ApplyingActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void applyingListener() {
        etProsContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvProsError.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        etConsContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvConsError.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }
}
