package com.paktalin.receiptanalyzer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Paktalin on 13.03.2018.
 */

public class EditActivity extends AppCompatActivity {

    ImageView buttonRotate;
    ImageView image;
    Bitmap bitmap;
    Button buttonOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_edit);

        buttonRotate = findViewById(R.id.button_rotate);
        image = findViewById(R.id.image);
        buttonOk = findViewById(R.id.button_ok);

        bitmap = DirectoryManager.getBitmap();
        image.setImageBitmap(bitmap);
        buttonRotate.setOnClickListener(buttonRotateListener);
        buttonOk.setOnClickListener(buttonOkListener);
    }

    View.OnClickListener buttonRotateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            image.setImageBitmap(bitmap);
        }
    };

    View.OnClickListener buttonOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DirectoryManager.saveBitmap(bitmap);
            Intent okIntent = new Intent(EditActivity.this, MainActivity.class);
            startActivity(okIntent);
        }
    };
}