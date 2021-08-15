package com.eighteen.fecom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class SignUp2Activity extends AppCompatActivity {
    ImageView card2Img;
    Button card2Btn, registerEndBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        card2Img = findViewById(R.id.card2Img);
        card2Btn = findViewById(R.id.card2Btn);

        card2Btn.setOnClickListener(v -> {
            Intent intent1 = new Intent();
            intent1.setType("image/*");
            intent1.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent1, 2);
        });


        registerEndBtn = findViewById(R.id.registerEndBtn);
        registerEndBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                // 선택한 이미지에서 비트맵 생성
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                // 이미지 표시
//                if (requestCode == 1) {
//                    card1Img.setImageBitmap(img);
//                }
                if (requestCode == 2) {  // requeseCode == 2
                    card2Img.setImageBitmap(img);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
