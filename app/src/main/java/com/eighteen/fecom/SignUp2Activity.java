package com.eighteen.fecom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.Objects;

public class SignUp2Activity extends AppCompatActivity {
    ImageView ivStudentCard;
    Button btStudentCard, btSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        LinearLayout llUnivAuth = findViewById(R.id.signup2_llAuth);    //TODO: 인증 시 llUnivAuth.setVisibility(VIEW.VISIBLE); 해야 함!

        ivStudentCard = findViewById(R.id.signup2_ivCard);
        btStudentCard = findViewById(R.id.signup2_btAddCard);

        btStudentCard.setOnClickListener(v -> {
            Intent intent1 = new Intent();
            intent1.setType("image/*");
            intent1.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent1, 2);
        });


        btSignUp = findViewById(R.id.signup2_btSubmit);
        btSignUp.setOnClickListener(v -> {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            try {
                InputStream in = getContentResolver().openInputStream(Objects.requireNonNull(data).getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();

                ivStudentCard.setImageBitmap(img);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
